package ca.ualberta.lard.test;

import ca.ualberta.lard.LocationSelectionActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.RadioButton;

/**
 * JUnit tests for LocationSettingsActivity. Tests widgets like the buttons and
 * spinner and tests its ability to generate a GeoLocation.
 * @author curnow
 *
 */
public class LocationSelectionActivityTests extends ActivityInstrumentationTestCase2<LocationSelectionActivity> {

	public LocationSelectionActivityTests() {
		super(LocationSelectionActivity.class);
	}

	/**
	 * Tests to see if RadioButtons are clickable
	 */
	public void testRadioButtons() {
		fail();
	}
	
	/**
	 * Tests to see if the Spinner is clickable
	 */
	public void testSpinner() {
		fail();
	}
	
	/**
	 * Tests to see if LocationSettingsActivity can create a Geolocation
	 */
	public void testGeoLocationConstructor() {
		fail();
	}
}
