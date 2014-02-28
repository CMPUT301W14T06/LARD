/**
 * When called, the id of the top level comment is received via intent.
 */

package ca.ualberta.lard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.ualberta.lard.model.Comment;

public class CommentActivity extends Activity {
	private String parentId;
	private ListView commentListView;
	private Comment parent;
	private ArrayList<Comment> commentList;
	private ArrayList<String> commentStrList;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_comment);
	    
	    // Configure the list view
	    commentListView = (ListView)findViewById(R.id.toplevel_and_children_list);
	    commentListView.setOnItemClickListener(new OnItemClickListener() {
	    	@Override
	    	// Opens another CommentActivity when a comment on the list is clicked
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		Intent intent = new Intent(getApplicationContext(),CommentActivity.class);
	    		// Get the id of the comment that was clicked
	    		String clickedCommentId = commentList.get(position).getId();
	    		intent.putExtra("extra_parent_id", clickedCommentId);
	    		startActivity(intent);		
	    	}	    	
		}); 
	    
	    // Get the id of the top level comment from intent
	    Intent intent = getIntent();
	    parentId = (String)intent.getStringExtra("extra_parent_id");
	    
	    // TODO: Get the comment by passing the id to the controller
	    // parent = ??
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
		// TODO: Create list of comments to display based off parentId.
		// commentList = parent.children();
	    // commentList.add(0, parent);
		
		// Create a list of strings for the adapter
		// TODO: Decide what part of comment to display. Currently only shows bodyText.
		commentStrList = new ArrayList<String>();
		for(Comment item: commentList) {
			commentStrList.add(item.getBodyText());
		}
		
		// Display parent comment and replies
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commentStrList);
		commentListView.setAdapter(adapter);
	}

	@Override
	protected void onPause() {
		// Nothing to be done onPause?
	}
	
	@Override
	// Display action bar
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_actionbar, menu); 
	    return super.onCreateOptionsMenu(menu);
	}
	
    /**
     *  Deals with on click for action bar
     *  Nothing is implemented yet here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_fav:
            // Favourite a comment
        	// TODO: Add comment to favourites
            return true;
        case R.id.action_reply:
            // Reply to a comment (open NewCommentActivity)
    		Intent intent = new Intent(getApplicationContext(),NewCommentActivity.class);
    		intent.putExtra("extra_parent_id", parentId);
    		startActivity(intent);
            return true;
        case R.id.action_save:
            // Save a comment for later
        	// TODO: Save
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}


