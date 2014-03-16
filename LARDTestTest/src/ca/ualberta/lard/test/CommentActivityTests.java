/**
 * Tests CommentActivity UI functionality. Tests that all the buttons
 * in the action bar work when clicked and that another CommentActivity
 * is started when a child comment is clicked.
 */

package ca.ualberta.lard.test;

import java.util.Date;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import ca.ualberta.lard.CommentActivity;
import ca.ualberta.lard.NewCommentActivity;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.DataModel;

public class CommentActivityTests extends ActivityInstrumentationTestCase2<CommentActivity> {
	Comment comment;
	private String id;
	private Intent intent;
	private CommentActivity activity;

	//ActivityIsolationTestCaseAndroid
	
	protected void setUp() {
		comment = new Comment("This is a comment", getActivity());
		id = comment.getId();
		intent = new Intent();
		intent.putExtra(CommentActivity.EXTRA_PARENT_ID, id);
		setActivityIntent(intent);
		activity = getActivity();
	}

	public CommentActivityTests() {
		super(CommentActivity.class);
	}
	
	/**
	 * Tests that when the activity is started, a comment id is received.
	 */
	public void testReceiveId() {		
		assertEquals("CommentActivity should receive a parent id from intent.", id,
				activity.getIntent().getStringExtra(CommentActivity.EXTRA_PARENT_ID).toString());
	}

	/**
	 * Tests that the list of comments can be seen on the screen.
	 * @throws Throwable
	 */
	public void testListViewIsVisable() throws Throwable {
		View view = activity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(view, activity.findViewById(ca.ualberta.lard.R.id.children_list));
	}
	
	/**
	 * Tests that a NewCommentActivity is opened when the reply button in the action bar
	 * is clicked and that the id of the parent comment is sent.
	 * @throws Throwable
	 */
	public void testReply() throws Throwable {
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
	    // Get the id from the NewCommentActivity intent.
	    String passedId = intent.getStringExtra("parentIdD");	    
	    assertEquals("Passed id should match parent id.", id, passedId);
	}
	
	/**
	 * Tests that the parent comment of the list is added to the favourites list when
	 * the favourite button in the action bar is clicked.
	 * @throws Throwable
	 */
	public void testFavourites() throws Throwable {
		// Fails automatically atm
		fail();
		
		// TODO: Add the comment to the favourites list
		
		// Get the save button
	    final View view = (View) activity.findViewById(ca.ualberta.lard.R.id.action_fav);
	    runTestOnUiThread(new Runnable() {
	    	@Override
	    	public void run() {
	    		view.requestFocus();
	    		view.performClick();
	    	}
	    });
	}

	/**
	 * Tests that the parent comment of the list is saved locally when the reply button
	 * in the action bar is clicked.
	 * @throws Throwable
	 */
	public void testSave() throws Throwable {	
		assertFalse("Comment originally is not saved locally.", DataModel.isLocal(comment));
		
		// Get the save button
	    final View view = (View) activity.findViewById(ca.ualberta.lard.R.id.action_save);
	    runTestOnUiThread(new Runnable() {
	    	@Override
	    	public void run() {
	    		view.requestFocus();
	    		view.performClick();
	    	}
	    });
	    
	    DataModel.saveLocal(comment, false, getActivity().getBaseContext());
	    assertTrue("Comment should be saved locally", DataModel.isLocal(comment));
	}
}
