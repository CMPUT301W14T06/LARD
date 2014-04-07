package ca.ualberta.lard;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.Follow;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.User;

/**
 * CommentActivity is called when a comment is selected.
 * It displays the comment at the top, followed by a list of its children. 
 * The selected comment can be replied to, added to favourites, or saved locally.
 * Selecting a one of the child comments will open another CommentActivity. 
 *
 * EXTRA_PARENT_ID	Expects the id of the parent comment as a String
 * @author Victoria
 */

public class CommentActivity extends Activity {
	private String commentId;
	private Comment comment;
	private static ArrayList<Comment> commentList;
	
	private CommentListBaseAdapter adapter;
	private CommentController commentController;
	
	private ListView commentListView;
	private TextView parentAuthorView;
	private TextView parentCommentTextView;
	private TextView parentNumRepliesView;
	private TextView parentLocationView;
	private ImageView parentPicView;

	// For getting the id of clicked comment in MainActivity
	public static final String EXTRA_PARENT_ID = "PARENT_ID";
	
	/**
	 * onCreate grabs all the ids of all the views used in CommentActivity. It uses the comment 
	 * id it is given from MainActivity to retrieve the comment. onCreate also attaches a click
	 * listener to the list view, which is supposed to display a list of children.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_comment);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);  
	    
        // Get ids of parts of view used to display parent comment
        parentAuthorView = (TextView)findViewById(R.id.parent_author);
        parentCommentTextView = (TextView)findViewById(R.id.parent_comment_body);
        parentNumRepliesView = (TextView)findViewById(R.id.parent_num_replies);
        parentPicView = (ImageView)findViewById(R.id.parent_picture);
        parentLocationView = (TextView)findViewById(R.id.parent_location);
        
	    // Get the id of the comment from intent
	    Intent intent = getIntent();
	    commentId = intent.getStringExtra(EXTRA_PARENT_ID);
	    if (commentId == null) {
        	Toast.makeText(getApplicationContext(), "Parent id was null.", Toast.LENGTH_SHORT).show();
        	finish();
	    }

	    // Get the comment by passing the id to the controller
	    CommentRequest commentRequest = new CommentRequest(1);
	    commentRequest.setId(commentId);
	    
	    // Check a comment was received 
	    commentController = new CommentController(commentRequest, this);
    	if (!commentController.isEmpty()) {
    		comment = commentController.getSingle();
    	} else {
    		Toast.makeText(getApplicationContext(), "Error loading the requested comment", Toast.LENGTH_SHORT).show();
    		System.err.println("Exiting Comment Activity with an Empty commentController.. Comment ID: " + commentId);
    		// Couldn't find the comment, should never get here though.
	    	finish();
    	}
    	
	    // Configure the list view
	    commentListView = (ListView)findViewById(R.id.children_list);
	    commentListView.setOnItemClickListener(new OnItemClickListener() {
	    	@Override
	    	// Opens another CommentActivity when a comment on the list is clicked
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		Intent intent = new Intent(getApplicationContext(),CommentActivity.class);
	    		// Get the id of the comment that was clicked
	    		String clickedCommentId = commentList.get(position).getId();
	    		intent.putExtra(CommentActivity.EXTRA_PARENT_ID, clickedCommentId);
	    		startActivity(intent);		
	    	}	    	
		});
	    
	    // Set up the adapter.
		commentList = new ArrayList<Comment>();
	    adapter = new CommentListBaseAdapter(this, commentList);
	    commentListView.setAdapter(adapter);
	    
	    // Display comment and children
	    displayCommentAndChildren(comment);  
		
	}
	
	/**
	 * This inflates the action bar and sets which icons are to be displayed.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_actionbar, menu); 
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Deals with items in the action bar. When the heart icon is clicked the parent comment
	 * and it's children is saved locally and added to favourites. When the floppy disk icon 
	 * is clicked the parent comment is saved locally. When the reply icon is clicked NewCommentActivity
	 * is opened and is passed the id of the parent comment.
	 * @param item A MenuItem
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_fav:
        	Toast.makeText(getApplicationContext(), "Comment Favourited.", Toast.LENGTH_SHORT).show();
        	// Save all the comments replies as well
        	commentController.favourite(comment);
            return true;
        case R.id.action_edit:        	
        	// Make a new user based on the current user
    		User curUser = new User(getSharedPreferences(User.PREFS_NAME, Context.MODE_PRIVATE));
    		
    		// Compute what the author with hash would be using the current users device id.
    		String authorWithCurUserId = curUser.hashWithGivenName(comment.getRawAuthor());
    		
    		// If author and computed author are the same, allow the user to edit the comment.
    		if (comment.getAuthor().equals(authorWithCurUserId)) {
        		Intent intent = new Intent(getApplicationContext(), NewEditCommentActivity.class);
        		intent.putExtra(NewEditCommentActivity.COMMENT_STRING, comment.toJson());
        		// Set flag to edit comment.
        		startActivity(intent);
    		} else {
        		Toast.makeText(getApplicationContext(), "You do not have permission to edit this comment.", Toast.LENGTH_SHORT).show();
        	} 
        	return true;
        case R.id.action_save:
        	if (commentController.paper(comment))
        	{
        		Toast.makeText(getApplicationContext(), "Comment Saved.", Toast.LENGTH_SHORT).show();
        	} else {
        		Toast.makeText(getApplicationContext(), "Error saving comment", Toast.LENGTH_SHORT).show();
        	}
            return true;
        case R.id.action_reply:
    		Intent intent = new Intent(getApplicationContext(), NewEditCommentActivity.class);
    		intent.putExtra(NewEditCommentActivity.PARENT_STRING, comment.toJson());
    		startActivity(intent);
            return true;
        case R.id.action_follow:
        	Follow follow = new Follow(getApplicationContext().getSharedPreferences(Follow.PREFS_NAME, Context.MODE_PRIVATE));
        	follow.addFollow(comment.getAuthor());
        	Toast.makeText(getApplicationContext(), "Followed user: " + comment.getAuthor(), Toast.LENGTH_SHORT).show();
        case R.id.action_refresh:
        	displayCommentAndChildren(comment);
        	Toast.makeText(getApplicationContext(), "Page Refreshed.", Toast.LENGTH_SHORT).show();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Displays a comments information and its children.
     * @param comment The Comment to be displayed
     */
    private void displayCommentAndChildren(Comment comment) {
		// Set the parent comment info in the view
		SetCommentInfo setInfo = new SetCommentInfo();
		setInfo.execute(comment);
		
		// Get the children if there are any
	    CommentRequest commentRequest = new CommentRequest(100);
	    commentRequest.setParentId(comment.getId());
		FetchChildren fetchChildren = new FetchChildren();
		fetchChildren.execute(commentRequest);
    }
    
    /**
     * Fetch all of the comments children.
     * Takes a comment request which has the parent id set.
     */
    private class FetchChildren extends AsyncTask<CommentRequest, Integer, ArrayList<Comment>> {
		ProgressDialog spinner = new ProgressDialog(CommentActivity.this);
    	
		/**
		 * Display a spinner while retrieving children
		 */
    	@Override
    	protected void onPreExecute() {
    		spinner.setMessage("Loading Replies...");
    		spinner.show();
    	}
    	
    	/**
    	 * Fetches the comments from the comment controller
    	 */
    	@Override
    	protected ArrayList<Comment> doInBackground(CommentRequest... params) {
    		CommentController commentController = new CommentController(params[0], getBaseContext());
			if (commentController.isEmpty() == false) {
				return commentController.get();
			}
			else {
				return new ArrayList<Comment>();
			}			
    	}
    	
    	/**
    	 *  Sets the number of childen and updates the list of children to be displayed, then
    	 *  gets rid of the loading spinner
    	 */
    	protected void onPostExecute(ArrayList<Comment> result) {
    		parentNumRepliesView.setText(Integer.toString(result.size()) + " replies");
    		
    		commentList.clear();
    		commentList.addAll(result);
    		adapter.notifyDataSetChanged();
    		
    		spinner.dismiss();
    	}
    }
    
    /**
     * Sets all of main comments info in text and image views.
     * Takes a comment which provides all the info to display.
     * @author Victoria
     *
     */
    private class SetCommentInfo extends AsyncTask<Comment, Void, Comment> {

		@Override
		protected Comment doInBackground(Comment... params) {
			return params[0];
		}
		
		protected void onPostExecute(Comment result) {
			// Set the picture, if there is one
			if (comment.hasPicture()) {
				parentPicView.setImageBitmap(comment.getPicture().getBitmap());
			}
			
			// Set the distance
			GeoLocation myCurLoc = new GeoLocation(getBaseContext());
			String distance = comment.getLocation().roundedDistanceFrom(myCurLoc);
			parentLocationView.setText(distance+"m away");
			
			// Set text and author
			parentCommentTextView.setText(comment.getBodyText());			
			parentAuthorView.setText("By: " + comment.getAuthor());			
		} 	
    }
}


