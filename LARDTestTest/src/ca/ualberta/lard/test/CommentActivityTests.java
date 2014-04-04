package ca.ualberta.lard.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ListView;
import ca.ualberta.lard.CommentActivity;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.DataModel;

/**
 * Tests CommentActivity UI functionality. Tests another CommentActivity
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
        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
		comment = new Comment("JUnit testing comment", context);
		id = comment.getId();
		DataModel.save(comment);
		
		Comment child = new Comment("Junit test child", context);
		child.setParent(id);
		DataModel.save(child);
		
		// Sleep to ensure the comments are pushed to the server before we begin the activity.
		Thread.sleep(1000);
		
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
	 * Tests that clicking a reply to a comment opens another Comment Activity.
	 * @throws InterruptedException 
	 */
	public void testClickChild() throws InterruptedException {
		ActivityMonitor monitor = new ActivityMonitor(CommentActivity.class.getName(), null, false);
		instru.addMonitor(monitor);
	    
		final ListView listview = (ListView) activity.findViewById(ca.ualberta.lard.R.id.children_list);
		assertNotNull("Listview should not be null", listview);
		
		// Wait to ensure list view is actually retrieved
		Thread.sleep(1000);
		
		final View child = listview.getChildAt(0);
		assertNotNull("Child should not be null", child);
		
		// Click the child comment
        activity.runOnUiThread(new Runnable() {
        	@Override
            public void run() {
        		listview.performItemClick(child, 0, listview.getItemIdAtPosition(0));
            }
        });
		
        // Check the activity was started.
        instru.waitForIdleSync();
		Activity childActivity = monitor.waitForActivityWithTimeout(5000);
		assertNotNull("Comment Activity should have started", childActivity);
		childActivity.finish();
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
}
