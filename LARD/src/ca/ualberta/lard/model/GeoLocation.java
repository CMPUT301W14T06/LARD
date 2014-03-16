package ca.ualberta.lard.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * A GeoLocation is composed of a latitude and a longitude, and as the name
 * suggests it represents a location in the world. It can be set
 * using specific coordinates or it can be set by getting the position of 
 * the users device in the world.
 */

public class GeoLocation {
	
	private double lon;
	private double lat;
	
	public ArrayList<GeoLocation> locations;
	
	/**
	 * Enumeration of preset coordinates for buildings on campus.
	 * This is for GeoLocationActivity so that the user can choose easily 
	 * choose a location.
	 * @author Troy Pavlek
	 *
	 */
	public static enum LOCATIONS {
		CAB (53.526572, -113.524734), 
		CAMERON (53.52677, -113.523672), 
		CCIS (53.528243, -113.525657), 
		CSC (53.526808, -113.527127), 
		DEWEYS (53.526049, -113.523318), 
		ETLC (53.527382,  -113.529509), 
		HUB (53.526425, -113.520443), 
		RUTHERFORD (53.525896, -113.52172), 
		STJOSPEH (53.524486, -113.524541), 
		SUB (53.525322, -113.52732), 
		TORY (53.528185,  -113.521462);
		
		private double lat;
		private double lon;
		private LOCATIONS(double latt, double lonn) {
			lat = latt;
			lon = lonn;
		}
	}
	
	/**
	 * Creates a GeoLocation using the position of the device.
	 * @param context
	 */
	public GeoLocation(Context context) {
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		// Can't find location. They're in compsci
		if (location == null) {
			this.lon = GeoLocation.LOCATIONS.CSC.lon;
			this.lat = GeoLocation.LOCATIONS.CSC.lat;
		} else {
			this.lon = location.getLongitude();
			this.lat = location.getLatitude();
		}
	}
	
	/**
	 * Constructor for instantiating default locations.
	 * <p>
	 * Example Usage: GeoLoation loc1 = new GeoLocation(GeoLocation.CSC);
	 * </p>
	 * @param pointNo
	 */
	public GeoLocation(GeoLocation.LOCATIONS pointNo) {
		this.lat = pointNo.lat;
		this.lon = pointNo.lon;
	}
	
	/**
	 * Creates a GeoLocation using a input latitude and longitude.
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
	
	/**
	 * Finds the distance from current location to a specified GeoLocation.
	 * 
	 * Credit: base code for the haversine formula here: http://www.movable-type.co.uk/scripts/latlong.html
	 * 
	 * @param loc1
	 * @return the distance as a double
	 */
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
	
	/**
	 * Returns GeoLocation model as a json object.
	 * @return The GeoLocation as a json object.
	 */
	public String toJSON() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
	
	/**
	 * Returns a GeoLocation model from a Json object.
	 * @param text
	 * @return GeoLocation model
	 */
	public static GeoLocation fromJSON(String text) {
		Gson gson = new Gson();
		GeoLocation new_model = gson.fromJson(text, GeoLocation.class);
		return new_model;
	}

}
