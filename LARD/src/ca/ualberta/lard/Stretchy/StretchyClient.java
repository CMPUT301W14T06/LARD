package ca.ualberta.lard.Stretchy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;
import ca.ualberta.lard.model.Comment;

import com.google.gson.Gson;

/**
 * The StrechyClient is a barebones REST client for Elastic Search. While some already exists, getting maven working was a good deal
 * harder than just writing one myself.
 * @author Troy Pavlek
 *
 */
public class StretchyClient {
	private static final String ES_LOCATION = "http://cmput301.softwareprocess.es:8080/cmput301w14t06/comment/";
	private Gson gson;
	private HttpClient client;
	private static final int NETWORKTIMEOUT = 5000; // Time in milliseconds to wait for the requested comments
	public StretchyClient() {
		gson = new Gson();
		client = new DefaultHttpClient();
	}
	
	/**
	 * Gets a particular comment by its ID.
	 * <p>
	 * Queries Elastic Search for a resource of type comment matching the provided ID.
	 * </p>
	 * @param id ID of the Comment the client is searching for
	 * @return Comment if found, null otherwise
	 */
	public Comment getById(final String id) {
		class RunGet implements Runnable {
			private Comment foundComment = null;
			@Override
			public void run() {
				HttpGet getReq = new HttpGet(ES_LOCATION + id + "?pretty=1");
				getReq.addHeader("Accept", "application/json");
				try {
					HttpResponse response = client.execute(getReq);

					StretchyResult<Comment> sResult = StretchyResult.create(response);
					if (sResult.exists()) {
						foundComment = sResult.getSource();
						//return sResult.getSource();
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// We've come too far, the Comment doesn't exist
			}
			public Comment get() {
				Long curtime = System.currentTimeMillis();
				while (foundComment == null) {
					if (System.currentTimeMillis() - curtime > NETWORKTIMEOUT) {
						break;
					}
				}
				return foundComment;
			}
		}
		RunGet s = new RunGet();
		try {
			new Thread(s).start();
		} catch (Exception e) {
			Log.d("HELP", "PLZ HALP");
			e.printStackTrace();
		}

		return s.get();
	}

	
	/**
	 * Saves a Comment to Elastic Search.
	 * @param comment The comment that the user wishes to save over the network
	 * @return StretchyResponse containing the server's response.
	 */
	public StretchyResponse save(Comment comment) {
		HttpPost postReq = new HttpPost(ES_LOCATION);
		
		StringEntity json = null;
		try { 
			json = new StringEntity(gson.toJson(comment));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		postReq.setHeader("Accept", "application/json");
		
		postReq.setEntity(json);
		
		HttpResponse response = null;
		try {
			response = client.execute(postReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return StretchyResponse.create(response);
	}
	
	public ArrayList<Comment> search(final SearchRequest req) {
		class RunSearch implements Runnable {
			private volatile ArrayList<Comment> returnComments;

			@Override
			public synchronized void run() {
				HttpPost postReq = new HttpPost(ES_LOCATION + "_search?pretty=1");

				StringEntity json = null;
				try {
					json = new StringEntity(req.toString());
				} catch (UnsupportedEncodingException ex) {
					ex.printStackTrace(); // TODO
				}

				postReq.setHeader("Accept", "application/json");
				postReq.setEntity(json);

				HttpResponse response = null;
				try { 
					response = client.execute(postReq);
				} catch (ClientProtocolException e) {
					e.printStackTrace(); // TODO
				} catch (IOException e) {
					e.printStackTrace();
				}

				StretchySearchResult<Comment> result = StretchySearchResult.create(response);
				if (result.timedOut()) {
					// TODO
					//throw new IOException("Timed out");
				}
				returnComments = result.hits().get();
			}
			public ArrayList<Comment> get() {
				Long curtime = System.currentTimeMillis();
				while (returnComments == null) {
					if (System.currentTimeMillis() - curtime > NETWORKTIMEOUT) {
						break;
					}
				}
				return returnComments;
			}
		}
		RunSearch s = new RunSearch();
		try {
			new Thread(s).start();
		} catch (Exception e) {
			Log.d("HELP", "PLZ HALP");
			e.printStackTrace();
		}

		return s.get();
	}
	
	

}
