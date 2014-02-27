package ca.ualberta.lard.Stretchy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.utility.File;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Container for the result of a single Elastic Search request - eg a particular ID.
 * @author Troy Pavlek
 *
 * @param <T>
 */
public class StretchyResult<T> {
	private String _index;
	private String _type;
	private String _id;
	private int _version;
	private boolean exists;
	private T _source;
	
	public static StretchyResult<Comment> create(HttpResponse response) {
		Gson gson = new Gson();
		Type type = new TypeToken<StretchyResult<Comment>>(){}.getType();
		return gson.fromJson(File.parseJsonFromResponse(response), type);
	}
	
	public boolean exists() {
		return this.exists;
	}
	
	public T getSource() {
		return this._source;
	}

}
