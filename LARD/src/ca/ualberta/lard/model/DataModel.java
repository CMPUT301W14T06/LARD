package ca.ualberta.lard.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import ca.ualberta.lard.Stretchy.SearchRequest;
import ca.ualberta.lard.Stretchy.StretchyClient;
import ca.ualberta.lard.Stretchy.StretchyResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * DataModel is the data abstraction layer of the application. It handles the querying and retrieving of all comments for the applicaiton.
 * Most methods in DataModel will accept a CommentRequest structure that specifies the priorities for each comment request. Default
 * behaviour is always to query the internet before retrieving a local version of the comment.
 * 
 * DataModel can be accessed by both Models and controllers - controllers use DataModel to request data, and models use DataModel to request
 * information about their own state eg. is the comment stored locally?
 * @author Troy Pavlek
 *
 */
public class DataModel {
	// File to use for saving
	private static final String FILENAME = "Comments.sav";
	
	/**
	 * Saves a comment to local storage
	 * <p>
	 * We can either be saving as a temporary measure, and store the files in a queue to be uploaded when
	 * we next have an internet connection, or we could be explicitly saving a Favourite or a Paper.
	 * </p>
	 * @param comment The comment object that is to be saved to disk.
	 * @param persisitent Is this save explicit or is it to be put in the upload queue 
	 */
	public static void saveLocal(Comment comment, boolean persisitent, Context context) {
		ArrayList<Comment> comments = readLocal(context);
		
		// Serialization using Gson.
		Gson gson = new Gson();
		String json = gson.toJson(comments);
		
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(json);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * Reads a comment from local storage.
	 * Assume you are passed a context.
	 * Reading from local storage will only be used if stretchy comment returns null.
	 * @return
	 */
	public static ArrayList<Comment> readLocal(Context context) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		// Check if save file exists.
		File file = context.getFileStreamPath(FILENAME);
		if (!file.exists()) {
			// Get the file path and create a new file.
			String filePath = context.getFilesDir().getPath().toString()+FILENAME;
			file = new File(filePath);
			
			// Since the save file doesn't exist, there is nothing to read
			return comments;
		}
		try {
			// Retrieve json object from memory
			FileInputStream fis = context.openFileInput(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			String allCountersTemp = (String) ois.readObject();
			ois.close();
			fis.close();
			
			// Convert back to type ArrayList<Counter>
			Gson gson = new Gson();			
			Type listOfComments = new TypeToken<ArrayList<Comment>>(){}.getType();
			comments = gson.fromJson(allCountersTemp, listOfComments);
			return comments;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
			
		return comments;
	}
	
	public static boolean save(Comment comment) {
		StretchyClient client = new StretchyClient();
		StretchyResponse response = client.save(comment);
		return response.ok();
	}
	
	/**
	 * Retrieves comments requested by client code
	 * <p>
	 * Comments are retrieved based on best-effort given a particular CommentRequest object. Best-effort means this function may not always
	 * be precisely deterministic - different results will be returned based on network disconnects, or slow or decomissioned servers.
	 * The application will <em>not</em> attempt to continue to connect to servers that do not reply the first time, unless the user requests
	 * such.
	 * </p>
	 * @param req A CommentRequest object containing the parameters the user wishes to Search for.
	 * @return ArrayList of comments in the usual case. Worst case, no comments at all exist, and we will return null.
	 */
	public static ArrayList<Comment> retrieveComments(CommentRequest req) {
		StretchyClient client = new StretchyClient();
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		String id = req.getId();
		// If the comment request has an ID, we want to get that specific comment.
		if (id != null) {
			Comment c = client.getById(id);
			if (c != null) {
				comments.add(c);
				return comments;
			}
			// TODO check local storage as a last resort.
			return null; // We couldn't find the ID the user requested. We've failed. Pack up and go home.
		}
		System.err.println("ID NULL");
		
		SearchRequest sReq = new SearchRequest(req);
		System.err.println(sReq.toString());
		ArrayList<Comment> comments2 = client.search(sReq); // TODO finish
		return comments2;
	}
	
	public static boolean isLocal(Comment comment) {
		// TODO unimplemented
		return false;
	}

}
