package ca.ualberta.lard;

import java.util.Arrays;
import java.util.Set;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.GeoLocationMap;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Returns A GeoLocation through intents. Can create a GeoLocation with your current
 * gps location or a location based on a list of pre-created locations on campus
 * @author Thomas
 */

public class LocationSelectionActivity extends Activity {
	private boolean gpsLocationClicked;
	private boolean customLocationClicked;
	private Spinner spinner;
	private String slectedLocationString;
	private GeoLocation geoLocation;
	
	// For getting the location set by this activity
	public static final String LOCATION_REQUEST = "LOCATION";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_selection);
		
		// Default state is gps location is set to true
		gpsLocationClicked = true;
		customLocationClicked = false;
		RadioButton gpsLocationRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
		RadioButton customLocationRadioButton = (RadioButton) findViewById(R.id.selectLocationRadioButton);
		gpsLocationRadioButton.setChecked(true);
		
		// Locks gps RadioButton and unlocks custom RadioButton
		gpsLocationRadioButton.setClickable(false);
		customLocationRadioButton.setClickable(true);
		
		spinner = (Spinner) findViewById(R.id.locationSpinner);
		
		// Gets a list of locations from the GeoLocation map then converts it to a string array
		GeoLocationMap geoMap = new GeoLocationMap();
		Set<String> nameSet = geoMap.getMap().keySet();
		String[] stringArray = Arrays.copyOf(nameSet.toArray(), nameSet.toArray().length, String[].class);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.location_list_item, stringArray);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	// Gets the name of the clicks item	
		        String string = (spinner.getItemAtPosition(position)).toString();
		        slectedLocationString = string;
		    }

		    
		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // does nothing, but needed for Listener
		    }
		});
	}
	
	// TODO: Not sure if we even need this function
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_selection, menu);
		return true;
	}
	
	// Gets RadioButton clicked then unchecks the other RadioButton, also sets the bools
	public void gpsLocationClick(View view) {
		gpsLocationClicked = true;
		customLocationClicked = false;
		radioButtonSelector(view);
	}
	
	// gets RadioButton clicked then unchecks the other RadioButton, also sets the bools
	public void customLocationClick(View view) {
		gpsLocationClicked = false;
		customLocationClicked = true;
		radioButtonSelector(view);
	}
	
	private void radioButtonSelector(View view) {
		RadioButton gpsLocationRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
		RadioButton customLocationRadioButton = (RadioButton) findViewById(R.id.selectLocationRadioButton);
		
		// swaps which RadioButton has been clicked
		gpsLocationRadioButton.setChecked(gpsLocationClicked);
		customLocationRadioButton.setChecked(customLocationClicked);
		
		// swaps which RadioButton can be clicked
		gpsLocationRadioButton.setClickable(customLocationClicked);
		customLocationRadioButton.setClickable(gpsLocationClicked);
	}
	
	public void locationSaveClick(View view){		
		if(gpsLocationClicked == true) {
			// Create a gps location from current phone context
			geoLocation = new GeoLocation(getApplicationContext());
			
			if (geoLocation != null) {
				// serializes string 
				String geoString = geoLocation.toJSON();
				// sends serialized string to parent activity
				Intent resultData = new Intent();
				resultData.putExtra(LOCATION_REQUEST, geoString);
				setResult(Activity.RESULT_OK, resultData);
			}
			finish();
		}
		else if (customLocationClicked == true) {
	
			if (slectedLocationString != null || slectedLocationString.isEmpty() == false) {
				//Creates a new GeoLocation based on what location was selected
				GeoLocationMap geoMap = new GeoLocationMap();
				double lat = (geoMap.getMap()).get(slectedLocationString).first;
				double lon = (geoMap.getMap()).get(slectedLocationString).second;
				geoLocation = new GeoLocation(lat,lon);
				//serializes the GeoLocation
				String geoString = geoLocation.toJSON();
				//Sends the serialized GeoLocation to the parent activity
				Intent resultData = new Intent();
				resultData.putExtra(LOCATION_REQUEST, geoString);
				setResult(Activity.RESULT_OK, resultData);
			}
			
			finish();
		}
		else {
			// We should not of got here
			Toast.makeText(getApplicationContext(), "Neither boolean set to true", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	

}
