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
import android.widget.TextView;
import android.widget.Toast;

/**
 * [ADD DESCRIPTION]
 *
 * @author Thomas
 */

public class LocationSelectionActivity extends Activity {
	private boolean gpsLocationClicked;
	private boolean customLocationClicked;
	private Spinner spinner;
	private String slectedLocationString;
	private GeoLocation geoLocation;
	
	@SuppressWarnings("unused")
	private TextView shownLocation; // TODO: Something?
	
	// For getting the location set by this activity
	public static final String LOCATION_REQUEST = "LOCATION";
	
	@SuppressWarnings("unused")
	private boolean debug_mode = true; // TODO: Remove
	
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
		        // TODO: Something?
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
		commonCode(view);
	}
	
	// gets RadioButton clicked then unchecks the other RadioButton, also sets the bools
	public void customLocationClick(View view) {
		gpsLocationClicked = false;
		customLocationClicked = true;
		commonCode(view);
	}
	
	// TODO: Rename this
	private void commonCode(View view) {
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
			
			// serializes string then send it to previous activity
			if (geoLocation != null) {
				String geoString = geoLocation.toJSON();
				Intent resultData = new Intent();
				resultData.putExtra(LOCATION_REQUEST, geoString);
				setResult(Activity.RESULT_OK, resultData);
			}
			
			finish();
		}
		else if (customLocationClicked == true) {
	
			if (slectedLocationString != null || slectedLocationString.isEmpty() == false) {
				GeoLocationMap geoMap = new GeoLocationMap();
				double lat = (geoMap.getMap()).get(slectedLocationString).first;
				double lon = (geoMap.getMap()).get(slectedLocationString).second;
				geoLocation = new GeoLocation(lat,lon);
			
				String geoString = geoLocation.toJSON();
				Intent resultData = new Intent();
				resultData.putExtra(LOCATION_REQUEST, geoString);
				setResult(Activity.RESULT_OK, resultData);

				System.out.println(lat); // TODO: Remove?
				System.out.println(lon); // TODO: Remove?
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
