package ca.ualberta.lard.test;

import ca.ualberta.lard.NewCommentActivity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

/**
 * JUnit tests for NewCommentActivity. Tests that the correct parent id is received. Also
 * tests user interaction with the UI (attaching a picture, changing location, inputting
 * text for the comment).
 */

public class NewCommentActivityTests extends ActivityInstrumentationTestCase2<NewCommentActivity> {
	
	public NewCommentActivityTests() {
		super(NewCommentActivity.class);
	}
	
	public void testParentIDExists() {
		Intent intent = new Intent();
		int value = 1;
		intent.putExtra("parentID", value);
		setActivityIntent(intent);
		NewCommentActivity activity = getActivity();
		assertEquals("NewCommentActivity should get the value from intent", value, activity.getPid());
	}
	
	public void testParentIDNotExists() {
		Intent intent = new Intent();
		setActivityIntent(intent);
		NewCommentActivity activity = getActivity();
		assertEquals("NewCommentActivity should get the value from intent", null, activity.getPid());
	}
	
	public void testPicture() {
		// can not test yet, will implement later
		fail();
	}
	
	public void testGeoLocation() {
		// can not test yet, will implement later
		fail();
	}

}
