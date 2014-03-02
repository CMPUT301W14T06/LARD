package ca.ualberta.lard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class NewCommentActivity extends Activity {
	private int pid;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_comment);
		
		// get the parent id out of the intent
		// will be -1 if this is a top level comment
		Intent intent = getIntent();
	    pid = intent.getIntExtra("parentID", -1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_comment, menu);
		return true;
	}
	
	// Called when the SendButton Button is clicked
	// Will do nothing if commentEditText field is empty
	// TODO: Finish this function
	public void onClickSendButton(View v) {
		EditText commentText = (EditText) findViewById(R.id.commentEditText);
		if (commentText.getText().toString().isEmpty()) {
			return;
		}
		
		EditText usernameText = (EditText) findViewById(R.id.usernameEditText);
		if (!usernameText.getText().toString().isEmpty()) {
			name = null;
		}
		else {
			name = usernameText.getText().toString();
		}
		
		// needs comment controller to exist
		if (pid == -1) {
			// create a top level comment
			// CreateComment(commentText, name)
		}
		else {
			// create a reply comment
			// CreateComment(commentText, name, pid)
		}
		
		finish();
	}

	public int getPid() {
		return pid;
	}

}
