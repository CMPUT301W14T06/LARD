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
	
	public void testSerialization() {
		GeoLocation geoLocation = new GeoLocation(9999);
		String string = geoLocation.toJSON();
		GeoLocation newGeoLocation = geoLocation.fromJSON(string);
		
		assertEquals("newGeoLocation lat should be the same", geoLocation.getLatitude(), newGeoLocation.getLatitude());
		assertEquals("newGeoLocation lon should be the same", geoLocation.getLongitude(), newGeoLocation.getLongitude());
		assertEquals("newGeoLocation long should be 60.0", 60.0, newGeoLocation.getLongitude());
		assertEquals("newGeoLocation lat should be 60.0", 60.0, newGeoLocation.getLatitude());
	}
	
}
