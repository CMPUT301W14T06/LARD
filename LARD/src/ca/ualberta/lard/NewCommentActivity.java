package ca.ualberta.lard;

import ca.ualberta.lard.model.Picture;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewCommentActivity extends Activity {
	private int pid;
	private Picture picture;
	// private location?

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get the parent id out of the intent
		// will be -1 if this is a top level comment
		Intent intent = getIntent();
	    pid = intent.getIntExtra("parentID", -1);
	    
	    if (pid != -1) {
	    	TextView lardTextView = (TextView) findViewById(R.id.lardTextView); // I am assuming that this is where the "Reply to:" goes
	    	// TODO: Fix this
	    	lardTextView.setText("Reply to: " + pid);
	    	//lardTextView.setText("Reply to: " + CommentController.getCommentName(pid));
	    }
	    
		setContentView(R.layout.activity_new_comment);
	}

	// Not sure if we even need this function
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
		if (usernameText.getText().toString().isEmpty()) {
			//String name = null;
		}
		else {
			//String name = usernameText.getText().toString();
		}
		
		// needs comment controller to exist
		if (pid == -1) {
			// create a top level comment
			// CreateComment(commentText, name, picture)
		}
		else {
			// create a reply comment
			// CreateComment(commentText, name, picture, pid)
		}
		
		finish();
	}
	
	// Called when the AttachButton Button is clicked
	// TODO: Finish this function
	public void onClickAttachButton(View v) {
		// needs picture model to exist
		// picture = new Picture();
	}
	
	// Called when the LocationButton Button is clicked
	// Not sure what this should actually do, will ask then complete
	// TODO: Finish this function
	public void onClickLocationButton(View v) {
		Intent intent = new Intent(this, LocationSelectionActivity.class);
		startActivity(intent);
	}

	public int getPid() {
		return pid;
	}
	
	public Picture getPicture() {
		return picture;
	}

}
