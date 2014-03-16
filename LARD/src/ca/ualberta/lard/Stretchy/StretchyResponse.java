package ca.ualberta.lard.Stretchy;

import org.apache.http.HttpResponse;
import com.google.gson.Gson;
import ca.ualberta.lard.utility.File;

public class StretchyResponse {
	private boolean ok;
	private String _id;
	
	@SuppressWarnings("unused") // TODO: Remove
	private int _version;
	@SuppressWarnings("unused") // TODO: Remove
	private String _type;
	@SuppressWarnings("unused") // TODO: Remove
	private String _index;
	
	public static StretchyResponse create(HttpResponse response) {
		Gson gson = new Gson();
		return gson.fromJson(File.parseJsonFromResponse(response), StretchyResponse.class);
	}
	
	public boolean ok() {
		return this.ok;
	}
	
	public String getId() {
		return this._id;
	}

}
