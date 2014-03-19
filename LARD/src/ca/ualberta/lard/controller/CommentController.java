package ca.ualberta.lard.controller;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.util.Pair;
import ca.ualberta.lard.comparator.CreationDateComparator;
import ca.ualberta.lard.comparator.LocationComparator;
import ca.ualberta.lard.comparator.PictureComparator;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.SortRequest;

/**
 * 
 * @author Troy Pavlek
 *
 */
public class CommentController {
	@SuppressWarnings("unused") // TODO: Remove
	private static Context context; // Needed for saveLocal
	private ArrayList<Comment> buffer;
	
	/**
	 * 
	 * @param req
	 */
	public void init(CommentRequest req) {
		buffer = DataModel.retrieveComments(req);
	}
	
	/**
	 * 
	 * @param context
	 */
	public CommentController(Context context) {
		CommentRequest req = new CommentRequest(10); //TODO pull this from configuration.
		req.setLocation(new GeoLocation(context));
		CommentController.context = context;
		init(req);
	}
	
	/**
	 * 
	 * @param req
	 */
	public CommentController(CommentRequest req) {
		init(req);
	}
	
	public CommentController request(CommentRequest req) {
		init(req);
		return this;
	}
	
	/**
	 * Creates a comment and saves it to disk
	 * </p>
	 * @param comment The comment object that is to be created.
	 */
	public static void createComment(Comment comment) {
		DataModel.save(comment);
	}
	
	public boolean any() {
		return buffer.size() > 0;
	}
	
	/**
	 * Sorts a given list of comments by a specified sort order. This information is contained
	 * in the SortRequest. Returns the sorted comments.
	 * @param req A SortRequest that contains the comments to be sorted and how to sort them.
	 * @return Comments in the sorted order
	 */
	public ArrayList<Comment> sort(SortRequest req) {
		ArrayList<Comment> comments = req.getComments();
		
		if (req.isByCreationDate()) {
			Collections.sort(comments, new CreationDateComparator());
		}
		else if (req.isByCurrentLocation()) {
			// Get the current location of the device
			GeoLocation curLoc = new GeoLocation(context);
			
			// Create a list of (distance from current location, comment) pairs.
			ArrayList<Pair<Double, Comment>> pairs = new ArrayList<Pair<Double, Comment>>();
			for(Comment comment: comments) {
				Double distance = curLoc.distanceFrom(comment.getLocation());
				pairs.add(Pair.create(distance, comment));
			}
			Collections.sort(pairs, new LocationComparator());
		}
		else if (req.isBySpecificLocation()) {
			// Create a list of (distance from current location, comment) pairs.
			ArrayList<Pair<Double, Comment>> pairs = new ArrayList<Pair<Double, Comment>>();
			for(Comment comment: comments) {
				Double distance = req.getSpecificLocation().distanceFrom(comment.getLocation());
				pairs.add(Pair.create(distance, comment));
			}
			Collections.sort(pairs, new LocationComparator());
		}
		else if (req.isByPicturesFirst()) {
			Collections.sort(comments, new PictureComparator());
		}
		else {
			// Pass, if we get in here it means no sorting option was selected.
			// Return the list as is.
		}
		return comments;
	}
	
	public ArrayList<Comment> get() {
		return buffer;
	}
	
	/**
	 * Returns the first comment in the CommentController
	 * <p>
	 * For use when the CommentController is known to only have one comment object in it
	 * @return A single Comment object
	 */
	public Comment getSingle() {
		return buffer.get(0);
	}

}
