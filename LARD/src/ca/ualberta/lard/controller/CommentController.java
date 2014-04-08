package ca.ualberta.lard.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import ca.ualberta.lard.comparator.CreationDateComparator;
import ca.ualberta.lard.comparator.LocationComparator;
import ca.ualberta.lard.comparator.PictureComparator;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;
import ca.ualberta.lard.model.Favourites;
import ca.ualberta.lard.model.Follow;
import ca.ualberta.lard.model.GeoLocation;

/**
 * 
 * @author Troy Pavlek
 *
 */
public class CommentController {
	// Needed for a variety of tasks
	private Context context;
	// The buffer of loaded comments from the server
	private ArrayList<Comment> buffer;
	
	/**
	 * Initializes CommentController's buffer using a commentRequest
	 * @param req CommentRequest the comments we want
	 */
	public void init(CommentRequest req) {
		buffer = DataModel.retrieveComments(req);
	}
	
	/**
	 * Initialize with Context. This will work, even if the server is down (but has overhead of managing contexts)
	 * @param req CommmentRequest the comments we want
	 * @param context Context Application context
	 */
	public void init(CommentRequest req, Context context) {
		buffer = DataModel.retrieveComments(req, context);
	}
	
	/**
	 * Constructor for when we only need to sort, not retrieve comments.
	 */
	public CommentController() {
	}
	
	/**
	 * Constructor with context (Gets comments without specification)
	 * @param context Context Application context
	 */
	public CommentController(Context context) {
		CommentRequest req = new CommentRequest(10);
		req.setLocation(new GeoLocation(context));
		this.context = context;
		init(req, context);
	}
	
	/**
	 * Constructor with a specific request.
	 * @param req CommentRequest the comments we want
	 * @param context Context Application context
	 */
	public CommentController(CommentRequest req, Context context) {
		this.context = context;
		init(req, context);
	}
	
	/**
	 * Requests more comments.
	 * @param req CommentRequest The comments we want
	 * @return CommentController Itself, for chaining
	 */
	public CommentController request(CommentRequest req) {
		init(req);
		return this;
	}
	
	/**
	 * Creates a comment and saves it.
	 * @param comment The comment object that is to be created.
	 * @return boolean Did it succeed?
	 */
	public static boolean createComment(Comment comment) {
		return DataModel.save(comment);
	}
	
	/**
	 * Updates a comment.
	 * @param comment The updated version of the comment
	 * @return boolean did it work?
	 */
	public static boolean update(Comment comment) {
		return DataModel.update(comment);
	}
	
	/**
	 * Are there any comments in the buffer?
	 * @return boolean Are there comments in the buffer?
	 */
	public boolean isEmpty() {
		return (buffer == null || buffer.size() == 0);
	}
	
	/**
	 * Sorts a given list of comments by creation date. If an empty list is provided as the
	 * list to be sorted, an empty list is returned.
	 * @param comments The list of comments to be sorted
	 * @return The list of comments sorted by creation date
	 */
	public ArrayList<Comment> sortByCreationDate(ArrayList<Comment> comments) {
		// Check the list has at least 2 elements
		if (comments.size() < 2) {
			return comments;
		}
		Collections.sort(comments, new CreationDateComparator());
		return comments;
	}
	
	/**
	 * Sorts a given list of comments by pictures first. If an empty list is provided as the
	 * list to be sorted, an empty list is returned.
	 * @param comments The list of comments to be sorted.
	 * @return The list of comments sorted by pictures first.
	 */
	public ArrayList<Comment> sortPicturesFirst(ArrayList<Comment> comments) {
		// Check the list has at least 2 elements
		if (comments.size() < 2) {
			return comments;
		}
		Collections.sort(comments, new PictureComparator());
		return comments;
	}
	
	/**
	 * Sorts a given list of comments by a specified location. If an empty list is provided as the
	 * list to be sorted, an empty list is returned.
	 * @param comments The list of comments to be sorted
	 * @param location The location to sort in proximity to
	 * @return The list of comments sorted by provided location
	 */
	public ArrayList<Comment> sortByLocation(ArrayList<Comment> comments, GeoLocation location) {
		// Check the list has at least 2 elements
		if (comments.size() < 2) {
			return comments;
		}
		// Create a list of (distance from current location, comment) pairs.
		ArrayList<Pair<Double, Comment>> pairs = new ArrayList<Pair<Double, Comment>>();
		for(Comment comment: comments) {
			Double distance = location.distanceFrom(comment.getLocation());
			pairs.add(Pair.create(distance, comment));
		}
		Collections.sort(pairs, new LocationComparator());
		comments = new ArrayList<Comment>();
		for(Pair<Double, Comment> pair: pairs) {
			comments.add(pair.second);
		}
		return comments;	
	}
	
	/**
	 * Gets the comments in the buffer
	 * @return ArrayList<Comment> comments in the buffer
	 */
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
	
	/**
	 * Favourites a comment (adds it to the favourites list, and saves the comment and its children to disk)
	 * @param comment Comment we want to favourite
	 */
	public void favourite(Comment comment) {
		Favourites favourites = this.getFavourites();
		favourites.addFavourite(comment.getId());
		DataModel.saveLocal(comment, true, this.context, true);
	}
	
	/**
	 * Saves a comment to local storage
	 * @param comment Comment the comment we want to save
	 * @return boolean Did it work?
	 */
	public boolean paper(Comment comment) {
		DataModel.saveLocal(comment, true, context, false);
		return true;
	}
	
	/**
	 * Gets a favourites object, representing our favourites
	 * @return Favourites Object with our favourites
	 */
	public Favourites getFavourites() {
		SharedPreferences prefs = context.getSharedPreferences(Favourites.PREFS_NAME, Context.MODE_PRIVATE);
		if (prefs == null) {
			System.err.println("Unable to retrieve shared preferences");
		}
		return new Favourites(prefs);
	}
	
	/**
	 * Gets a Follow object, representing the users that we follow
	 * @return Follow the Users  that we follow
	 */
	public Follow getFollows() {
		SharedPreferences prefs = context.getSharedPreferences(Follow.PREFS_NAME, Context.MODE_PRIVATE);
		if (prefs == null) {
			System.err.println("Unable to retrieve shared preferences");
		}
		return new Follow(prefs);
	}
	
	/**
	 * Selects our favourite comments out of the buffer
	 * @return ArrayList<Comment> The favourites in the buffer
	 */
	public ArrayList<Comment> getFavouriteComments() {
		ArrayList<Comment> favoriteBuffer = new ArrayList<Comment>();
		Set<String> favIDSet = this.getFavourites().getFavouritesList();
		for (Comment bufferComment: buffer) {
			if(favIDSet.contains(bufferComment.getId())) {
				favoriteBuffer.add(bufferComment);
			}
		}
		return favoriteBuffer;
	}
	
	/**
	 * Selects our followed Comments out of the buffer
	 * @return ArrayList<Comment> our followed comments in the buffer
	 */
	public ArrayList<Comment> getFollowedComments() {
		ArrayList<Comment> followedBuffer = new ArrayList<Comment>();
		Set<String> followedIDSet = this.getFollows().getFollowed();
		for (Comment bufferComment : buffer) {
			if(followedIDSet.contains(bufferComment.getAuthor())) {
				followedBuffer.add(bufferComment);
			}
		}
		
		return followedBuffer;
	}

}
