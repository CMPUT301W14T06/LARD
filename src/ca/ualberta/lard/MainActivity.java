package ca.ualberta.lard;

import java.util.ArrayList;

import ca.ualberta.lard.R;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> allComments;
	private ListView commentList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        commentList = (ListView) findViewById(R.id.listView1);
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
		allComments = new ArrayList<String>();
		allComments.add("Look at this cat - 450m away - 7 replies");
		allComments.add("Does anyone else use this app? If so... - 900m away - 0 replies");
		allComments.add("Plz upvte thx. - 4.4km away - 9001 replies");
		adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, allComments);
		commentList.setAdapter(adapter);
	}

    
    
}
