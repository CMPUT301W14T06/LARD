package ca.ualberta.lard;

import java.util.ArrayList;

import ca.ualberta.lard.model.GeoLocation;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class LocationSelectionActivity extends Activity {
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
		CheckBox gpsLocationCheckBox = (CheckBox) findViewById(R.id.gpsLocationCheckbox);
		CheckBox customLocationCheckBox = (CheckBox) findViewById(R.id.customLocationCheckbox);
		gpsLocationCheckBox.setChecked(true);
		//locks gps checkbox and unlocks custom checkbox
		gpsLocationCheckBox.setClickable(false);
		customLocationCheckBox.setClickable(true);
		
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
	
	
	//gets checkboxed clicked then unchecks the other checkbox, also sets the bools
	public void gpsLocationClick(View view) {
		CheckBox gpsLocationCheckBox = (CheckBox) findViewById(R.id.gpsLocationCheckbox);
		CheckBox customLocationCheckBox = (CheckBox) findViewById(R.id.customLocationCheckbox);
		//changes what the view wants to return
		gpsLocationClicked = true;
		customLocationClicked = false;
		//cant have two checkboxes selected at same time
		if (customLocationCheckBox.isChecked()) {
			customLocationCheckBox.setChecked(false);
	    }
		//locks gps checkbox and unlocks custom checkbox
		gpsLocationCheckBox.setClickable(false);
		customLocationCheckBox.setClickable(true);
	}
	
	//gets checkboxed clicked then unchecks the other checkbox, also sets the bools
	public void customLocationClick(View view) {
		CheckBox customLocationCheckBox = (CheckBox) findViewById(R.id.customLocationCheckbox);
		CheckBox gpsLocationCheckBox = (CheckBox) findViewById(R.id.gpsLocationCheckbox);
		//changes what the view wants to return
		gpsLocationClicked = false;
		customLocationClicked = true;
		//cant have two checkboxes selected at same time
		if (gpsLocationCheckBox.isChecked()) {
			gpsLocationCheckBox.setChecked(false);
	    }
		//locks location checkbox and unlocks gps checkbox
		gpsLocationCheckBox.setClickable(true);
		customLocationCheckBox.setClickable(false);
	}
	
	public void locationSaveClick(View view){
		
		if(gpsLocationClicked == true) {
			//Create a gps location from current phone context
			//geoLocation = new GeoLocation(getApplicationContext());
		}
		else if (customLocationClicked == true) {
			//checks if you have selected a location
			if (slectedLocationString == null ||slectedLocationString.isEmpty() || slectedLocationString == "Please select a location from the list!") {
				slectedLocationString = "Please select a location from the list!";
				shownLocation.setText("Please select a location!");
				return;
			}
			//TODO enumerated locations
			//geoLocation = new GeoLocation(id)
		}
		else {
			//We should not of got here
		}
	}
	

}
