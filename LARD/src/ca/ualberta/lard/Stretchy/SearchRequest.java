package ca.ualberta.lard.Stretchy;

import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.GeoLocation;

/**
 * Elastic search uses specific string literal formatting. SearchRequest
 * transforms the size, body text, and parent of a comment into the correct
 * Json String format.
 */

public class SearchRequest {
	
	private int _size;
	private String bodyText;
	private String parent;
	private GeoLocation location;
	
	public SearchRequest(int size) {
		this._size = size;
	}
	
	public SearchRequest(CommentRequest req) {
		this._size = req.size();
		this.bodyText = req.getBodyText();
		this.parent = req.getParentId();
		this.location = req.getLocation();
	}
	
	/**
	 * Turns information about the size, body text, and the
	 * parent of a comment into a Json String literal that is
	 * usable in elastic search. 
	 */
	public String toString() {
		String ret = 
			"{ "
			+ "\"size\" : " + this._size + ", "
			+ "\"query\" : { ";
		if (!this.anyQuery()) {
			ret += "\"match_all\" : { } ";
		} else if (this.bodyText != null) {
			ret += this.bodyString();
		} else if (this.parent != null) {
			ret += this.parentString();
		} else if (this.location != null) {
			ret += this.locationString();
		}

		ret += "} ";
		ret += "}";
		return ret;
	}
	
	public void bodyText(String text) {
		this.bodyText = text;
	}
	
	public void parent(String id) {
		this.parent = id;
	}
	
	/**
	 * Helper function for toString(). This is used if we want to search
	 * for bodytext or parent specifically.
	 * @return true if either bodyText or parent is set
	 */
	public boolean anyQuery() {
		return !(this.bodyText == null && this.parent == null && this.location == null);
	}
	
	/**
	 * This is a helper function for toString() that turns the 
	 * body text into a String literal for elastic search.
	 * @return the body text in the form of a string literal
	 */
	public String bodyString() {
		if (this.bodyText != null && !this.bodyText.isEmpty()) {
			return "\"term\" : { \"bodyText\" : \"" + this.bodyText + "\" } "; 
		}
		return "";
	}
	
	public String parentString() {
		if (this.parent != null && !this.parent.isEmpty()) {
			return "\"query_string\" : { \"query\" : \"" + this.parent + "\" , \"fields\": [\"parent\"] } ";
		}
		return "";
	}
	
	public String locationString() {
		if (this.location != null) {
			String searchString = "{ \"sort\" : [ { \"_geo_distance\" : { \"comment.location\" : { \"lat\" : " +
					Double.toString(location.getLatitude()) + " , \"lon\" : " +
					Double.toString(location.getLongitude()) +
					", \"order\" : \"asc\", \"unit\" : \"km\" } } ], \"query\" : { \"match_all\" : {} }";
			System.out.println(searchString);
			return searchString;
		}
		return "";
	}

}
