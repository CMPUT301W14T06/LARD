package ca.ualberta.lard;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class LocationSelectionActivity extends Activity {
	//current gps location is the defailt
	boolean gpsLocationClicked = true;
	boolean customLocationClicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_selection);
		
		//default state is gps location is set to true
		CheckBox gpsLocationCheckBox = (CheckBox) findViewById(R.id.gpsLocationCheckbox);
		CheckBox customLocationCheckBox = (CheckBox) findViewById(R.id.customLocationCheckbox);
		gpsLocationCheckBox.setChecked(true);
		
		//locks gps checkbox and unlocks custom checkbox
		gpsLocationCheckBox.setClickable(false);
		customLocationCheckBox.setClickable(true);
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_selection, menu);
		return true;
	}
	
	/*
	TODO: Ask about whether we want the activity to contain a GEOLOCATION model or not
	ALSO: Probally dont want to use the onBackPressed, going to ask more questions on what I should use instead
	
	@Override
	public void onBackPressed() {
	    return;
	}
	*/
	
	//gets checkboxed clicked then unchecks the other checkbox, also sets the bools
	public void gpsLocationClick(View view) {
		CheckBox gpsLocationCheckBox = (CheckBox) findViewById(R.id.gpsLocationCheckbox);
		CheckBox customLocationCheckBox = (CheckBox) findViewById(R.id.customLocationCheckbox);
		//changes what the view wants to return
		boolean gpsLocationClicked = true;
		boolean customLocationClicked = false;
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
		boolean gpsLocationClicked = false;
		boolean customLocationClicked = true;
		//cant have two checkboxes selected at same time
		if (gpsLocationCheckBox.isChecked()) {
			gpsLocationCheckBox.setChecked(false);
	    }
		//locks location checkbox and unlocks gps checkbox
		gpsLocationCheckBox.setClickable(true);
		customLocationCheckBox.setClickable(false);
	}
	

}
