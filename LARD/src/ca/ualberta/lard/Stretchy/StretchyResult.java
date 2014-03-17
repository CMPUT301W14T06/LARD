package ca.ualberta.lard.Stretchy;

import java.lang.reflect.Type;
import org.apache.http.HttpResponse;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.utility.File;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Container for the result of a single Elastic Search request - eg a particular ID.
 * A one-to-one mapping of the JSON Results from Elastic Search
 * @author Troy Pavlek
 *
 * @param <T> Type of result we're expecting
 */
public class StretchyResult<T> {
	private boolean exists;
	private T _source;
	
	@SuppressWarnings("unused") // TODO: Remove
	private String _index;
	@SuppressWarnings("unused") // TODO: Remove
	private String _type;
	@SuppressWarnings("unused") // TODO: Remove
	private String _id;
	@SuppressWarnings("unused") // TODO: Remove
	private int _version;
	
	
	/**
	 * Factory to create a stretchyResult<Comment> given a particular HTTPResponse.
	 * @param response HttpResponse containing the server's result.
	 * @return StretchyResult<Comment> containing an object with all result metadata.
	 */
	public static StretchyResult<Comment> create(HttpResponse response) {
		Gson gson = new Gson();
		Type type = new TypeToken<StretchyResult<Comment>>(){}.getType();
		return gson.fromJson(File.parseJsonFromResponse(response), type);
	}
	
	/**
	 * Getter to return whether or not the entity exists on the server
	 * @return boolean Does the entity exist?
	 */
	public boolean exists() {
		return this.exists;
	}
	
	/**
	 * Gets the object retrieved from the server.
	 * @return T deserialized object from the response.
	 */
	public T getSource() {
		return this._source;
	}

}
