package ca.ualberta.lard.test;

import android.app.ActionBar;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import ca.ualberta.lard.CommentActivity;
import ca.ualberta.lard.NewCommentActivity;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.DataModel;

/**
 * Tests CommentActivity UI functionality. Tests that all the buttons
 * in the action bar work when clicked and that another CommentActivity
 * is started when a child comment is clicked.
 * @author Victoria
 */

public class CommentActivityTests extends ActivityInstrumentationTestCase2<CommentActivity> {
	Comment comment;
	private String id;
	private Intent intent;
	private CommentActivity activity;
	private Instrumentation instru;

	//ActivityIsolationTestCaseAndroid

	public CommentActivityTests() {
		super(CommentActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
        
        // Get the id of the comment
        //Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
		//comment = new Comment("This is a comment", context);
		//id = comment.getId();
		//DataModel.save(comment);
		id = "1234";
		
		// Pass the activity the id
		intent = new Intent();
		intent.putExtra(CommentActivity.EXTRA_PARENT_ID, id);
		
		setActivityIntent(intent);
		activity = getActivity();
		instru = getInstrumentation();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		comment = null;
		id = null;
		activity = null;
		intent = null;
	}
	
	/**
	 * Tests that when the activity is started, a comment id is received.
	 */
	public void testReceiveId() {
		String passedId = (String)activity.getIntent().getStringExtra(CommentActivity.EXTRA_PARENT_ID);
		assertEquals("CommentActivity should receive a parent id from intent.", id, passedId);
	}

	/**
	 * Tests that all parts of the parent comment and its list of child comments can be 
	 * seen on the screen.
	 * @throws Throwable
	 */
	public void testListViewIsVisable() throws Throwable {
		View view = activity.getWindow().getDecorView();
		
		ViewAsserts.assertOnScreen(view, activity.findViewById(ca.ualberta.lard.R.id.children_list));
		ViewAsserts.assertOnScreen(view, activity.findViewById(ca.ualberta.lard.R.id.parent_author));
		ViewAsserts.assertOnScreen(view, activity.findViewById(ca.ualberta.lard.R.id.parent_comment_body));
		ViewAsserts.assertOnScreen(view, activity.findViewById(ca.ualberta.lard.R.id.parent_location));
		ViewAsserts.assertOnScreen(view, activity.findViewById(ca.ualberta.lard.R.id.parent_num_replies));
		ViewAsserts.assertOnScreen(view, activity.findViewById(ca.ualberta.lard.R.id.parent_picture));
	}
	
	/**
	 * Tests that a NewCommentActivity is opened when the reply button in the action bar
	 * is clicked and that the id of the parent comment is sent.
	 * @throws Throwable
	 * 
	 * Credit: http://stackoverflow.com/questions/20023483/how-to-get-actionbar-view
	 */
	public void testReply() throws Throwable {
		// Get the reply button and click it
	    Window window = activity.getWindow();
	    View v = window.getDecorView();
	    int resId = activity.getResources().getIdentifier("action_reply", "id", "android");
	    final View view = activity.findViewById(resId);

		//final View view = (View) actionBar.getCustomView().findViewById(ca.ualberta.lard.R.id.action_reply);
	    assertNotNull("Reply button should not be null.", view);
	    // get a menu view 
	    
	    runTestOnUiThread(new Runnable() {
	    	@Override
	    	public void run() {
	    		view.requestFocus();
	    		view.performClick();
	    	}
	    });
	    
	    /*
	      activity.runOnUiThread(new Runnable() {
	          @SuppressLint("NewApi")
			public void run() {
		    		view.requestFocus();
		    		//view.performClick();
		    		view.callOnClick();
	          }
	      });
	      */

	    instru.waitForIdleSync();
	    
	    // Get the id from the NewCommentActivity intent.
	    String passedId = intent.getStringExtra(NewCommentActivity.PARENT_ID);	    
	    assertEquals("Passed id should match parent id.", id, passedId);
	}
	
	/**
	 * Tests that the parent comment of the list is added to the favourites list when
	 * the favourite button in the action bar is clicked.
	 * @throws Throwable
	 */
	public void testFavourites() throws Throwable {
		// Get the save button
	    final Button view = (Button) activity.findViewById(ca.ualberta.lard.R.id.action_fav);
	    assertNotNull("Reply favourite should not be null.", view);
	    
	    runTestOnUiThread(new Runnable() {
	    	@Override
	    	public void run() {
	    		view.requestFocus();
	    		view.performClick();
	    	}
	    });
	    
	    instru.waitForIdleSync();
	}

	/**
	 * Tests that the parent comment of the list is saved locally when the reply button
	 * in the action bar is clicked.
	 * @throws Throwable
	 */
	public void testSave() throws Throwable {	
		assertFalse("Comment originally is not saved locally.", DataModel.isLocal(comment, activity.getBaseContext()));
	    
		// Get the save button
	    final View view = (View) activity.findViewById(ca.ualberta.lard.R.id.action_save);
	    assertNotNull("Save button should not be null.", view);
	    
	    runTestOnUiThread(new Runnable() {
	    	@Override
	    	public void run() {
	    		view.requestFocus();
	    		view.performClick();
	    	}
	    });
	    
	    instru.waitForIdleSync();
	    assertTrue("Comment should be saved locally", DataModel.isLocal(comment, activity.getBaseContext()));
	}
}
