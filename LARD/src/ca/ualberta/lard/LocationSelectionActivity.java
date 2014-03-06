package ca.ualberta.lard;

import java.util.ArrayList;

import ca.ualberta.lard.model.GeoLocation;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class LocationSelectionActivity extends Activity {
	public final static String LOCATION_REQUEST = "LOCATION"; // the value of this can be changed
	//current GPS location is the default
	private boolean gpsLocationClicked;
	private boolean customLocationClicked;
	boolean debug_mode = true;
	public ListView listView;
	int currentId;
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
		
		//Makes the list clickable
		listView = (ListView) findViewById(R.id.locationListView);
		
		//Readies TextView to show select locations
		shownLocation = (TextView)findViewById(R.id.locationTextView);
		
		if (debug_mode) {
			ArrayList<String> locations = new ArrayList<String>();
			locations.add("uacs");
			locations.add("rat");
			locations.add("csc");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.location_list_item, locations);
			listView.setAdapter(adapter);
		}
	
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView parent, View v, int position, long id) {
				//gets the name of the clicks item
		        String string = (listView.getItemAtPosition(position)).toString();
		        shownLocation.setText(string);
		        slectedLocationString = string;
		        //TODO get enumerated location of clicked location
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
			//geoLocation = new GeoLocation(getApplicationContext());
			//Intent resultData = new Intent();
			//resultData.putExtra("geoLocation", "valueData");
			//setResult(Activity.RESULT_OK, resultData);
			//finish();
		}
		else if (customLocationClicked == true) {
			//checks if you have selected a location
			if (slectedLocationString == null ||slectedLocationString.isEmpty() || slectedLocationString == "Please select a location from the list!") {
				slectedLocationString = "Please select a location from the list!";
				shownLocation.setText("Please select a location!");
				return;
			}
			//TODO enumerated locations
			//geoLocation = new GeoLocation(id);
			//Intent resultData = new Intent();
			//resultData.putExtra("geoLocation", "valueData");
			//setResult(Activity.RESULT_OK, resultData);
			//finish();
		}
		else {
			//We should not of got here
		}
	}
	

}
