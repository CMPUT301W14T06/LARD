package ca.ualberta.lard;

import java.util.ArrayList;
import ca.ualberta.lard.R;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;
import ca.ualberta.lard.model.GeoLocation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The main activity is the main page where we view top level comments, their 
 * distance and how many replies they have. From this activity you can select
 * comments, go to the favorites comment activity and create new top level comments
 * 
 * @author Eldon Lake
 */
public class MainActivity extends Activity {
private CommentListBaseAdapter adapter;
private ArrayList<Comment> allComments;
private ListView commentList;
private GeoLocation sortLocation;

	/**
	 * Gets the action bar and sets the comment ListView Adapter that lets you 
	 * click on comments and go to the CommentActivity that will let you view the 
	 * Top-level comments in more detail
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        
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
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();	
       	inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Adds functionality to the menu options in the action bar menu. Options include
     * createing a new comment, setting your location, going to favourites, going to
     * followed authors, setting username and sorting the comments list.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      	case R.id.action_new:
      		Intent i = new Intent(getBaseContext(), NewEditCommentActivity.class);
      		i.putExtra(NewEditCommentActivity.PARENT_STRING, (String) null);
      		startActivity(i);
      		break;
    	case R.id.action_location:
    		Intent j = new Intent(getBaseContext(), LocationSelectionActivity.class);
        	startActivity(j);
        	break;
    	case R.id.action_favourites:
    		Intent fav = new Intent(getBaseContext(), FavouriteActivity.class);
        	startActivity(fav);
        	break;
    	case R.id.action_followed:
    		Intent fol = new Intent(getBaseContext(), FollowActivity.class);
    		startActivity(fol);
    		break;
    	case R.id.action_set_username:
    		DialogFragment newFragment = new SetUsernameFragment();
    	    newFragment.show(getFragmentManager(), "SetUsername");
    		break;
    	case R.id.action_sort:
    		final String[] sortOptions =  {"Sort by location", "Sort by date", "Sort by pictures"};

    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("How would you like to sort?");
    		builder.setItems(sortOptions, new DialogInterface.OnClickListener() {
    		    @Override
    		    public void onClick(DialogInterface dialog, int which) {
		        	CommentController cController = new CommentController();
    		        switch(which) {
    		        case 0:
    		        	Intent intent = new Intent(getBaseContext(), LocationSelectionActivity.class);
    		    		startActivityForResult(intent, NewEditCommentActivity.LOCATION_REQUEST_ID);
    		        	break;
    		        case 1:
    		        	allComments = cController.sortByCreationDate(allComments);
    		        	adapter.notifyDataSetChanged();
    		        	break;
    		        case 2:
    		        	allComments = cController.sortPicturesFirst(allComments);
    		        	adapter.notifyDataSetChanged();
    		        	break;
    		        	
    		        }
    		    }
    		});
    		builder.show();
        }

      return true;
    } 
    
    /**
     * Starts the activity by creating the Comment adapter and then fetches nearby
     * comments from the comment controller
     */
    @Override
    protected void onStart() {
    	super.onStart();
    	allComments = new ArrayList<Comment>();
    	adapter = new CommentListBaseAdapter(this, allComments);
    	commentList.setAdapter(adapter);
    	FetchNearbyComments fetch = new FetchNearbyComments();
    	fetch.execute(this);
    }
	
	/**
	 * Grabs all the comments, caches them, and displays them sorted by location
	 * @author Eldon Lake
	 * @param context
	 */
    private class FetchNearbyComments extends AsyncTask<Context, Integer, ArrayList<Comment>> {
		ProgressDialog spinner = new ProgressDialog(MainActivity.this); 
    	
		/**
		 * Display a spinner while retrieving children
		 */
    	@Override
    	protected void onPreExecute() {
    		spinner.setMessage("Loading Comments...");
    		spinner.show();
    	}
    	
    	/**
    	 * fetch all the comments off of the server
    	 */
    	@Override
    	protected ArrayList<Comment> doInBackground(Context... params) {
    		CommentRequest proximityRequest = new CommentRequest(200);
    		GeoLocation loc = new GeoLocation(getBaseContext());
    		if (sortLocation != null) {
    			loc = sortLocation;
    		}
    		CommentController controller = new CommentController(proximityRequest, params[0]);
    		return controller.sortByLocation(controller.get(), loc);  
    	}

    	/**
    	 * Cache all the comments (this is a master retrieve) and notifies the adapter that
    	 * the comment list has changed, then dismisses the loading spinner
    	 */
    	protected void onPostExecute(ArrayList<Comment> result) {
    		allComments.clear();
    		DataModel.saveLocal(result, false, getBaseContext()); 
    		for (Comment topLevelComment : result)
    		{
    			if (topLevelComment.getParentId() == null)
    			{
    				allComments.add(topLevelComment);
    			}
    		}
    		adapter.notifyDataSetChanged();
    		
    		spinner.dismiss();
    	}
    }
    
    /**
     * Sets the GeoLocation used for soring comments
     */
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	System.out.println("We got our activity result");
		if (requestCode == NewEditCommentActivity.LOCATION_REQUEST_ID) {
			if (resultCode == RESULT_OK) {
				String locationData = data
						.getStringExtra(LocationSelectionActivity.LOCATION_REQUEST);
				sortLocation = GeoLocation.fromJSON(locationData);
			}
		}
    }
    
}