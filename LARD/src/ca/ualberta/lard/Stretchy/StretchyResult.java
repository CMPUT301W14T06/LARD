package ca.ualberta.lard.Stretchy;

import java.lang.reflect.Type;
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
