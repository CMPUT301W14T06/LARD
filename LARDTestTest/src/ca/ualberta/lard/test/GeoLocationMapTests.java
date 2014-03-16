package ca.ualberta.lard.test;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.GeoLocationMap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Pair;

public class GeoLocationMapTests extends ActivityInstrumentationTestCase2<MainActivity> {

	public GeoLocationMapTests() {
		super(MainActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testConstructor() {
		GeoLocationMap geoLocationMap = new GeoLocationMap();
		
		assertNotNull(geoLocationMap.getMap());
		
		Pair<Double, Double> cab = new Pair<Double, Double>(53.526572, -113.524734);
		
		assertEquals("The lon and lat of CAB should be the same", cab, geoLocationMap.getMap().get("CAB") );
	}
	
}
