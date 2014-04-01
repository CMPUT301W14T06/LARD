package ca.ualberta.lard.test;

import java.util.ArrayList;

import com.google.gson.Gson;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.Stretchy.SearchRequest;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;
import android.test.ActivityInstrumentationTestCase2;

/**
 * JUnit tests for the Datamodel. Tests handling requests from Comment model and saving
 * locally and to stretchy client.
 */

public class DataModelTests extends ActivityInstrumentationTestCase2<MainActivity> {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public DataModelTests() {
		super(MainActivity.class);
	}
	
	/**
	 * Tests that a comment can successfully be saved to stretchy client.
	 */
	// This test will be a little bit handwavey, I suppose. We're involving external infrastructure.
	public void testSave() {
		Comment c = new Comment("Eldon is mad stinky", getActivity());
		Gson gson = new Gson();
		System.err.println(gson.toJson(c));
		boolean result = DataModel.save(c);
		assertTrue(result); // this is a finnicky not-a-test
	}
	
	/**
	 * Tests making a request to the Datamodel for an existing comment returns the correct
	 * comment and returns only 1 comment.
	 * Currently uses a hard-coded id that we know exists.
	 */
	public void testGetByIdExists() {
		final String id = "64e5d2c9-97f3-42d4-844e-911ebbfdd2e5"; 
		
		CommentRequest req = new CommentRequest(1);
		req.setId(id);
		ArrayList<Comment> arr = DataModel.retrieveComments(req);
		
		assertEquals(arr.size(), 1);
		
		Comment c = arr.get(0);
		
		assertEquals(c.getId(), id);
	}
	
	/**
	 * Tests that trying to make a request to the Datamodel for a non-existent comment
	 * returns null.
	 */
	public void testGetByIdNotExists() {
		final String id = "farts-and-tarts";
		
		CommentRequest req = new CommentRequest(1);
		req.setId(id);
		
		ArrayList<Comment> arr = DataModel.retrieveComments(req);
		assertNull(arr);
	}
	
	/**
	 * Tests that the Datamodel is able to check if a comment is saved locally.
	 */
	public void testLocalStorage() {
		//creates a comment then tests to see if it is saved local
		Comment localComment = new Comment("I am lost? am I local?", getActivity());
		DataModel.saveLocal(localComment, true, getActivity(), false);
		//Gets the is of the last element of the local list, it should be the same as our newly created comment
		ArrayList<Comment> localList = DataModel.readLocal(getActivity());
		//tests if equal
		assertEquals("The locally stored comment should be the same", localComment.getId(), localList.get(localList.size() - 1).getId());
	}
	
	/**
	 * Tests that isLocal function
	 */
	public void testIsLocal() {
		//creates a comment then tests to see if it is saved local
		Comment localComment = new Comment("I am lost? am I local?", getActivity());
		DataModel.saveLocal(localComment, true, getActivity(), false);
		//tests to see if true
		assertEquals("The isLocal should return true", true, DataModel.isLocal(localComment, getActivity()));
	}
	
	/**
	 * Tests that the Datamodel can pull at least 1 comment from the server
	 * when a request is made for any comments.
	 * 
	 * Precondition: Elastic search has at least one comment.
	 */
	public void testEmptySearch() {
		CommentRequest req = new CommentRequest(5);
		ArrayList<Comment> comments = DataModel.retrieveComments(req);
		
		
		assertTrue(comments.size() > 1);
		System.err.println(comments.get(0));	
	}
	
	/**
	 * Tests that the Datamodel can search for a comment using only body text.
	 */
	public void testSearchByBody() {
		CommentRequest req = new CommentRequest(5);
		// TODO
		fail();
	}

}
