package ca.ualberta.lard;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
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
 * NewCommentActivity is called when a CreateComment button is pushed (Either from MainActivity or CommentActivity).
 * This activity is used to create a new comment (Either top level or reply).
 * The new comment can be given a picture or a location (from LocationSelectionActivity).
 * A valid new comment must have some body text. Everything else is optional.
 * <p>
 * PARENT_ID:	Expects the id of the parent comment as a String
 * @author Dylan
 */

public class NewCommentActivity extends Activity {
	private String id;
	private Picture picture;
	private GeoLocation location;
	private Comment comment;
	private String caller;
	public Comment editComment;
	private CommentController commentController;
	
	private TextView lardTextView;
	private TextView userNameEditTextView;
	private TextView bodyTextEditTextView;
	private TextView locationLatTextView;
	private TextView locationLongTextView;
	private ImageView pictureImageView;
	
	// These probably dont need to be public, may change in future
	public static final int LOCATION_REQUEST_ID = 1;
	public static final int CAMERA_REQUEST_ID = 2;
	
	// For getting the id of the parent comment of this new comment
	public static final String PARENT_ID = "PID";
	// Specify if the comment is a reply or a new comment;
	// Set flag to NEW or EDIT.
	public static final String FLAG = "FLAG";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_comment);
		
		lardTextView = (TextView) findViewById(R.id.lardTextView);
		userNameEditTextView = (TextView) findViewById(R.id.usernameEditText);
		bodyTextEditTextView = (TextView) findViewById(R.id.commentEditText);
		locationLatTextView = (TextView) findViewById(R.id.currentLatLocation);
		locationLongTextView = (TextView) findViewById(R.id.currentLongLocation);
		pictureImageView = (ImageView) findViewById(R.id.currentPicture);
		
		// get the parent id out of the intent
		// will be null if this is a top level comment
		Intent intent = getIntent();
	    id = intent.getStringExtra(PARENT_ID);
	    caller = intent.getStringExtra(FLAG);
	    
	    if (caller.equals("NEW")) {
	    	location = new GeoLocation(getApplicationContext()); // TODO: look at users settings first
			picture = new Picture();
	    }
	    
	    if (id != null) {
	    	CommentRequest req = new CommentRequest(1);
	    	req.setId(id);
	    	if (caller.equals("NEW")) {
	    		GetParent getParent = new GetParent();
		    	getParent.execute(req);
		    }
	    	else if (caller.equals("EDIT")) {
	    		GetComment getComment = new GetComment();
		    	getComment.execute(req);
		    	try {
					editComment = getComment.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}    	
		    	// Display the comments current information
				userNameEditTextView.setText(editComment.getAuthor());
				bodyTextEditTextView.setText(editComment.getBodyText());
				location = new GeoLocation(editComment.getLocation().getLatitude(), editComment.getLocation().getLongitude());
				// TODO: Remove
				// location.setLatitude(editComment.getLocation().getLatitude());
				// location.setLongitude(editComment.getLocation().getLongitude());
				picture = editComment.getPicture();
		    }
	    	else {
	    		// If we get here the flag was sent incorrectly
	    		Toast.makeText(getApplicationContext(), "Flag was incorrectly set", Toast.LENGTH_SHORT).show();
		    	finish();
	    	}
	    }
	    else if (caller.equals("NEW")) {
	    	// is this a valid line?
	    	;
	    }
	    else {
	    	// If we get here the flag was sent incorrectly or id was null and flag was EDIT
	    	Toast.makeText(getApplicationContext(), "Flag was incorrectly set or id was null with EDIT flag", Toast.LENGTH_SHORT).show();
	    	finish();
	    }
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		locationLatTextView.setText("Latitude: " + location.getLatitude());
		locationLongTextView.setText("Longitude: " + location.getLongitude());
		
		// TODO: Change
		// !picture.isNull()
		if (picture != null && !picture.isNull()) {
			Bitmap bm = BitmapFactory.decodeByteArray(picture.getImageByte(), 0, picture.getImageByte().length);
			if (bm != null) {
				pictureImageView.setImageBitmap(bm);
			}
			else {
				Toast.makeText(getApplicationContext(), "Picture Bitmap was null.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	// TODO: Not sure if we even need this function
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_comment, menu);
		return true;
	}
	
	/**
	 * Called when the SendButton Button is clicked
	 * <p>
	 * Creates the comment and sends it to CommentController. Will do nothing if commentEditText field is empty
	 * @param v A View
	 */
	public void onClickSendButton(View v) {
		if (caller.equals("NEW")) {
			// There must be text in the bodyTextEditTextView field for the comment to be valid
			if (bodyTextEditTextView.getText().toString().isEmpty()) {
				Toast.makeText(getApplicationContext(), "Missing comment text.", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// Username may not contain "#" character
			if (userNameEditTextView.getText().toString().contains("#")) {
				Toast.makeText(getApplicationContext(), "Username may not contain \"#\" character", Toast.LENGTH_SHORT).show();
				return;
			}

			// Create the comment either with a pid or without
			if (id == null) {
				comment = new Comment(bodyTextEditTextView.getText().toString(), this);
			}
			else {
				comment = new Comment(bodyTextEditTextView.getText().toString(), id, this);
			}

			// Set an author for the comment if you can
			if (!userNameEditTextView.getText().toString().isEmpty()) {
				comment.setAuthor(userNameEditTextView.getText().toString());
			}

			// Set a location for the comment
			comment.setLocation(location);

			// Set a picture for the comment
			// TODO: drop the if condition
			if (picture != null) {
				comment.setPicture(picture);
			}

			// Send the completed comment to the CommentController
			MakeComment makeComment = new MakeComment();
			makeComment.execute(comment);
		}
		else {
			// Username may not contain "#" character
			if (userNameEditTextView.getText().toString().contains("#")) {
				Toast.makeText(getApplicationContext(), "Username may not contain \"#\" character", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// Set author
			// if author text is not empty
			if (!userNameEditTextView.getText().toString().isEmpty()) {
				editComment.setAuthor(userNameEditTextView.getText().toString());
			}
			// if author text is empty
			else {
				editComment.setAuthor(null);
			}
			
			// Set body text
			// if body text is empty
			if (bodyTextEditTextView.getText().toString().isEmpty()) {
				editComment.setBodyText("[Comment Text Removed]");
			}
			// if body text is not empty
			else {
				editComment.setBodyText(bodyTextEditTextView.getText().toString());
			}
			
			// Set GeoLocation
			editComment.setLocation(location);
			
			// Set Picture
			// TODO: drop the if condition
			if (picture != null) {
				editComment.setPicture(picture);
			}
			
			// Perform the actual save
			if (!CommentController.update(editComment)) {
				Toast.makeText(getApplicationContext(), "Error saving your comment changes", Toast.LENGTH_SHORT).show();
			}
		}
		
		finish();
	}
	
	/**
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
	 * Called when the LocationButton Button is clicked
	 * <p>
	 * Launches the LocationSelectionActivity activity, which will return a GeoLocation that we chose for this comment
	 * @param v A View
	 */
	public void onClickLocationButton(View v) {
		Intent intent = new Intent(this, LocationSelectionActivity.class);
		startActivityForResult(intent, LOCATION_REQUEST_ID);
	}
	
	// Called when LocationSelectionActivity or Intent.ACTION_GET_CONTENT returns
	// Will either get the GeoLocation data from LocationSelectionActivity
	// or get the picture data from Intent.ACTION_GET_CONTENT
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
	        	if(data != null) {
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
	        	else {
	            	// should never get here
	            	Toast.makeText(getApplicationContext(), "Failed to get picture (data was null).", Toast.LENGTH_SHORT).show();
	            }
	        }
	    }
	}

	/**
	 * Returns the Parent ID of the comment as a string
	 */
	public String getPid() {
		return id;
	}
	
	/**
	 * Returns the Picture of the comment
	 */
	public Picture getPicture() {
		return picture;
	}
	
	/**
	 * Returns the GeoLocation of the comment
	 */
	public GeoLocation getGeoLocation() {
		return location;
	}

	private class GetParent extends AsyncTask<CommentRequest, Integer, String> {
		@Override
		protected String doInBackground(CommentRequest... params) {
			commentController = new CommentController(params[0], getBaseContext());
			if (commentController.any()) {
				Comment comment = commentController.getSingle();
				return comment.getAuthor().toString();
			}
			else {
				// TODO: REMOVE
				// should this return?
				// Toast.makeText(getApplicationContext(), "CommentController did not return a comment with that parent id.", Toast.LENGTH_SHORT).show();
				//finish();
				return "Error! Parent comment not found!";
			}
		}

		protected void onPostExecute(String result) {
			lardTextView.setText("Reply to: " + result);
		}
    }
	
	private class GetComment extends AsyncTask<CommentRequest, Integer, Comment> {
		@Override
		protected Comment doInBackground(CommentRequest... params) {
			
			commentController = new CommentController(params[0], getBaseContext());
			if (commentController.isEmpty() == false) {
				return commentController.getSingle();
			}
			else {
				return null;
			}
		}
		
		protected void onPostExecute(Comment result) {
			editComment = result;
			// TODO: We probably shouldn't be returning the comment because it causes large delays.
			// Somehow need to refactor the code so we can set all the views after the comment is found.
			/*
			userNameEditTextView.setText(editComment.getAuthor());
			bodyTextEditTextView.setText(editComment.getBodyText());
			locationLatTextView.setText("Latitude: " + editComment.getLocation().getLatitude());
			locationLongTextView.setText("Longitude: " + editComment.getLocation().getLongitude()); */
		}
    }
	
	private class MakeComment extends AsyncTask<Comment, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Comment... params) {
			CommentController.createComment(comment);
			return true;
		}

		protected void onPostExecute(Boolean result) {
			System.out.println(result);
		}
    }
	
}
