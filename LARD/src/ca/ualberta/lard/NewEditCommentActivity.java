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
 * NewEditCommentActivity is called when a CreateComment button is pushed (Either
 * from MainActivity or CommentActivity), or when an EditComment button is pushed. 
 * This activity is used to create a new comment (Either top level or reply) or to 
 * edit an existing comment. The comment can be given a picture
 * or a location (from LocationSelectionActivity). A valid new comment must have
 * some body text. Everything else is optional.
 * 
 * @author Dylan
 */

public class NewEditCommentActivity extends Activity {
	private Picture picture;
	private GeoLocation location;
	private Comment comment;
	private Comment parent;
	private int mode;

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
	public static final String PARENT_STRING = "PID";
	public static final String COMMENT_STRING = "CSTR";
	
	// Our modes for this activity
	private static final int EDIT_MODE = 1;
	private static final int NEW_MODE = 2;

	/**
	 * Instantiate all of the views and displays the comments current information. 
	 * Information including information on the comments parent, bodytext and if 
	 * the comment contains a picture
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_comment);

		// Instantiate our views
		lardTextView = (TextView) findViewById(R.id.lardTextView);
		userNameEditTextView = (TextView) findViewById(R.id.usernameEditText);
		bodyTextEditTextView = (TextView) findViewById(R.id.commentEditText);
		locationLatTextView = (TextView) findViewById(R.id.currentLatLocation);
		locationLongTextView = (TextView) findViewById(R.id.currentLongLocation);
		pictureImageView = (ImageView) findViewById(R.id.currentPicture);

		Intent intent = getIntent();
		
		// We either deserialize the comment passed along, or we create a new empty one
		if (intent.hasExtra(COMMENT_STRING)) {
			this.mode = EDIT_MODE;
			this.comment = Comment.fromJson(intent.getStringExtra(COMMENT_STRING));
			this.comment.setContext(getApplicationContext());
		} else {
			this.mode = NEW_MODE;
			this.comment = new Comment("", getApplicationContext());
		}
		
		if (!intent.hasExtra(PARENT_STRING)) {
			lardTextView.setVisibility(View.GONE);
		} else {
			if (intent.getStringExtra(PARENT_STRING) != null) {
				// Initialize the parent
				this.parent = Comment.fromJson(intent.getStringExtra(PARENT_STRING));
				this.parent.setContext(getApplicationContext());
				lardTextView.setText(parent.getAuthor().toString());
				// Since we have a parent, set it to the comment's parent.
				this.comment.setParent(this.parent.getId());
			}
			else {
				lardTextView.setVisibility(View.GONE);
			}
		}

		// Display the comments current information
		userNameEditTextView.setText(comment.getRawAuthor());
		bodyTextEditTextView.setText(comment.getBodyText());
		this.location = comment.getLocation();
		
		picture = comment.getPicture();
		if (picture == null) {
			picture = new Picture();
		}
	}

	/**
	 * Gets the phones current location and will decode pictures from the
	 * picture's byte array
	 */
	@Override
	protected void onResume() {
		super.onResume();

		locationLatTextView.setText("Latitude: " + location.getLatitude());
		locationLongTextView.setText("Longitude: " + location.getLongitude());

		// TODO: Change
		// !picture.isNull()
		if (picture != null && !picture.isNull()) {
			Bitmap bm = BitmapFactory.decodeByteArray(picture.getImageByte(),
					0, picture.getImageByte().length);
			if (bm != null) {
				pictureImageView.setImageBitmap(bm);
			} else {
				Toast.makeText(getApplicationContext(),
						"Picture Bitmap was null.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * Called when the SendButton Button is clicked
	 * <p>
	 * Creates the comment and sends it to CommentController. Will do nothing if
	 * commentEditText field is empty
	 * 
	 * @param v
	 *            A View
	 */
	public void onClickSendButton(View v) {
		// There must be text in the bodyTextEditTextView field for the
		// comment to be valid
		if (bodyTextEditTextView.getText().toString().isEmpty()) {
			if (this.mode == NEW_MODE) {
				Toast.makeText(getApplicationContext(),
					"Missing comment text.", Toast.LENGTH_SHORT).show();
				return;
			} else {
				comment.setBodyText("[Comment Text Removed]");
			}
		}
		else {
			comment.setBodyText(bodyTextEditTextView.getText().toString());
		}
		
		// Username may not contain "#" character
		if (userNameEditTextView.getText().toString().contains("#")) {
			Toast.makeText(getApplicationContext(),
					"Username may not contain \"#\" character",
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			// Set an author for the comment if you can
			if (!userNameEditTextView.getText().toString().isEmpty()) {
				this.comment.setAuthor(userNameEditTextView.getText().toString());
			} else {
				this.comment.setAuthor(null);
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return;
		}
		// Set a location for the comment
		comment.setLocation(location);
		
		// Set a picture for the comment
		// TODO: drop the if condition
		if (picture != null) {
			comment.setPicture(picture);
		}
		
		// Send the completed comment to the CommentController
		UpdateOrSaveComment finalize = new UpdateOrSaveComment();
		finalize.execute(comment);

		finish();
	}

	/**
	 * Called when the AttachButton Button is clicked
	 * <p>
	 * Launches an activity with the Intent.ACTION_GET_CONTENT action and sets
	 * its type to image, will return an image that was choosen
	 * 
	 * @param v
	 *            A View
	 */
	public void onClickAttachButton(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				CAMERA_REQUEST_ID);
	}

	/**
	 * Called when the LocationButton Button is clicked
	 * <p>
	 * Launches the LocationSelectionActivity activity, which will return a
	 * GeoLocation that we chose for this comment
	 * 
	 * @param v
	 *            A View
	 */
	public void onClickLocationButton(View v) {
		Intent intent = new Intent(this, LocationSelectionActivity.class);
		startActivityForResult(intent, LOCATION_REQUEST_ID);
	}

	/**
	 * Called when LocationSelectionActivity or Intent.ACTION_GET_CONTENT returns
	 * and Will either get the GeoLocation data from LocationSelectionActivity
	 * or get the picture data from Intent.ACTION_GET_CONTENT
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOCATION_REQUEST_ID) {
			if (resultCode == RESULT_OK) {
				String locationData = data
						.getStringExtra(LocationSelectionActivity.LOCATION_REQUEST);
				location = GeoLocation.fromJSON(locationData);
			}
		}
		if (requestCode == CAMERA_REQUEST_ID) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					// gets the image out of the return intent and stores it as
					// a a string in the picture model
					Uri uri = data.getData();

					try {
						InputStream is = getContentResolver().openInputStream(
								uri);
						Bitmap bitmap = BitmapFactory.decodeStream(is);
						is.close();
						Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 200,
								200, false);
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						scaled.compress(Bitmap.CompressFormat.JPEG, 80, os);
						picture.setImageByte(os.toByteArray());
						os.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					// should never get here
					Toast.makeText(getApplicationContext(),
							"Failed to get picture (data was null).",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**
	 * Will Save the comment if the comment is a new comment or in case of editing
	 * a comment, it will update that comment. 
	 */
	private class UpdateOrSaveComment extends AsyncTask<Comment, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Comment... params) {
			boolean result = false;
			if (mode == EDIT_MODE) {
				result = CommentController.update(comment);
			} else {
				result = CommentController.createComment(comment);
			}
			return result;
		}

		protected void onPostExecute(Boolean result) {
			System.out.println("Result of NewCommentActivity onPostExecute");
			System.out.println(result);
		}
	}

}
