package ca.ualberta.lard.test;

import ca.ualberta.lard.NewCommentActivity;
import ca.ualberta.lard.model.GeoLocation;
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
		String value = "Test";
		intent.putExtra(NewCommentActivity.PARENT_ID, value);
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
	}

}
