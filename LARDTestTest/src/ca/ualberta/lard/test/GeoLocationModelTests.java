package ca.ualberta.lard.test;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.GeoLocation;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Junit tests for GeoLocation model. Tests the constructor, lat and lon
 * getters, distanceFrom function and JSON serialization. 
 */
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
		GeoLocation geoLocation = new GeoLocation(60.0, 80.0);
		
		assertNotNull("getLatitude should give a return", geoLocation.getLatitude());
		assertNotNull("getLongitude should give a return", geoLocation.getLongitude());
	}
	
	/**
	 * Makes sure that a distance is the same to and from two destination points
	 */
	public void testDistanceFrom() {
		GeoLocation geoLocation1 = new GeoLocation(60.0, 60.0);
		GeoLocation geoLocation2 = new GeoLocation(60.0, 80.0);
		
		double distance1 = geoLocation1.distanceFrom(geoLocation2);
		double distance2 = geoLocation2.distanceFrom(geoLocation1);
		
		assertEquals("The Distance should be the same", distance1, distance2);
	}
	
	/**
	 * Tests the constructor by creating a GeoLocation and comparing attributes
	 */
	public void testConstructor() {
		GeoLocation geoLocation = new GeoLocation(60.0, 70.0);
		assertEquals("newGeoLocation long should be 70.0", 70.0, geoLocation.getLongitude());
		assertEquals("newGeoLocation lat should be 60.0", 60.0, geoLocation.getLatitude());
	}
	
	/**
	 * Tests the JSON String serialization and de-serialization by creating
	 * a GeoLocation, serialization then comparing to see if the results are the same
	 * after de-serialization.
	 */
	public void testSerialization() {
		GeoLocation geoLocation = new GeoLocation(60.0, 70.0);
		String string = geoLocation.toJSON();
		GeoLocation newGeoLocation = GeoLocation.fromJSON(string);
		
		assertEquals("newGeoLocation lat should be the same", geoLocation.getLatitude(), newGeoLocation.getLatitude());
		assertEquals("newGeoLocation lon should be the same", geoLocation.getLongitude(), newGeoLocation.getLongitude());
		assertEquals("newGeoLocation long should be 70.0", 70.0, newGeoLocation.getLongitude());
		assertEquals("newGeoLocation lat should be 60.0", 60.0, newGeoLocation.getLatitude());
	}
}
