package ca.ualberta.lard.Stretchy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
	/**
	 * Hardcoded location of the strechy client repository. We only work with objects of type Comment.
	 */
	private static final String ES_LOCATION = "http://cmput301.softwareprocess.es:8080/cmput301w14t06/comment/";
	private Gson gson;
	private HttpClient client;
	/**
	 * Time in milliseconds to wait for the requested comments
	 */
	private static final int NETWORKTIMEOUT = 5000;
	
	/**
	 * Constructor to intialize the Gson worker and the Apache HttpClient.
	 */
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
			e.printStackTrace();
		}

		return s.get();
	}

	
	/**
	 * Saves a Comment to Elastic Search.
	 * @param comment The comment that the user wishes to save over the network
	 * @return StretchyResponse containing the server's response.
	 */
	public StretchyResponse save(final Comment comment) {
		HttpPost postReq = new HttpPost(ES_LOCATION);
		return push(postReq, comment);
	}
	
	/**
	 * Updates a comment Elastic Search.
	 * @param comment The comment that the user wishes to save over the network
	 * @return StretchyResponse containing the server's response.
	 */
	public StretchyResponse update(final Comment comment) {
		HttpPut postReq = new HttpPut(ES_LOCATION + comment.getId() + "");
		return push(postReq, comment);
	}
	
	/**
	 * Pushes a comment to elastic search.
	 * @param postReq HttpPost A post request to the server.
	 * @param comment Comment The data payload.
	 * @return StretchyResponse Contains the server's response
	 */
	private StretchyResponse push(final HttpEntityEnclosingRequestBase postReq, final Comment comment) {
		class RunPush implements Runnable {
			private StretchyResponse sResponse = null;
			@Override
			public void run() {
				HttpEntityEnclosingRequestBase mutableReq = postReq;
				String json = "";
				
				// While rare, we experienced stackoverflows before, so it's always good to protect against them.
				try {
					json = gson.toJson(comment);
				} catch (StackOverflowError ex) {
					System.err.println("Major error in StretchyClient - Update: stackoverflow ");
					sResponse = new StretchyResponse(false, comment.getId());
					return;
				}
				
				mutableReq.setHeader("Accept", "application/json");
				StringEntity insert = null;
				try { 
					insert = new StringEntity(json);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				mutableReq.setEntity(insert);
				
				HttpResponse response = null;
				try {
					response = client.execute(mutableReq);
					sResponse = StretchyResponse.create(response);
					return;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			public StretchyResponse get() {
				Long curtime = System.currentTimeMillis();
				while (sResponse == null) {
					if (System.currentTimeMillis() - curtime > NETWORKTIMEOUT) {
						sResponse = new StretchyResponse(false, comment.getId());
						return sResponse;
					}
				}
				return sResponse;
			}
		}
			
		RunPush run = new RunPush();
		
		try {
			new Thread(run).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return run.get();
			
	}
	
	/**
	 * Search elastic search and find an ArrayList of Comments.
	 * @param req A SearchRequest whose fields specify the objects and values that the user wishes to search for
	 * @return ArrayList of Comments embodying the results. May be empty, which should be checked on the client side.
	 */
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
