package ca.ualberta.lard.Stretchy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.google.gson.Gson;

import ca.ualberta.lard.utility.File;

public class StretchyResponse {
	
	private boolean ok;
	private String _index;
	private String _type;
	private String _id;
	private int _version;
	
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
