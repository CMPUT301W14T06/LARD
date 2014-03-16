package ca.ualberta.lard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.GeoLocationMap;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class LocationSelectionActivity extends Activity {
	public final static String LOCATION_REQUEST = "LOCATION"; // the value of this can be changed
	//current GPS location is the default
	private boolean gpsLocationClicked;
	private boolean customLocationClicked;
	boolean debug_mode = true;
	//public ListView listView;
	public Spinner spinner;
	String slectedLocationString;
	GeoLocation geoLocation;
	TextView shownLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_selection);
		//default state is gps location is set to true
		gpsLocationClicked = true;
		customLocationClicked = false;
		RadioButton gpsLocationRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
		RadioButton customLocationRadioButton = (RadioButton) findViewById(R.id.selectLocationRadioButton);
		gpsLocationRadioButton.setChecked(true);
		//locks gps RadioButton and unlocks custom RadioButton
		gpsLocationRadioButton.setClickable(false);
		customLocationRadioButton.setClickable(true);
		
		spinner = (Spinner) findViewById(R.id.locationSpinner);
		
		//Gets a list of locations from the GeoLocation map then converts it to a string array
		GeoLocationMap geoMap = new GeoLocationMap();
		Set<String> nameSet = geoMap.getMap().keySet();
		String[] stringArray = Arrays.copyOf(nameSet.toArray(), nameSet.toArray().length, String[].class);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.location_list_item, stringArray);
		spinner.setAdapter(adapter);
		
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		       
		    	//gets the name of the clicks item	
		        String string = (spinner.getItemAtPosition(position)).toString();
		        slectedLocationString = string;
		        
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		 
	}
	
    protected void onStart() {
        super.onStart(); 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_selection, menu);
		return true;
	}
	
	
	//gets RadioButton clicked then unchecks the other RadioButton, also sets the bools
	public void gpsLocationClick(View view) {
		RadioButton gpsLocationRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
		RadioButton customLocationRadioButton = (RadioButton) findViewById(R.id.selectLocationRadioButton);
		//changes what the view wants to return
		gpsLocationClicked = true;
		customLocationClicked = false;
		//cant have two checkboxes selected at same time
		if (customLocationRadioButton.isChecked()) {
			customLocationRadioButton.setChecked(false);
	    }
		//locks gps RadioButton and unlocks custom RadioButton
		gpsLocationRadioButton.setClickable(false);
		customLocationRadioButton.setClickable(true);
	}
	
	//gets RadioButton clicked then unchecks the other RadioButton, also sets the bools
	public void customLocationClick(View view) {
		RadioButton customLocationRadioButton = (RadioButton) findViewById(R.id.selectLocationRadioButton);
		RadioButton gpsLocationRadioButton = (RadioButton) findViewById(R.id.gpsRadioButton);
		//changes what the view wants to return
		gpsLocationClicked = false;
		customLocationClicked = true;
		//cant have two RadioButton selected at same time
		if (gpsLocationRadioButton.isChecked()) {
			gpsLocationRadioButton.setChecked(false);
	    }
		//locks location RadioButton and unlocks gps RadioButton
		gpsLocationRadioButton.setClickable(true);
		customLocationRadioButton.setClickable(false);
	}
	
	public void locationSaveClick(View view){		
		if(gpsLocationClicked == true) {
			//Create a gps location from current phone context
			geoLocation = new GeoLocation(getApplicationContext());
			//serializes string then send it to previous activity
			if (geoLocation != null) {
				String geoString = geoLocation.toJSON();
				Intent resultData = new Intent();
				resultData.putExtra("geoLocation", geoString);
				setResult(Activity.RESULT_OK, resultData);
			}
			
			finish();
		}
		else if (customLocationClicked == true) {
	
			if( slectedLocationString != null || slectedLocationString.isEmpty() == false) {
			
				GeoLocationMap geoMap = new GeoLocationMap();
				double lat = (geoMap.getMap()).get(slectedLocationString).first;
				double lon = (geoMap.getMap()).get(slectedLocationString).second;
				geoLocation = new GeoLocation(lat,lon);
			
				String geoString = geoLocation.toJSON();
				Intent resultData = new Intent();
				resultData.putExtra("geoLocation", geoString);
				setResult(Activity.RESULT_OK, resultData);

				System.out.println(lat);
				System.out.println(lon);
			}
			finish();
		}
		else {
			//We should not of got here
			finish();
		}
	}
	

}
