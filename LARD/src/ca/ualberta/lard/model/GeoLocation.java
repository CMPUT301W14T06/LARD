package ca.ualberta.lard.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.google.gson.Gson;
import android.content.Context;
import android.location.Criteria;
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

	// Our internal list of default locations
	public ArrayList<GeoLocation> locations;


	/**
	 * Creates a GeoLocation using the position of the device.
	 * @param context Context Application context in which we are running.
	 */
	public GeoLocation(Context context) {
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, true);
		Location location = lm.getLastKnownLocation(provider);
		if (location == null) {
			location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		// Can't find location. We're going to put them in computing science, as this is a sensible default
		// for the domain of our application
		if (location == null) {
			this.lat = 53.526808 ;
			this.lon = -113.527127;
		} else {
			this.lon = location.getLongitude();
			this.lat = location.getLatitude();
		}
	}

	/**
	 * Creates a GeoLocation using a input latitude and longitude.
	 * @param lat double Latitude
	 * @param lon double Longitude
	 */
	public GeoLocation (double lat, double lon) {
		this.lon = lon;
		this.lat = lat;
	}

	//Getters
	public double getLatitude() {
		return this.lat;
	}

	public double getLongitude() {
		return this.lon;
	}

	//Setters
	//Return themselves for chaining

	public GeoLocation setLatitude(double lat) {
		this.lat = lat;
		return this;
	}

	public GeoLocation setLongitude(double lon) {
		this.lon = lon;
		return this;
	}

	/**
	 * Finds the distance from current location to a specified GeoLocation.
	 * 
	 * Credit: base code for the haversine formula here: http://www.movable-type.co.uk/scripts/latlong.html
	 * 
	 * @param loc1 GeoLocation The location we are comparing to.
	 * @return double Distance in meters from current location to specified location
	 */
	public double distanceFrom(GeoLocation loc1) {

		int earthRadius = 6371; // earth's radius in KM - constant in source code because we don't expect this to change - ever.
		double dLat = Math.toRadians(this.getLatitude() - loc1.getLatitude());
		double dLon = Math.toRadians(this.getLongitude() - loc1.getLongitude());
		double lat1 = Math.toRadians(loc1.getLatitude());
		double lat2 = Math.toRadians(this.getLatitude());

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.asin(Math.sqrt(a)); 
		double returnValue = earthRadius * c * 1000;
		return returnValue;
	}

	/**
	 * Returns GeoLocation model as a json object.
	 * @return String The GeoLocation as a json object.
	 */
	public String toJSON() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	/**
	 * Returns a GeoLocation model from a Json object.
	 * @param text String the JSON string we want to deserialize
	 * @return GeoLocation The GeoLocation object
	 */
	public static GeoLocation fromJSON(String text) {
		Gson gson = new Gson();
		GeoLocation new_model = gson.fromJson(text, GeoLocation.class);
		return new_model;
	}

	/**
	 * Overridden hashcode, to verify uniqueness of our GeoLocation
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Returns the distance between two GeoLocations rounded to two
	 * decimal places as a string.
	 * @param loc GeoLocation The GeoLocation to calculate distance from
	 * @return String of distance rounded to 2 decimal places
	 */
	public String roundedDistanceFrom(GeoLocation loc) {
		Double unrounded = this.distanceFrom(loc);
		DecimalFormat rounded = new DecimalFormat("#.##");
		return rounded.format(unrounded);
	}

	/**
	 * Override of equals, compares two GeoLocations
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoLocation other = (GeoLocation) obj;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
			return false;
		return true;
	}

}