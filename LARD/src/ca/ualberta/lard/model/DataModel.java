package ca.ualberta.lard.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Pair;
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
	 * @param comment Comment The comment object that is to be saved to disk.
	 * @param persisitent boolean Is this save explicit or is it to be put in the upload queue 
	 */
	public static void saveLocal(Comment comment, boolean persistent, Context context) {
		// Calls the saveLocal override using ArrayList
		ArrayList<Comment> comments = new ArrayList<Comment>();
		comments.add(comment);
		saveLocal(comments, persistent, context);
		return;
	}
	
	/**
	 * Saves an ArrayList of comments to local storage.
	 * @param comments ArrayList<Comment> The comments that we want to add to local storage
	 * @param persistent boolean Do we want this to be permanently stored locally, or just until we can push to a server?
	 * @param context Context Our android context
	 */
	public static void saveLocal(ArrayList<Comment> comments, boolean persistent, Context context) {
		ArrayList<Comment> localComments = readLocal(context);
		
		// We only want to add comments that are not saved locally already.
		Iterator<Comment> i = comments.iterator();
		while (i.hasNext()) {
		   Comment c = i.next();
		   if (localComments.contains(c)) {
			   i.remove();
		   }
		}
		
		// Add all new comments to the local array.
		localComments.addAll(comments);
		
		Gson gson = new Gson();
		String json = gson.toJson(localComments);
		
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
	 * Assumes you are passed a context.
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
	
	/**
	 * Saves a comment to stretchy client. Returns a boolean which specifies 
	 * if the save was successful or not.
	 * @param comment
	 * @return true if save was successful, otherwise false
	 */
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
	
	/**
	 * Checks if a comment is saved locally. Returns a boolean which specifies if it
	 * is saved locally or not.
	 * When determining if a comment is saved locally, only compare comment IDs since
	 * other fields may have been edited.
	 * @param comment
	 * @return true if saved locally, otherwise false
	 */
	public static boolean isLocal(Comment comment, Context context) {
		ArrayList<Comment> localComments = readLocal(context);
		for (Comment c: localComments) {
			return c.equals(comment);
		}
		return false;
	}

}
