package ca.ualberta.lard.Stretchy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

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
	
	public StretchyClient() {
		gson = new Gson();
		client = new DefaultHttpClient();
	}
	
	public Comment getById(String id) {
		HttpGet getReq = new HttpGet(ES_LOCATION + id + "?pretty=1");
		getReq.addHeader("Accept", "application/json");
		try {
		HttpResponse response = client.execute(getReq);
		
		StretchyResult<Comment> sResult = StretchyResult.create(response);
		if (sResult.exists()) {
			return sResult.getSource();
		}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// We've come too far, the Comment doesn't exist
		return null;
		
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
	
	

}
