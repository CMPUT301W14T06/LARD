package ca.ualberta.lard.model;

import com.google.gson.Gson;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class GeoLocation {
	
	private double lon;
	private double lat;
	
	public GeoLocation(Context context) {
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		this.lon = location.getLongitude();
		this.lat = location.getLatitude();
	}
	
	public GeoLocation(int id) {
		// Todo enumerated locations
		
		// Todo remove
		if (id == 9999) {
			this.lon = 60;
			this.lat = 60;
					
		}
	}
	/**
	 * 
	 * @param lat
	 * @param lon
	 */
	public GeoLocation (double lat, double lon) {
		this.lon = lon;
		this.lat = lat;
	}
	
	
	public double getLatitude() {
		return this.lat;
	}
	
	public double getLongitude() {
		return this.lon;
	}
	
	
	// Thanks for the base code for the haversine formula here: http://www.movable-type.co.uk/scripts/latlong.html
	public double distanceFrom(GeoLocation loc1) {
		int earthRadius = 6371; // earth's radius in KM - constant in source code because we don't expect this to change - ever.
		double dLat = Math.toRadians(this.getLatitude() - loc1.getLatitude());
		double dLon = Math.toRadians(this.getLongitude() - loc1.getLongitude());
		double lat1 = Math.toRadians(loc1.getLatitude());
		double lat2 = Math.toRadians(this.getLatitude());

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return earthRadius * c;
	}
	
	//Returns this model as a Gson serialized text
	public String toJSON() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
	
	//Turns a Gson serialized text into a GeoLocation Object
	public static GeoLocation fromJSON(String text) {
		Gson gson = new Gson();
		GeoLocation new_model = gson.fromJson(text, GeoLocation.class);
		return new_model;
	}

}
