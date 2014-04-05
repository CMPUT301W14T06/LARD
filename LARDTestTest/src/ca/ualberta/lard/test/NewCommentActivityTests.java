package ca.ualberta.lard.test;

import ca.ualberta.lard.NewEditCommentActivity;
import ca.ualberta.lard.model.GeoLocation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

/**
 * JUnit tests for NewCommentActivity. Tests that the correct parent id is received. Also
 * tests user interaction with the UI (attaching a picture, changing location, inputting
 * text for the comment).
 */

public class NewCommentActivityTests extends ActivityInstrumentationTestCase2<NewEditCommentActivity> {
	
	public NewCommentActivityTests() {
		super(NewEditCommentActivity.class);
	}
	
	/*public void testParentIDExists() {
		Intent intent = new Intent();
		String value = "Test";
		intent.putExtra(NewCommentActivity.PARENT_STRING, value);
		setActivityIntent(intent);
		NewCommentActivity activity = getActivity();
		assertEquals("NewCommentActivity should get the value from intent", value, activity, activity.getPid());
	}
	
	public void testParentIDNotExists() {
		Intent intent = new Intent();
		setActivityIntent(intent);
		NewCommentActivity activity = getActivity();
		assertEquals("NewCommentActivity should get the value from intent", null, activity.getPid());
	}
	
	public void testPictureDefault() {
		Intent intent = new Intent();
		setActivityIntent(intent);
		NewCommentActivity activity = getActivity();
		assertTrue("NewCommentActivity should by default have no picture", activity.getPicture().isNull());
	}
	
	public void testGeoLocationDefault() {
		Intent intent = new Intent();
		setActivityIntent(intent);
		NewCommentActivity activity = getActivity();
		GeoLocation loc = new GeoLocation(activity);
		assertEquals("NewCommentActivity should by default have a default location", loc.getLatitude(), activity.getGeoLocation().getLatitude());
		assertEquals("NewCommentActivity should by default have a default location", loc.getLongitude(), activity.getGeoLocation().getLongitude());
	}*/

}

/*package ca.ualberta.lard.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.lard.EditCommentActivity;
import ca.ualberta.lard.model.Comment;

public class EditCommentActivityTests extends ActivityInstrumentationTestCase2<EditCommentActivity> {
	private Comment comment;
	private String id;
	private EditCommentActivity activity;
	private Intent intent;
	
	public EditCommentActivityTests() {
		super(EditCommentActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
		comment = new Comment("This is a comment.", context);
		id = comment.getId();
		
		// Pass the activity the id
		intent = new Intent();
		intent.putExtra(EditCommentActivity.EXTRA_COMMENT_ID, id);
		
		setActivityIntent(intent);
		activity = getActivity();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		comment = null;
		id = null;
		intent = null;
		activity = null;
	}
	
	*//**
	 * Tests that when the activity is started, a comment id is received.
	 *//*
	public void testReceiveId() {
		String passedId = (String)activity.getIntent().getStringExtra(EditCommentActivity.EXTRA_COMMENT_ID);
		assertEquals("Should receive an id from the intent.", id, passedId);
	}
	
	*//**
	 * Tests that when the user changes the comment author, the comment author
	 * is updated. Changing to an empty author should update the author to Anonymous.
	 *//*
	public void testChangeAuthor() {
		fail();
	}
	
	*//**
	 * Tests that when the user changes the comment body text, the body text is 
	 * updated. Changing to an empty body text should update the text to say 
	 * [Comment Text Removed].
	 *//*
	public void testChangeBodyText() {
		fail();
	}
	
	*//**
	 * Tests that when the user attaches a new picture, the picture is updated.
	 * Removing a picture should display "Picture removed".
	 *//*
	public void testChangePicture() {
		fail();
	}
	
	*//**
	 * Tests that when the user changes the location, LocationSelectionsActivity is opened 
	 * and that location is updated.
	 *//*
	public void testChangeLocation() {
		fail();
	}
}*/
