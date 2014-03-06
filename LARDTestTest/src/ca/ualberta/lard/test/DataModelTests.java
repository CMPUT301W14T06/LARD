package ca.ualberta.lard.test;

import java.util.ArrayList;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.Stretchy.SearchRequest;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;
import android.test.ActivityInstrumentationTestCase2;

public class DataModelTests extends ActivityInstrumentationTestCase2<MainActivity> {

	public DataModelTests() {
		super(MainActivity.class);
	}
	
	// This test will be a little bit handwavey, I suppose. We're involving external infrastructure.
	public void testSave() {
		Comment c = new Comment("Super comment wow, such comment amaze", getActivity());
		boolean result = DataModel.save(c);
		assertTrue(result); // this is a finnicky not-a-test
	}
	
	// For the time being we're using a hardcoded ID that we know exists
	public void testGetByIdExists() {
		final String id = "64e5d2c9-97f3-42d4-844e-911ebbfdd2e5"; 
		
		CommentRequest req = new CommentRequest(1);
		req.setId(id);
		ArrayList<Comment> arr = DataModel.retrieveComments(req);
		
		assertEquals(arr.size(), 1);
		
		Comment c = arr.get(0);
		
		assertEquals(c.getId(), id);
	}
	
	public void testGetByIdNotExists() {
		final String id = "farts-and-tarts";
		
		CommentRequest req = new CommentRequest(1);
		req.setId(id);
		
		ArrayList<Comment> arr = DataModel.retrieveComments(req);
		assertNull(arr);
	}
	
	public void testIsLocal() {
		fail();
	}
	
	/**
	 * Precondition: Elastic search has at least one comment.
	 */
	public void testEmptySearch() {
		CommentRequest req = new CommentRequest(5);
		ArrayList<Comment> comments = DataModel.retrieveComments(req);
		
		
		assertTrue(comments.size() > 1);
		System.err.println(comments.get(0));
		
		
	}
	
	public void testSearchByBody() {
		CommentRequest req = new CommentRequest(5);
		fail();
	}

}
