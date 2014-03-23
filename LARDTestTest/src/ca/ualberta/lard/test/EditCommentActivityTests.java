package ca.ualberta.lard.test;

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
	
	/**
	 * Tests that when the activity is started, a comment id is received.
	 */
	public void testReceiveId() {
		String passedId = (String)activity.getIntent().getStringExtra(EditCommentActivity.EXTRA_COMMENT_ID);
		assertEquals("Should receive an id from the intent.", id, passedId);
	}
	
	/**
	 * Tests that when the user changes the comment author, the comment author
	 * is updated. Changing to an empty author should update the author to Anonymous.
	 */
	public void testChangeAuthor() {
		fail();
	}
	
	/**
	 * Tests that when the user changes the comment body text, the body text is 
	 * updated. Changing to an empty body text should update the text to say 
	 * [Comment Text Removed].
	 */
	public void testChangeBodyText() {
		fail();
	}
	
	/**
	 * Tests that when the user attaches a new picture, the picture is updated.
	 * Removing a picture should display "Picture removed".
	 */
	public void testChangePicture() {
		fail();
	}
	
	/**
	 * Tests that when the user changes the location, LocationSelectionsActivity is opened 
	 * and that location is updated.
	 */
	public void testChangeLocation() {
		fail();
	}
}