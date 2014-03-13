package ca.ualberta.lard;

import java.util.ArrayList;

import ca.ualberta.lard.R;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent i = new Intent(getBaseContext(), CommentActivity.class);
				Comment selection = allComments.get(position);
				i.putExtra("EXTRA_PARENT_ID", selection.getId());
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
      // action with ID action_refresh was selected
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
    	CommentController controller = new CommentController(this);
    	allComments = controller.get();
    	if (allComments == null) {
    		allComments = new ArrayList<Comment>();
    		// BaseAdapter can't fathom a null, so give it an empty list if there are no comments
    	}
    	adapter = new CommentListBaseAdapter(this, allComments);
    	commentList.setAdapter(adapter);
    }

    
    
}