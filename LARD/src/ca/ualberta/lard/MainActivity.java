package ca.ualberta.lard;

import java.util.ArrayList;

import ca.ualberta.lard.R;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.BarrenComment;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

public class MainActivity extends Activity {

private CommentListBaseAdapter adapter;
private ArrayList<BarrenComment> allComments;
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
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
protected void onStart() {
// TODO Auto-generated method stub
super.onStart();
allComments = new ArrayList<BarrenComment>();
allComments.add(new BarrenComment("Test comment.", "Anonymous", false, 60, 9));
allComments.add(new BarrenComment("Another comment!", "Sir Maynard", true, 1200, 0));
allComments.add(new BarrenComment("Dogz suk catz4lyfe", "catlover66", false, 10, 9001));
adapter = new CommentListBaseAdapter(this,
 allComments);
commentList.setAdapter(adapter);
}

    
    
}