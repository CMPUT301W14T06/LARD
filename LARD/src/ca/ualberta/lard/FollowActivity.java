package ca.ualberta.lard;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import android.view.View;

/**
 * Gives you an up-to-date list of comments from your followed users and their comments
 */
public class FollowActivity extends MainActivity {
	private CommentListBaseAdapter adapter;
	private ArrayList<Comment> allComments;
	private ListView commentList;

	/**
	 * Populates the Action bar and Listview, making it so you are able to click on followed 
	 * comments to view them in more detail
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        commentList = (ListView) findViewById(R.id.threadsListView);
        
        commentList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				// Select a top level comment, pass its id to CommentActivity to view it and its children
				Intent i = new Intent(getBaseContext(), CommentActivity.class);
				Comment selection = allComments.get(position);
				i.putExtra(CommentActivity.EXTRA_PARENT_ID, selection.getId());
				System.err.println("Starting Comment Activity with commentID " + selection.getId());
				startActivity(i);
			}
		});
    }

    /**
     * Hides not needed options and changes the option that goes to main into 
     * an option that goes into favorites instead
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();	
       	inflater.inflate(R.menu.main_menu, menu);
		//sets the favourite option to be a go back to main option instead
		MenuItem favouriteItem = menu.findItem(R.id.action_favourites);
		favouriteItem.setTitle("Main");
		//hides stuff the user does not need to see
		MenuItem addNewItem = menu.findItem(R.id.action_new);
		addNewItem.setVisible(false);
		MenuItem usernameItem = menu.findItem(R.id.action_set_username);
		usernameItem.setVisible(false);
        return true;
    }

    /**
     * Gives function to the option items that give you sorting ability and going back
     * to main.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		// These are the menu options in the action bar menu
    		case R.id.action_location:
    			Intent j = new Intent(getBaseContext(), LocationSelectionActivity.class);
    			startActivity(j);
    			break;
    		case R.id.action_favourites:
    			//go back by just finishing the activity
    			finish();
    			break;
    			
    		case android.R.id.home:
    			// we got a call to the parent activity
    			finish();
    			break;
    		case R.id.action_sort:
			
    	}
      return true;
    } 
    
    /**
     * Immediately tries to fetch all the favorite comments from server
     */
    @Override
    protected void onStart() {
    	super.onStart();
    	allComments = new ArrayList<Comment>();
    	adapter = new CommentListBaseAdapter(this, allComments);
    	commentList.setAdapter(adapter);
		FetchFollowedComments fetch = new FetchFollowedComments();
		fetch.execute(this);
    }
    
    /**
     * An AsyncTask task that calls a function in the Comment controller that will filter
     * out all followed comments from the other comments.
     */
    private class FetchFollowedComments extends AsyncTask<Context, Integer, ArrayList<Comment>> {
    	
       	/**
    	 * Calls the controller to request a comments list that consist of followe comments
    	 */
    	@Override
    	protected ArrayList<Comment> doInBackground(Context... params) {
    		CommentRequest req = new CommentRequest(20);
    		req.setParentId(null);
    		CommentController controller = new CommentController(req, params[0]);
			return controller.getFollowedComments();
    	}  
    	/**
    	 * Tells the adapter that the followed comments list has changed
    	 */
    	protected void onPostExecute(ArrayList<Comment> result) {
    		allComments.clear();
    		allComments.addAll(result);
    		adapter.notifyDataSetChanged();
    	}
    }
    
}