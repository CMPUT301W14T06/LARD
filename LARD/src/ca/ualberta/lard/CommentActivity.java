/**
 * When called pass the id of the comment through intent.
 */

package ca.ualberta.lard;

import ca.ualberta.lard.model.Comment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class CommentActivity extends Activity {
	//private int parentId;
	private ListView commentListView;
	private Comment parent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_comment);
	    
	    // Configure the listview, make it clickable
	    commentListView = (ListView)findViewById(R.id.toplevel_and_children_list);
	    
	    // Get the id of the top level comment from intent
	    Intent intent = getIntent();
	    //parentId = (int) intent.getIntExtra("extra_parent_id", 0);
	    parent = (Comment) intent.getSerializableExtra("extra_comment");
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
		// Create list of comments to display based off parentId.
		parent.getChildren();
	    // Based on id, get list of children
	    // Create a list with top level comment followed by all children
	    // Display this list	
	}

	@Override
	protected void onPause() {
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
            // Reply to a comment
        	// TODO: Open NewCommentActivity -> Pass it parent comment?
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


