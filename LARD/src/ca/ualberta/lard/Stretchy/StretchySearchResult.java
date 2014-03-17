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
 * @param <T> The type of result we're expecting from the search. Should be a comment in all current implementations
 */
public class StretchySearchResult<T> {
	protected int took;
	protected boolean timed_out;
	protected StretchySearchHits<T> hits;
	
	/**
	 * Factory to create a StretchySearchResult of Comments from an HttpResponse
	 * @param response HttpResponse containing the body of the search
	 * @return StretchySearchResult<Comment> object containing the results of the search.
	 */
	public static StretchySearchResult<Comment> create(HttpResponse response) {
		Gson gson = new Gson();
		Type type = new TypeToken<StretchySearchResult<Comment>>(){}.getType();
		return gson.fromJson(File.parseJsonFromResponse(response), type);
	}
	
	/**
	 * Did the request time out? Should we retry?
	 * @return boolean
	 */
	public boolean timedOut() {
		return this.timed_out;
	}
	
	/**
	 * Getter for the hits on the search.
	 * @return A StretchySearchHits object containing the list of hits of type T
	 */
	public StretchySearchHits<T> hits() {
		return this.hits;
	}
	
	/**
	 * Object to encapsulate the hits of a StretchySearchResult. Implemented as a direct mapping of the default elastic search hierarchy to allow
	 * for one-to-one JSON deserialization.
	 * @author Troy Pavlek
	 *
	 * @param <T> Type of the object recieved
	 */
	@SuppressWarnings("hiding") // TODO: Something?
	public class StretchySearchHits<T> {
		protected int total;
		protected double max_score;
		protected ArrayList<StretchyResult<T>> hits;
		
		/**
		 * The number of hits.
		 * @return int number of his
		 */
		public int total() {
			return this.total;
		}
		
		/**
		 * The relevancy of the best hit - definied by Elastic Search
		 * @return double max_score.
		 */
		public double maxScore() {
			return this.max_score;
		}
		
		/**
		 * Getter for retrieving all StrechyResults that the search rerturned. StretchyResults can be used to query
		 * metadata about each results. If you only care about retrieving the actual Object (eg Comment) that each search
		 * located, use .get() instead.
		 * @return ArrayList<StretchyResult<T>> containing a StretchyResult for each T that the search located
		 */
		public ArrayList<StretchyResult<T>> results() {
			return this.hits;
		}
		
		/**
		 * Gets the actual object representation of each successful hit located by ElasticSearch
		 * @return ArrayList<T> of each object found
		 */
		public ArrayList<T> get() {
			ArrayList<T> arr = new ArrayList<T>();
			for (StretchyResult<T> result : this.results()) {
				arr.add(result.getSource());
			}
			return arr;
		}
	}
}
