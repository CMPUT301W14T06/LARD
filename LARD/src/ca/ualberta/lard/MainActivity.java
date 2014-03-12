package ca.ualberta.lard;

import java.util.ArrayList;

import ca.ualberta.lard.R;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.Comment;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

public class MainActivity extends Activity {
// All instances of Comment should be replaced with Comment once Comment is constructible
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	allComments = new ArrayList<Comment>();
    	CommentController controller = new CommentController(this);
    	allComments = controller.get();
    	if (allComments == null) {
    		allComments = new ArrayList<Comment>();
    	}
    	adapter = new CommentListBaseAdapter(this, allComments);
    	commentList.setAdapter(adapter);
    }

    
    
}