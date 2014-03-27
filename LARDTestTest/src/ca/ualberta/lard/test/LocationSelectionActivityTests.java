package ca.ualberta.lard.test;

import ca.ualberta.lard.LocationSelectionActivity;
import ca.ualberta.lard.R;
import ca.ualberta.lard.model.Comment;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.RadioButton;
import android.widget.Spinner;

/**
 * JUnit tests for LocationSettingsActivity. Tests widgets like the buttons and
 * spinner and tests its ability to generate a GeoLocation.
 * @author curnow
 *
 */
public class LocationSelectionActivityTests extends ActivityInstrumentationTestCase2<LocationSelectionActivity> {
	private Intent intent;
	private LocationSelectionActivity activity;
	private RadioButton gpsLocationRadioButton;
	private RadioButton customLocationRadioButton;
	private Spinner spinner;
	
	public LocationSelectionActivityTests() {
		super(LocationSelectionActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
		activity = this.getActivity();
		
		gpsLocationRadioButton = (RadioButton) activity.findViewById(R.id.gpsRadioButton);
		customLocationRadioButton = (RadioButton) activity.findViewById(R.id.selectLocationRadioButton);
		spinner = (Spinner) activity.findViewById(R.id.locationSpinner);
	}
	
	/**
	 * Tests to see if RadioButtons are null
	 */
	public void testRadioButtons() {
		assertNotNull(gpsLocationRadioButton);
		assertNotNull(customLocationRadioButton);
	}
	
	/**
	 * Tests to see if the Spinner is not null
	 */
	public void testSpinner() {
		assertNotNull(spinner);
	}
}
