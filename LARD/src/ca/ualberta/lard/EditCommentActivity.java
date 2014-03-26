package ca.ualberta.lard;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.Picture;

/**
 * EditCommentActivity is called when a user chooses the option to edit a comment.
 * The activity calling EditCommentActivity is responsible for checking
 * that the user is allowed to edit the comment, and should display a message if the
 * user is not allowed.
 * 
 * <b> EditCommentActivity assumes that since it was called, the user has permission to
 * edit the comment. </b>
 * @param EXTRA_COMMENT_ID Expects the id of the comment to be edited as a String.
 * @author Victoria
 *
 */

public class EditCommentActivity extends Activity {
	private TextView editUsernameView;
	private TextView editBodyTextView;
	private ImageView editPictureView;
	private TextView editLatView;
	private TextView editLonView;
	private String commentId;
	private Comment comment;
	private String curUsername;
	private String curBodyText;
	private GeoLocation curLocation;
	private GeoLocation newLocation;
	private Picture picture;
	
	// For getting the id of comment to be edited
	public static final String EXTRA_COMMENT_ID = "COMMENT_ID";
	
	private static final int LOCATION_REQUEST_ID = 1;
	private static final int CAMERA_REQUEST_ID = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_edit_comment);
	    
        // Get id's of parts of view used to display parent comment
        editUsernameView = (TextView)findViewById(R.id.edit_username_textview);
        editBodyTextView = (TextView)findViewById(R.id.edit_bodytext_textview);
        editPictureView = (ImageView)findViewById(R.id.edit_pic_view);
        editLatView = (TextView)findViewById(R.id.edit_lat_textview);
        editLonView = (TextView)findViewById(R.id.edit_long_textview);
        
	    // Get the id of the comment from intent
	    Intent intent = getIntent();
	    commentId = intent.getStringExtra(EXTRA_COMMENT_ID);
	    if (commentId == null) {
        	Toast.makeText(getApplicationContext(), "Parent id was null.", Toast.LENGTH_SHORT).show();
        	finish();
	    }
	    
	    newLocation = null;

	    // Get the comment by passing the id to the controller
	    CommentRequest commentRequest = new CommentRequest(1);
	    commentRequest.setId(commentId);
	    
	    // Check a comment was received 
	    CommentController commentController = new CommentController(commentRequest, this);
    	if (commentController.any()) {
    		comment = commentController.getSingle();
    	}
    	else {
    		// Couldn't find the comment, should never get here though.
	    	finish();
    	}
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		// Set current author and body text
		curBodyText = comment.getBodyText();
		curUsername = comment.getAuthor();
		curLocation = comment.getLocation();
		
		editUsernameView.setText(curUsername);
		editBodyTextView.setText(curBodyText);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (newLocation != null) {
			editLatView.setText("Current Latitude: "+String.valueOf(newLocation.getLatitude()));
			editLonView.setText("Current Longitude: "+String.valueOf(newLocation.getLongitude()));
		}
		else {
			editLatView.setText("Current Latitude: "+String.valueOf(curLocation.getLatitude()));
			editLonView.setText("Current Longitude: "+String.valueOf(curLocation.getLongitude()));
		}
		
		// Should always check if there is a picture to display
		if (!picture.isNull()) {
			Bitmap bm = BitmapFactory.decodeByteArray(picture.getImageByte(), 0, picture.getImageByte().length);
			if (bm != null) {
				editPictureView.setImageBitmap(bm);
			}
			else {
				Toast.makeText(getApplicationContext(), "Picture Bitmap was null.", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	/**
	 * When the user is finished making changes, clicking save will update
	 * any changed parts of the comment and save it.
	 * @param v View
	 */
	public void onClickSaveChanges(View v) {
		
		// Set author
		String newUsername = editUsernameView.getText().toString();
		if (newUsername.isEmpty() || newUsername == null) {
			comment.setAuthor("Anonymous", getBaseContext());
		}
		// Only change author if it is different
		else if (newUsername.equals(curUsername) == false) {
			comment.setAuthor(newUsername, getBaseContext());
		}
		else { 
		}
		
		// Set body text
		String newBodyText = editBodyTextView.getText().toString();
		if (newBodyText.isEmpty() || newBodyText == null) {
			comment.setBodyText("[Comment Text Removed]");
		}
		// Only change text if it is different
		else if (newBodyText.equals(curBodyText) == false) {
			comment.setBodyText(newBodyText);
		}
		else { 
		}
		
		// Set GeoLocation only if it was changed.
		if (curLocation.equals(newLocation) == false) {
			comment.setLocation(newLocation);
		}
		
		// Set Picture only there is one.
		if (!picture.isNull()) {
			comment.setPicture(picture);
		}
		
		// TODO: activities should not directly access models. use CommentController (i dont currently know how to do this)
		// Check if the comment was saved locally
		if (comment.isLocal(getBaseContext())) {
			// TODO interact with the comment controller somehow here?
		}
		DataModel.save(comment);
		
		finish();
	}
	
	/**
	 * Unchanged from NewCommentActivity
	 * Called when the AttachButton Button is clicked
	 * <p>
	 * Launches an activity with the Intent.ACTION_GET_CONTENT action and sets its type to image, will return an image that was choosen
	 * @param v A View
	 */
	public void onClickAttachButton(View v) {		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAMERA_REQUEST_ID);
	}

	/**
	 * Unchanged from NewCommentActivity
	 * Called when the LocationButton Button is clicked
	 * <p>
	 * Launches the LocationSelectionActivity activity, which will return a GeoLocation that we chose for this comment
	 * @param v A View
	 */
	public void onClickLocationButton(View v) {
		Intent intent = new Intent(this, LocationSelectionActivity.class);
		startActivityForResult(intent, LOCATION_REQUEST_ID);
	}
	
	@Override
	/**
	 * Slightly changed from NewCommentActivity.
	 * Called when LocationSelectionActivity or picture is selected to be attached.
	 * Will get GeoLocation data from LocationSelectionActivity or get the
	 * picture data from Intent.ACTION_GET_CONTENT.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == LOCATION_REQUEST_ID) {
	        if (resultCode == RESULT_OK) {
	        	String locationData = data.getStringExtra(LocationSelectionActivity.LOCATION_REQUEST);
	        	newLocation = GeoLocation.fromJSON(locationData);
	        }
	    }
	    if (requestCode == CAMERA_REQUEST_ID) {
	        if (resultCode == RESULT_OK) {
	        	if(data != null)
	            {
	        		// gets the image out of the return intent and stores it as a a string in the picture model
	        		Uri uri = data.getData();
	        		
					try {
						InputStream is = getContentResolver().openInputStream(uri);
						Bitmap bitmap = BitmapFactory.decodeStream(is);
		                is.close();
		                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
		                ByteArrayOutputStream os = new ByteArrayOutputStream();
		                scaled.compress(Bitmap.CompressFormat.JPEG , 80, os);
		                picture.setImageByte(os.toByteArray());
		                os.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	            else
	            {
	            	// should never get here
	            	Toast.makeText(getApplicationContext(), "Failed to get picture (data was null).", Toast.LENGTH_SHORT).show();
	            }
	        }
	    }
	}
	
	
}
