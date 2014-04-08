package ca.ualberta.lard.Stretchy;

import org.apache.http.HttpResponse;
import com.google.gson.Gson;
import ca.ualberta.lard.utility.File;

/**
 * An object to encapsulate the response given by requests to Elastic Search.
 * <p>
 * Only used for requests that don't return relevant data. Eg, a search will not return a StretchyResponse, but saving which doesn't return "Results" will
 * return a StretchyResponse, allowing the user to query data about the success of his REST call.
 * </p>
 * 
 * @author Troy Pavlek
 *
 */
public class StretchyResponse {
	// Status of the request
	private boolean ok;
	// ID of object we requested
	private String _id;
	
	// Unused, but necessary for proper deserialization of the response from Elastic Search
	@SuppressWarnings("unused")
	private int _version;
	@SuppressWarnings("unused")
	private String _type;
	@SuppressWarnings("unused")
	private String _index;
	
	public StretchyResponse(boolean status, String id) {
		this.ok = status;
		this._id = id;
	}
	
	/**
	 * Factory to create a StrechyResponse given an HttpResponse
	 * @param response HttpResponse received from the server.
	 * @return StretchyResponse that was constructed.
	 */
	public static StretchyResponse create(HttpResponse response) {
		Gson gson = new Gson();
		return gson.fromJson(File.parseJsonFromResponse(response), StretchyResponse.class);
	}
	
	/**
	 * Was the response OK?
	 * @return boolean request success
	 */
	public boolean ok() {
		return this.ok;
	}
	
	/**
	 * Retrieve the id of the object that StrechyClient just created for us.
	 * @return String id of the object saved to the server.
	 */
	public String getId() {
		return this._id;
	}

}
