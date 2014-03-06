package ca.ualberta.lard;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewCommentActivity extends Activity {
	public final static int LOCATION_REQUEST_ID = 1;
	public final static int CAMERA_REQUEST_ID = 2;
	private String pid;
	private Picture picture;
	private GeoLocation location;
	private Comment comment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get the parent id out of the intent
		// will be null if this is a top level comment
		Intent intent = getIntent();
	    pid = intent.getStringExtra("parentID");
	    
	    if (pid != null) {
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

		if (pid == null) {
			comment = new Comment(commentText.getText().toString(), this);
		}
		else {
			comment = new Comment(commentText.getText().toString(), pid, this);
		}
		
		EditText usernameText = (EditText) findViewById(R.id.usernameEditText);
		if (!usernameText.getText().toString().isEmpty()) {
			comment.setAuthor(usernameText.getText().toString(), this);
		}
		
		if (location != null) {
			comment.setLocation(location);
		}
		
		if (picture != null) {
			comment.setPicture(picture);
		}
		
		// TODO:
		// provide the comment to the comment controller
		
		finish();
	}
	
	// Called when the AttachButton Button is clicked
	// TODO: Finish this function
	public void onClickAttachButton(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
        startActivityForResult(intent, CAMERA_REQUEST_ID); 
	}
	
	// Called when the LocationButton Button is clicked
	// Launches the LocationSelectionActivity activity, which will return a GeoLocation that we chose for this comment
	public void onClickLocationButton(View v) {
		Intent intent = new Intent(this, LocationSelectionActivity.class);
		startActivityForResult(intent, LOCATION_REQUEST_ID);
	}
	
	// Called when LocationSelectionActivity or Intent.ACTION_GET_CONTENT returns
	// Will either get the Geolocation data from LocationSelectionActivity
	// or get the picture data from Intent.ACTION_GET_CONTENT
	// got: http://stackoverflow.com/questions/7832773/android-how-to-get-the-image-using-intent-data
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == LOCATION_REQUEST_ID) {
	        if (resultCode == RESULT_OK) {
	        	String locationData = data.getStringExtra(LocationSelectionActivity.LOCATION_REQUEST);
	        	location = GeoLocation.fromJSON(locationData);
	        }
	    }
	    if (requestCode == CAMERA_REQUEST_ID) {
	        if (resultCode == RESULT_OK) {
	        	if(data != null)
	            {           
	                Uri uri = data.getData();
	                String file = uri.getPath();
	                Bitmap thumbnail = BitmapFactory.decodeFile(file);
	                ByteArrayOutputStream os = new ByteArrayOutputStream();
	                thumbnail.compress(Bitmap.CompressFormat.JPEG , 80, os);
	                picture.setImageString(Picture.encode(os));
	            }
	            else
	            {
	                System.out.println("SDCard have no images");  
	            }
	        }
	    }
	}

	public String getPid() {
		return pid;
	}
	
	public Picture getPicture() {
		return picture;
	}
	
	public GeoLocation getGeoLocation() {
		return location;
	}

}
