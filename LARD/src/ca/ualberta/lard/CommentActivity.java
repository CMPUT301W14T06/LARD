package ca.ualberta.lard;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
import ca.ualberta.lard.model.DataModel;

/**
 * CommentActivity is called when a comment is selected.
 * It displays the comment at the top, followed by a list of its children. 
 * The selected comment can be replied to, added to favourites, or saved locally.
 * Selecting a one of the child comments will open another CommentActivity. 
 *
 * @param  EXTRA_PARENT_ID	Expects the id of the parent comment as a String
 * @author Victoria
 */

public class CommentActivity extends Activity {
	private String commentId;
	private ListView commentListView;
	private Comment comment;
	private ArrayList<Comment> commentList;
	private CommentListBaseAdapter adapter;
	private TextView parentAuthorView;
	private TextView parentCommentTextView;
	private TextView parentNumRepliesView;
	
	@SuppressWarnings("unused") // TODO: Remove
	private ImageView parentPicView;
	@SuppressWarnings("unused") // TODO: Remove
	private TextView parentLocationView;

	// For getting the id of clicked comment in MainActivity
	public static final String EXTRA_PARENT_ID = "PARENT_ID";
	
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
	    
	    // ----------------------------------------------------------------------------
	    
	    // TODO: Dylan changed this. I think that we are supposed to access the comments through CommentController
	    
	    // Check a comment was received 
	    CommentController commentController = new CommentController(commentRequest);
    	if (commentController.any()) {
    		comment = commentController.getSingle();
    	}
    	else {
    		// Couldn't find the comment, should never get here though.
	    	finish();
    	}
    	
    	/*
	    // Check a comment was received 
	    ArrayList<Comment> temp = DataModel.retrieveComments(commentRequest);
	    if (temp == null) {
	    	// Couldn't find the comment, should never get here though.
	    	finish();
	    }
	    comment = temp.get(0);
	    */
    	
    	// ----------------------------------------------------------------------------
	    
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
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
		
		// Get children of the comment
		if (comment.children() == null) {
			commentList = new ArrayList<Comment>();
		}
		else {
			commentList = comment.children();
		}
		
		// Set the parent comment info in the view
		// Make sure comment body isn't empty
		if (comment.getBodyText() == null || comment.getBodyText() == "") {
			parentCommentTextView.setText("[Comment Text Removed]");
		}
		else {
			parentCommentTextView.setText(comment.getBodyText());			
		}
		parentAuthorView.setText("By: "+comment.getAuthor());
		//TODO: parentLocationView.setText(comment.);
		parentNumRepliesView.setText(Integer.toString(comment.numReplies())+" replies");
		//TODO: parentPicView.setP
	
	    adapter = new CommentListBaseAdapter(this, commentList);
	    commentListView.setAdapter(adapter);
	} 
	
	@Override
	// Display action bar
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_actionbar, menu); 
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Deals with items in the action bar. When the heart icon is clicked the parent comment
	 * is saved locally and added to favourites. When the floppy disk icon is clicked the parent
	 * comment is saved locally. When the reply icon is clicked NewCommentActivity is opened
	 * and is passed the id of the parent comment.
	 * @param item A MenuItem
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_fav:
        	Toast.makeText(getApplicationContext(), "Comment Favourited.", Toast.LENGTH_SHORT).show();
        	DataModel.saveLocal(comment, true, this);
            return true;
        case R.id.action_save:
        	DataModel.saveLocal(comment, true, this);
        	Toast.makeText(getApplicationContext(), "Comment Saved.", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.action_reply:
    		Intent intent = new Intent(getApplicationContext(), NewCommentActivity.class);
    		intent.putExtra(NewCommentActivity.PARENT_ID, commentId);
    		startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}


