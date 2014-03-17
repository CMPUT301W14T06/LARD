package ca.ualberta.lard.model;

import java.util.HashMap;
import java.util.Map;

import android.util.Pair;

/**
 * A GeoLocation map of specific latitude, longitude pairs of locations on
 * the University of Alberta campus.
 */
public class GeoLocationMap {
	
	Map<String, Pair<Double, Double>> geoDict = new HashMap<String,  Pair<Double, Double>>();
	
	/**
	 * Populates the GeoLocationMap's Hashmap with locations from 
	 * Univeristy of Alberta and their latitude and longitude 
	 */
	public GeoLocationMap() {
		super();
		//populates GeoLocationMap
		Pair<Double, Double> CAB = new Pair<Double, Double>(53.526572, -113.524734);
		Pair<Double, Double> CAMERON = new Pair<Double, Double>(53.52677, -113.523672);
		Pair<Double, Double> CCIS = new Pair<Double, Double>(53.528243, -113.525657);
		Pair<Double, Double> CSC = new Pair<Double, Double>(53.526808, -113.527127);
		Pair<Double, Double> DEWEYS = new Pair<Double, Double>(53.526049, -113.523318);
		Pair<Double, Double> ETLC = new Pair<Double, Double>(53.527382,  -113.529509);
		Pair<Double, Double> HUB = new Pair<Double, Double>(53.526425, -113.520443);
		Pair<Double, Double> RUTHERFORD = new Pair<Double, Double>(53.525896, -113.52172);
		Pair<Double, Double> STJOSPEH = new Pair<Double, Double>(53.524486, -113.524541);
		Pair<Double, Double> SUB = new Pair<Double, Double>(53.525322, -113.52732);
		Pair<Double, Double> TORY = new Pair<Double, Double>(53.528185,  -113.521462);
		geoDict.put("CAB", CAB);
		geoDict.put("CAMERON", CAMERON);
		geoDict.put("CCIS", CCIS);
		geoDict.put("CSC", CSC);
		geoDict.put("DEWEYS", DEWEYS);
		geoDict.put("ETLC", ETLC);
		geoDict.put("HUB", HUB);
		geoDict.put("RUTHERFORD", RUTHERFORD);
		geoDict.put("STJOSPEH", STJOSPEH);
		geoDict.put("SUB", SUB);
		geoDict.put("TORY", TORY);	
	}
	
	/**
	 * returns the HashMap Attribute
	 * @return GeoLocationMap's HashMap
	 */
	
	public Map<String, Pair<Double, Double>> getMap() {
		return geoDict;
	}	
}
