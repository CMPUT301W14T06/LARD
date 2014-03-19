package ca.ualberta.lard.model;

import java.util.ArrayList;

/**
 * SortRequest specifies an ArrayList of Comments should be sorted. It must be provided an ArrayList of 
 * Comments that are to be sorted and <b>exactly one</b> of the sort options should be set.
 * Comment controller checks the search request fields in a specific order, so if more than 1 option
 * is selected, it will be sorted only once. 
 * @author Victoria
 */

public class SortRequest {

	private ArrayList<Comment> comments;
	private boolean byPicturesFirst;
	private boolean byCreationDate;
	private boolean byCurrentLocation;
	private boolean bySpecificLocation;
	private GeoLocation specificLocation;
	
	/**
	 * Initially all fields are set to false;
	 * @param comments: The Comments to be sorted
	 */
	public SortRequest(ArrayList<Comment> comments) {
		super();
		this.comments = comments;
		this.byPicturesFirst = false;
		this.byCreationDate = false;
		this.byCurrentLocation = false;
		this.bySpecificLocation = false;
		this.specificLocation = null;
	}

	/**
	 * Requests pictures appear first.
	 */
	public void setByPicturesFirst() {
		this.byPicturesFirst = true;
	}

	/**
	 * Requests comments are sorted by created date.
	 */
	public void setByCreationDate() {
		this.byCreationDate = true;
	}
	
	/**
	 * Requests comments are sorted by the users current location.
	 */
	public void setByCurrentLocation() {
		this.byCurrentLocation = true;
	}

	/**
	 * Requests comments are sorted by a specific location.
	 * @param location The GeoLocation to sort in proximity to.
	 */
	public void setBySpecificLocation(GeoLocation location) {
		this.bySpecificLocation = true;
		this.specificLocation = location;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public boolean isByPicturesFirst() {
		return byPicturesFirst;
	}

	public boolean isByCreationDate() {
		return byCreationDate;
	}

	public boolean isByCurrentLocation() {
		return byCurrentLocation;
	}

	public boolean isBySpecificLocation() {
		return bySpecificLocation;
	}

	public GeoLocation getSpecificLocation() {
		return specificLocation;
	}		
}
