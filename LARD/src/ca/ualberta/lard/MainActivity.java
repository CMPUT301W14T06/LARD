package ca.ualberta.lard;

import java.util.ArrayList;

import ca.ualberta.lard.R;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The main activity is the main page where we view top level comments.
 * 
 * @author Eldon Lake
 */

public class MainActivity extends Activity {
private CommentListBaseAdapter adapter;
private ArrayList<Comment> allComments;
private ListView commentList;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();	
       	inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      // These are the menu options in the action bar menu
      	case R.id.action_new:
      		Intent i = new Intent(getBaseContext(), NewCommentActivity.class);
      		startActivity(i);
      		break;
    	case R.id.action_location:
    		Intent j = new Intent(getBaseContext(), LocationSelectionActivity.class);
        	startActivity(j);
        	break;
        }

      return true;
    } 
    
    @Override
    protected void onStart() {
    	super.onStart();
    	allComments = new ArrayList<Comment>();
    	adapter = new CommentListBaseAdapter(this, allComments);
    	commentList.setAdapter(adapter);
    	FetchComments fetch = new FetchComments();
    	fetch.execute(this);
    }

    private class FetchComments extends AsyncTask<Context, Integer, ArrayList<Comment>> {
    	@Override
    	protected ArrayList<Comment> doInBackground(Context... params) {
    		CommentController controller = new CommentController(params[0]);
    		return controller.get(); // fetch all the comments off of the server
    	}

    	protected void onPostExecute(ArrayList<Comment> result) {
    		allComments.clear();
    		allComments.addAll(result);
    		adapter.notifyDataSetChanged();
    	}
    }
}