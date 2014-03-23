package ca.ualberta.lard;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ca.ualberta.lard.controller.CommentController;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.Picture;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * NewCommentActivity is called when a CreateComment button is pushed (Either from MainActivity or CommentActivity).
 * This activity is used to create a new comment (Either top level or reply).
 * The new comment can be given a picture or a location (from LocationSelectionActivity).
 * A valid new comment must have some body text. Everything else is optional.
 *
 * @param  PARENT_ID	Expects the id of the parent comment as a String
 * @author Dylan
 */

public class NewCommentActivity extends Activity {
	private String pid;
	private Picture picture;
	private GeoLocation location;
	private Comment comment;
	private TextView lardTextView;
	
	// These probably dont need to be public, may change in future
	public static final int LOCATION_REQUEST_ID = 1;
	public static final int CAMERA_REQUEST_ID = 2;
	
	// For getting the id of the parent comment of this new comment
	public static final String PARENT_ID = "PID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_comment);
		
		location = new GeoLocation(getApplicationContext()); // TODO: look at users settings first
		picture = new Picture();
		
		// get the parent id out of the intent
		// will be null if this is a top level comment
		Intent intent = getIntent();
	    pid = intent.getStringExtra(PARENT_ID);
	    
	    if (pid != null) {
	    	lardTextView = (TextView) findViewById(R.id.lardTextView);

	    	// request the comment that has an id equal to the current pid
	    	CommentRequest req = new CommentRequest(1);
	    	req.setParentId(pid);
	    	GetParent getParent = new GetParent();
	    	getParent.execute(req);
	    }
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		TextView locationLatTextView = (TextView) findViewById(R.id.currentLatLocation);
		TextView locationLongTextView = (TextView) findViewById(R.id.currentLongLocation);
		locationLatTextView.setText("Latitude: " + location.getLatitude());
		locationLongTextView.setText("Longitude: " + location.getLongitude());
		
		if (!picture.isNull()) {
			ImageView pictureImageView = (ImageView) findViewById(R.id.currentPicture);
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
		EditText commentText = (EditText) findViewById(R.id.commentEditText);
		// There must be text in the commentEditText field for the comment to be valid
		if (commentText.getText().toString().isEmpty()) {
			return;
		}

		// Create the comment either with a pid or without
		if (pid == null) {
			comment = new Comment(commentText.getText().toString(), this);
		}
		else {
			comment = new Comment(commentText.getText().toString(), pid, this);
		}
		
		// Set an author for the comment if you can
		EditText usernameText = (EditText) findViewById(R.id.usernameEditText);
		if (!usernameText.getText().toString().isEmpty()) {
			comment.setAuthor(usernameText.getText().toString(), this);
		}
		
		// Set a location for the comment
		comment.setLocation(location);
		
		// Set a picture for the comment if you can
		if (!picture.isNull()) {
			comment.setPicture(picture);
		}
		
		// Send the completed comment to the CommentController
		MakeComment makeComment = new MakeComment();
		makeComment.execute(comment);
		
		finish();
	}
	
	/**
	 * Called when the AttachButton Button is clicked
	 * <p>
	 * Launches an activity with the Intent.ACTION_GET_CONTENT action and sets its type to image, will return an image that was choosen
	 * @param v A View
	 */
	public void onClickAttachButton(View v) {
		// TODO: REMOVE
		// for taking pics
        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
        startActivityForResult(intent, CAMERA_REQUEST_ID);*/
		
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
	        	if(data != null)
	            {
	        		// gets the image out of the return intent and stores it as a a string in the picture model
	        		Uri uri = data.getData();
	        		
					try {
						// TODO: REMOVE
						/*String file = getPath(this, uri);
		        		Bitmap bitmap = BitmapFactory.decodeFile(file);*/
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

	// TODO: REMOVE
	// Remeber to de-credit paulburke on wiki
	/**
	 * Get a file path from a Content Uri.
	 * <p>
	 * MAJOR EDITS WERE MADE TO THIS FUNCTION. 
	 * ORIGINAL AUTHOR was paulburke. 
	 * ORIGINAL FUNCTION NAME WAS getDataColumn. 
	 * <p>
	 * Source: https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	public static String getPath(final Context context, Uri uri) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, null, null, null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}

	/**
	 * Returns the Parent ID of the comment as a string
	 */
	public String getPid() {
		return pid;
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
			CommentController commentController = new CommentController(params[0]);
			if (commentController.any()) {
				Comment comment = commentController.getSingle();
				return comment.getAuthor().toString();
			}
			else {
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
