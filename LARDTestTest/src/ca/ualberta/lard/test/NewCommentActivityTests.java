package ca.ualberta.lard.test;

import ca.ualberta.lard.NewCommentActivity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class NewCommentActivityTests extends ActivityInstrumentationTestCase2<NewCommentActivity> {
	
	public NewCommentActivityTests() {
		super(NewCommentActivity.class);
	}
	
	public void testParentID() {
		Intent intent = new Intent();
		int value = 1;
		intent.putExtra("parentID", value);
		setActivityIntent(intent);
		NewCommentActivity activity = getActivity();
		
		assertEquals("NewCommentActivity should get the value from intent", value, activity.getPid());
	}

}
