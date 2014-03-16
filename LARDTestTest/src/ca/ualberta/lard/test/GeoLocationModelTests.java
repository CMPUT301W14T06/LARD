package ca.ualberta.lard.test;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.GeoLocation;
import android.test.ActivityInstrumentationTestCase2;

public class GeoLocationModelTests extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public GeoLocationModelTests() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPreconditions() {
		fail();
		
	}
	
	public void testDistanceFrom() {
		fail();
	}
}
