/**
 * This activity is called when a comment is selected.
 * It displays the comment at the top, followed by a list of its children. 
 * The selected comment can be replied to, added to favourites, or saved locally.
 * Selecting a one of the child comments will open another CommentActivity. 
 *
 * @param  EXTRA_PARENT_ID	Expects the id of the parent comment as a String
 */

package ca.ualberta.lard;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;

public class CommentActivity extends Activity {
	private String commentId;
	private ListView commentListView;
	private Comment comment;
	private ArrayList<Comment> commentList;
	private CommentListBaseAdapter adapter;
	
	// For debugging purposes
	private static final String TAG = "Comment Activity";
	
	// For getting the parent id from the extra
	public static final String EXTRA_PARENT_ID = "TEXT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_comment);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);  
	    
	    // Get the id of the top level comment from intent
	    Intent intent = getIntent();
	    commentId = (String)intent.getStringExtra(EXTRA_PARENT_ID);

	    // Get the comment by passing the id to the controller
	    CommentRequest commentRequest = new CommentRequest(1);
	    commentRequest.setId(commentId);
	    // Check a comment was received 
	    ArrayList<Comment> temp = DataModel.retrieveComments(commentRequest);
	    if (temp == null) {
	    	// Couldn't find the comment, should never get here though.
	    	finish();
	    }
	    comment = temp.get(0);
	    
	    // Configure the list view
	    commentListView = (ListView)findViewById(R.id.toplevel_and_children_list);
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
		
		// Add the parent to the front of the list
	    commentList.add(0, comment);
	
	    adapter = new CommentListBaseAdapter(this, commentList);
	    commentListView.setAdapter(adapter);
	} 
	
	@Override
	// Display action bar
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_actionbar, menu); 
	    return super.onCreateOptionsMenu(menu);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_fav:
        	// TODO: Add comment to favourites
        	Toast.makeText(getApplicationContext(), "Comment Favourited.", Toast.LENGTH_SHORT).show();
        	DataModel.saveLocal(comment, true, this);
            return true;
        case R.id.action_reply:
    		Intent intent = new Intent(getApplicationContext(), NewCommentActivity.class);
    		intent.putExtra(NewCommentActivity.PARENT_ID, commentId);
    		startActivity(intent);
            return true;
        case R.id.action_save:
        	DataModel.saveLocal(comment, true, this);
        	Toast.makeText(getApplicationContext(), "Comment Saved.", Toast.LENGTH_SHORT).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}


