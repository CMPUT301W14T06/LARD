package ca.ualberta.lard.test;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.Stretchy.SearchRequest;
import android.test.ActivityInstrumentationTestCase2;

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
	
	public void testBodyTerm() {
		SearchRequest s = new SearchRequest(1);
		s.bodyText("wow, such comment");
		
		String json = "{ \"size\" : 1, \"query\" : { \"term\" : { \"bodyText\" : \"wow, such comment\" } } }";
		System.err.println(json);
		System.err.println(s.toString());
		assertEquals(json, s.toString());
	}

}
