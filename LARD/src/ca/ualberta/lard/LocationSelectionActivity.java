package ca.ualberta.lard;

import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.GeoLocationMap;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Returns A GeoLocation through intents. Can create a GeoLocation with your current
 * gps location, a location based on a list of pre-created locations on campus or
 * a location created manually by inputing the desired latitude and longitude.
 * @author Thomas
 */
public class LocationSelectionActivity extends Activity {
	private boolean gpsLocationClicked;
	private boolean selectedLocationClicked;
	private boolean customLocationClicked;
	private Spinner spinner;
	private String selectedLocationString;
	private GeoLocation geoLocation;
	
	// For getting the location set by this activity
	public static final String LOCATION_REQUEST = "LOCATION";
	
	/**
	 * Gets the widgets and sets them to their default state. The Default state is that the RadioButton
	 * for using your current gps location is clicked. It populates the spinner with some default 
	 * location across local campus (CSC, CAB, etc) and makes the spinner clickable
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_selection);
		
		//Makes the action bar title look cleaner
		getActionBar().setTitle("Location Settings");
		
		// Default state is gps location is set to true
		gpsLocationClicked = true;
		selectedLocationClicked = false;
		customLocationClicked = false;
		RadioButton gpsLocationRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
		RadioButton selectedLocationRadioButton = (RadioButton) findViewById(R.id.selectLocationRadioButton);
		RadioButton customLocationRadioButton = (RadioButton) findViewById(R.id.customLocationRadioButton);
		gpsLocationRadioButton.setChecked(true);
		
		// Locks gps RadioButton and unlocks Selected RadioButton
		gpsLocationRadioButton.setClickable(false);
		selectedLocationRadioButton.setClickable(true);
		customLocationRadioButton.setClickable(true);
		
		spinner = (Spinner) findViewById(R.id.locationSpinner);
		// Gets a list of locations from the GeoLocation map then converts it to a string array
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.location_array, R.layout.spinner_item);
		//sets the spinner item as the custom one so you can have increased text size 
		adapter.setDropDownViewResource(R.layout.spinner_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			/**
			 * Gets the name of the clicked item then stores the item's string in an attribute
			 * so that when you click save it had a selected location to build a GeoLocation from.
			 */
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        String string = (spinner.getItemAtPosition(position)).toString();
		        selectedLocationString = string;	        
		        //sets text color of spinner to white
				TextView textView = (TextView) spinner.getSelectedView();
				textView.setTextColor(Color.WHITE);
		    }
		    
		    /**
		     * does nothing, but needed for Listener
		     */
		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});
	}
	
	/**
	 * Gets RadioButton clicked then unchecks the other RadioButton, also sets the bools
	 * @param view
	 */
	public void gpsLocationClick(View view) {
		gpsLocationClicked = true;
		selectedLocationClicked = false;
		customLocationClicked = false;
		radioButtonSelector(view);
	}
	
	/**
	 * Gets the clicked RadioButton then unchecks the other RadioButton, also sets the bools
	 * @param view
	 */
	public void selectedLocationClick(View view) {
		gpsLocationClicked = false;
		selectedLocationClicked = true;
		customLocationClicked = false;
		radioButtonSelector(view);
	}
	
	/**
	 * Gets the clicked RadioButton then unchecks the other RadioButton, also sets the bools
	 * @param view
	 */
	public void customLocationClick(View view) {
		gpsLocationClicked = false;
		selectedLocationClicked = false;
		customLocationClicked = true;
		radioButtonSelector(view);
	}
	
	/**
	 * Makes sure you cant have both RadioButtons selected at the same time
	 * @param view
	 */
	private void radioButtonSelector(View view) {
		RadioButton gpsLocationRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
		RadioButton selectedLocationRadioButton = (RadioButton) findViewById(R.id.selectLocationRadioButton);
		RadioButton customLocationRadioButton = (RadioButton) findViewById(R.id.customLocationRadioButton);
		
		// swaps which RadioButton has been clicked
		gpsLocationRadioButton.setChecked(gpsLocationClicked);
		selectedLocationRadioButton.setChecked(selectedLocationClicked);
		customLocationRadioButton.setChecked(customLocationClicked);
		
		// swaps which RadioButton can be clicked
		gpsLocationRadioButton.setClickable(selectedLocationClicked || customLocationClicked);
		selectedLocationRadioButton.setClickable(gpsLocationClicked || customLocationClicked);
		customLocationRadioButton.setClickable(selectedLocationClicked || gpsLocationClicked);
	}
	
	/**
	 * Will return a GeoLocation when you press the save button, what GeoLocation
	 * You get will depend on what RadioButtons are selected and what Location
	 * you have selected on the spinner or what value you have in the lat and lon fields.
	 * @param view
	 */
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
		else if (selectedLocationClicked == true) {
			if (selectedLocationString != null || selectedLocationString.isEmpty() == false) {
				//Creates a new GeoLocation based on what location was selected
				GeoLocationMap geoMap = new GeoLocationMap();
				double lat = (geoMap.getMap()).get(selectedLocationString).first;
				double lon = (geoMap.getMap()).get(selectedLocationString).second;
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
		
		else if (customLocationClicked == true) {
			EditText latEditText = (EditText) findViewById(R.id.latEditText);
			EditText lonEditText = (EditText) findViewById(R.id.lonEditText);
			
			//checks if strings are empty, if are then make it 0
			String latString = latEditText.getText().toString();
			String lonString = lonEditText.getText().toString();
			if(latString.matches("")) {
				latEditText.setText("0");
			}
			if( lonString.matches("") ) {
				lonEditText.setText("0");	
			}
			double lat = Double.parseDouble(latEditText.getText().toString());
			double lon = Double.parseDouble(lonEditText.getText().toString());
			geoLocation = new GeoLocation(lat, lon);
			if (geoLocation != null ) {
				// serializes string 
				String geoString = geoLocation.toJSON();
				// sends serialized string to parent activity
				Intent resultData = new Intent();
				resultData.putExtra(LOCATION_REQUEST, geoString);
				setResult(Activity.RESULT_OK, resultData);
				finish();
			}
			
		}
		
		else {
			// We should not of got here
			Toast.makeText(getApplicationContext(), "Neither boolean set to true", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	

}
