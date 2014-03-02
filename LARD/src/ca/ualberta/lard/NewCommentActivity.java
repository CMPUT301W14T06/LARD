package ca.ualberta.lard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class NewCommentActivity extends Activity {
	int pid[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_comment);
		
		// get the parent id out of the intent
		// will be null if this is a top level comment
		Intent intent = getIntent();
	    pid = intent.getIntArrayExtra("parentID");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_comment, menu);
		return true;
	}

}
