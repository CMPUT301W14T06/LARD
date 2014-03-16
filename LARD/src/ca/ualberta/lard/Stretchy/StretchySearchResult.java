package ca.ualberta.lard.Stretchy;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.utility.File;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Container class for the results of an Elastic Search
 * @author Troy Pavlek
 *
 * @param <T>
 */
public class StretchySearchResult<T> {
	protected int took;
	protected boolean timed_out;
	protected StretchySearchHits<T> hits;
	
	public static StretchySearchResult<Comment> create(HttpResponse response) {
		Gson gson = new Gson();
		Type type = new TypeToken<StretchySearchResult<Comment>>(){}.getType();
		return gson.fromJson(File.parseJsonFromResponse(response), type);
	}
	
	public boolean timedOut() {
		return this.timed_out;
	}
	
	public StretchySearchHits<T> hits() {
		return this.hits;
	}
	
	@SuppressWarnings("hiding") // TODO: Something?
	public class StretchySearchHits<T> {
		protected int total;
		protected double max_score;
		protected ArrayList<StretchyResult<T>> hits;
		
		public int total() {
			return this.total;
		}
		
		public double maxScore() {
			return this.max_score;
		}
		
		public ArrayList<StretchyResult<T>> results() {
			return this.hits;
		}
		
		public ArrayList<T> get() {
			ArrayList<T> arr = new ArrayList<T>();
			for (StretchyResult<T> result : this.results()) {
				arr.add(result.getSource());
			}
			return arr;
		}
	}
}
