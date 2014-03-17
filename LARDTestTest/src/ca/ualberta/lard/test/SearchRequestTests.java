package ca.ualberta.lard.test;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.Stretchy.SearchRequest;
import android.test.ActivityInstrumentationTestCase2;

/**
 * JUnit tests for the SearchRequest class, which used in elastic search to turn
 * information about a comment into a specific Json String literal format.
 * Tests that the string is correctly created.
 */

public class SearchRequestTests extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public SearchRequestTests() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Test that search request creates the json string literal in the correct format
	 * when body text is not empty.
	 */
	public void testBodyTerm() {
		SearchRequest s = new SearchRequest(1);
		s.bodyText("wow, such comment");
		
		String json = "{ \"size\" : 1, \"query\" : { \"term\" : { \"bodyText\" : \"wow, such comment\" } } }";
		System.err.println(json);
		System.err.println(s.toString());
		assertEquals(json, s.toString());
	}
	
	/**
	 * Test that search request creates the json string literal in the correct format 
	 * when the parent id is not empty.
	 */
	public void testParentId() {
		fail();
		SearchRequest s = new SearchRequest(1);
		s.parent("1234");

		String json = "{ \"size\" : 1, \"query\" : { \"term\" : { \"parent\" : \"1234\" } } }";
		System.err.println(json);
		System.err.println(s.toString());
		assertEquals(json, s.toString());
	}
	
	/**
	 * Test that search request creates the json string literal in the correct format 
	 * when no information is provided.
	 */
	public void testNoInfo() {
		SearchRequest s = new SearchRequest(1);

		String json = "{ \"size\" : 1, \"query\" : { \"term\" : { \"bodyText\" : \"\" } } }";
		System.err.println(json);
		System.err.println(s.toString());
		assertEquals(json, s.toString());
	}

}
