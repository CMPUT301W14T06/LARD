package ca.ualberta.lard.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import ca.ualberta.lard.CommentActivity;

public class CommentActivityTests extends ActivityInstrumentationTestCase2<CommentActivity> {
	//ActivityIsolationTestCaseAndroid

	public CommentActivityTests() {
		super(CommentActivity.class);
	}
	
	// Test a parent id is received when the activity is started.
	public void testReceiveId() {
		String id = "1234";
		
		Intent intent = new Intent();
		intent.putExtra(CommentActivity.EXTRA_PARENT_ID, id);
		
		setActivityIntent(intent);
		CommentActivity activity = getActivity();
		
		assertEquals("CommentActivity should receive a parent id from intent.", id,
				activity.getIntent().getStringExtra(CommentActivity.EXTRA_PARENT_ID).toString());
	}

	// Test the list view is on the screen
	public void testListViewIsVisable() throws Throwable {
		CommentActivity activity = getActivity();
		View view = activity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(view, activity.findViewById(ca.ualberta.lard.R.id.toplevel_and_children_list));
	}
	
	
	// Test NewCommentActivity is opened when the reply button is clicked, and that the correct id is sent
	public void testReply() throws Throwable {
		String id = "1234";
		Intent intent = new Intent();
		intent.putExtra(CommentActivity.EXTRA_PARENT_ID, id);
		setActivityIntent(intent);
		CommentActivity activity = getActivity();
		
		// Get the reply button and click it
	    final View view = (View) activity.findViewById(ca.ualberta.lard.R.id.action_reply);
	    runTestOnUiThread(new Runnable() {
	    	@Override
	    	public void run() {
	    		view.requestFocus();
	    		view.performClick();
	    	}
	    });

	    assertNotNull("Parent id was not put into intent", intent);

	    // Get the id from the intent
	    String passedId = intent.getStringExtra(CommentActivity.EXTRA_PARENT_ID).toString();
	    
	    assertEquals("Passed id should match parent id.", id, passedId);
	}
	
	// A favourite should be saved locally and added to the favourites list.
	public void testFavourites() {
		// TODO
		fail();
	}

	// A saved comment should be saved locally.
	public void testSave() {
		fail();
		/* 
		// Create a new comment
		Comment comment = new Comment("This is a comment.", getActivity());
		String id = comment.getId();
		
		Intent intent = new Intent();
		intent.putExtra(CommentActivity.EXTRA_PARENT_ID, id);
		setActivityIntent(intent);
		CommentActivity activity = getActivity();
		
		assertFalse("Comment originally is not saved locally.", DataModel.isLocal(comment));
		
		// Get the save button
	    final View view = (View) activity.findViewById(ca.ualberta.lard.R.id.action_save);
	    activity.runOnUiThread(new Runnable() {
	    	@Override
	    	public void run() {
	    		view.requestFocus();
	    		view.performClick();
	    	}
	    });
	    
	    DataModel.saveLocal(comment, true);
	    assertTrue("Comment should be saved locally", DataModel.isLocal(comment));
	    */ 
	}

}
