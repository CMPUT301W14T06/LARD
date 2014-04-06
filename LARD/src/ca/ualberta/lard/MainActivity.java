package ca.ualberta.lard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import ca.ualberta.lard.R;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;
import ca.ualberta.lard.model.Favourites;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.User;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
private boolean favouriteMode;
private Menu menu;

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
       	this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      // These are the menu options in the action bar menu
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
    		if (favouriteMode == false) {
    			favouriteMode = true;
    			//sets item labels
    			getActionBar().setTitle("Favorites");
    			MenuItem favouriteItem = menu.findItem(R.id.action_favourites);
    			favouriteItem.setTitle("Main");
    			//fetches the favrourite comments
    			FetchFavoriteComments fetch = new FetchFavoriteComments();
    			fetch.execute(this);
    		} else {
    			favouriteMode = false;
    			//sets item labels
    			getActionBar().setTitle("LARD");
    			MenuItem favouriteItem = menu.findItem(R.id.action_favourites);
    			favouriteItem.setTitle("Favourites");
    			//fetches the NearbyComments
    	    	FetchNearbyComments fetch = new FetchNearbyComments();
    	    	fetch.execute(this);
    		}
    		
        	break;
    	case R.id.action_set_username:
    		DialogFragment newFragment = new SetUsernameFragment();
    	    newFragment.show(getFragmentManager(), "SetUsername");
    		break;
    	case R.id.action_sort:
    		
        }

      return true;
    } 
    
    @Override
    protected void onStart() {
    	super.onStart();
    	favouriteMode = false;
    	allComments = new ArrayList<Comment>();
    	adapter = new CommentListBaseAdapter(this, allComments);
    	commentList.setAdapter(adapter);
    	FetchNearbyComments fetch = new FetchNearbyComments();
    	fetch.execute(this);
    }
	
	/*
	 * These classes are duplicating lots of code. Later let's refactor, use state model
	 */
    private class FetchNearbyComments extends AsyncTask<Context, Integer, ArrayList<Comment>> {
    	@Override
    	protected ArrayList<Comment> doInBackground(Context... params) {
    		CommentRequest proximityRequest = new CommentRequest(200);
    		GeoLocation loc = new GeoLocation(getBaseContext());
    		//proximityRequest.setLocation(loc); //Doesn't work yet, omit for now
    		CommentController controller = new CommentController(proximityRequest, params[0]);
    		return controller.get(); // fetch all the comments off of the server
    	}

    	protected void onPostExecute(ArrayList<Comment> result) {
    		allComments.clear();
    		for (Comment topLevelComment : result)
    		{
    			if (topLevelComment.getParent() == null)
    			{
    				allComments.add(topLevelComment);
    			}
    		}
    		adapter.notifyDataSetChanged();
    	}
    }
    
    private class FetchFavoriteComments extends AsyncTask<Context, Integer, ArrayList<Comment>> {
    	
    	@Override
    	protected ArrayList<Comment> doInBackground(Context... params) {
    		CommentRequest proximityRequest = new CommentRequest(20);
    		proximityRequest.setParentId(null);
    		GeoLocation loc = new GeoLocation(getBaseContext());
    		CommentController controller = new CommentController(proximityRequest, params[0]);
			return controller.getFavouriteComments();
    	}    	
    	FetchNearbyComments fetch = new FetchNearbyComments();
    	protected void onPostExecute(ArrayList<Comment> result) {
    		allComments.clear();
    		allComments.addAll(result);
    		adapter.notifyDataSetChanged();
    	}
    	
    }
    
}