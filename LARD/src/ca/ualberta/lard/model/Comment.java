package ca.ualberta.lard.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * An instance of Comment represents a comment a user creates to share
 * her thoughts about a particular location. Every comment has a generated
 * id, a GeoLocation, an author, the date it was created, the most recent
 * date it was updated, and a body text. It may or may not have a picture.
 * 
 * The CommentController can be used to retrieve lists of Comments in a sorted order
 * and the DataModel can be used to save a Comment. Comments can use to DataModel to
 * request information about it, such as if it has children or if it is saved locally.
 * @author Victoria
 */

public class Comment {
	
	private String id;
	private String bodyText;
	private Date createdAt;
	private Date updatedAt;
	private String author;
	private GeoLocation location;
	private Picture picture;
	
	/**
	 * Lazy-loaded members. Uninstantiated, until a method requires the data.
	 */
	private transient Integer numReplies;
	private transient ArrayList<Comment> children;
	
	// Context is transient because it is required at runtime, but is not relevant for serialization
	private transient Context context;
	/**
	 * ID of parent comment element.
	 */
	private String parent;
	
	/**
	 * General initialization code for a comment
	 * <p>
	 * Initializes code common to the multiple Comment constructors.
	 * </p>
	 * @param c The Comment object to populate
	 * @param body BodyText of the Comment
	 * @param context Application context of the current Comment Creation request
	 */
	public void init(Comment c, String body, Context context) {
		this.context = context;
		c.createdAt = new Date();
		c.updatedAt = new Date();
		this.setAuthor(null);
		this.bodyText = body;
		//c.location = new GeoLocation(context);
		c.location = new GeoLocation(53.525896, -113.52172);
		c.id = UUID.randomUUID().toString();
		c.picture = null;
		c.parent = null;
	}
	
	/* Minimal constructor, contains only body text */
	public Comment(String body, Context context) {
		this.init(this, body, context);
	}
	
	/* Constructor for creating replies */
	public Comment (String body, String parentID, Context context) {
		this.init(this, body, context);
		this.parent = parentID;
	}
	
	public String toString() {
		return this.bodyText;
	}
	
	// Getters
	
	public String getId() {
		return this.id;
	}
	
	public String getBodyText() {
		return this.bodyText;
	}
	
	public Date getCreatedDate() {
		return this.createdAt;
	}
	
	public Date getUpdatedDate() {
		return this.updatedAt;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getRawAuthor() {
		return this.author.split("#")[0];
	}
	
	public GeoLocation getLocation() {
		return this.location;
	}
	
	public Picture getPicture() {
		return this.picture;
	}
	
	/**
	 * Returns null if the comment does not have a parent, so the 
	 * parent comment cannot be retrieved. Otherwise, the parent comment
	 * is returned.
	 * @return null or the parent Comment
	 */
	public Comment getParent() {
		// Check the comment has a parent first.
		if (this.parent == null) {
			return null;
		}		
		CommentRequest req = new CommentRequest(1);
		req.setId(this.parent);
		ArrayList<Comment> arr = DataModel.retrieveComments(req);
		
		if (arr.size() > 0) {
			return arr.get(0);
		}		
		return null;
	}
	
	/**
	 * Returns null is a comment has no children. Otherwise returns the 
	 * children in a ArrayList. Children should be sorted by date created.
	 * @return null or ArrayList of children
	 */
	public ArrayList<Comment> children() {
		if (this.children != null) {
			return this.children;
		}
		CommentRequest req = new CommentRequest(100);
		req.setParentId(this.id);
		// TODO: Children should be sorted by date of creation.
		this.children  = DataModel.retrieveComments(req);
		
		return this.children;
	}
	
	// Setters
	
	/**
	 * Sets the text of the comment. If an empty input is given the text will
	 * be set to [Comment Text Removed] by default. 
	 * @param bodyText
	 */
	public void setBodyText(String bodyText) {
		// Check the body text is not empty.
		if (bodyText == "" || bodyText == null) {
			bodyText = "[Comment Text Removed]";
		}
		this.bodyText = bodyText;
		this.setUpdated();
	}
	
	/**
	 * Sets the author of the comment. The author will be the user name appended
	 * with a hash unique to the device being used. If an empty user name or no name
	 * is given author will be set to Anonymous by default.
	 * @param username
	 */
	public Comment setAuthor(String username) {
		// Check the username is not empty. If it is set to Anonymous.
		
		User user = new User(this.context.getSharedPreferences(User.PREFS_NAME, Context.MODE_PRIVATE));
		
		if (username != null && !username.isEmpty()) {
			user.setUsername(username);
		}
		this.author = user.getUsername();
		this.setUpdated();
		return this;
	}
	
	public void setLocation(GeoLocation location) {
		this.location = location;
		this.setUpdated();
	}
	
	public void setPicture(Picture picture) {
		this.picture = picture;
		this.setUpdated();
	}
	
	public void setParent(String parentID) {
		this.parent = parentID;
		this.setUpdated();
	}
	
	// For testing comparators
	public void setCreationDate(Date date) {
		this.createdAt = date;
	}
	
	public Comment setContext(Context context) {
		this.context = context;
		return this;
	}
	
	
	// Helper functions
	
	public boolean hasParent() {
		return (this.parent != null);
	}
	
	/**
	 * Touches the Comment's updatedAt timestamp.
	 * @return Comment The current comment for chaining.
	 */
	public Comment setUpdated() {
		this.updatedAt = new Date();
		return this;
	}
	
	public boolean hasPicture() {
		if (this.picture == null || this.picture.isNull()) {
			return false;
		} else if (this.picture.getImageString() == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns an integer. The number of children is 0 if the list
	 * of children is null. Otherwise the number of elements in the list
	 * of children is returned.
	 * @return int Number of children 
	 */
	public int numReplies() {
		if (this.numReplies == null) {
			this.numReplies = this.children().size();
		}
		return this.numReplies;
	}
	
	/**
	 * Returns true if the comment is saved locally and false if
	 * it is not.
	 * @return true or false
	 */
	public boolean isLocal() {
		return DataModel.isLocal(this, context);
	}
	
	/**
	 * Checks whether the id's of 2 comments are the same. This is
	 * used by DataModel to decide if a comment has been saved locally
	 * but might have had it's fields edited.
	 * @param comment
	 * @return true if IDs are same, false otherwise
	 */
	public boolean equals (Comment comment) {
		if (this == comment)
			return true;
		if (comment == null)
			return false;
		if (comment.getId().equals(this.getId())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Converts a comment to a json string.
	 * @return Comment as a json string
	 */
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	/**
	 * Converts a json string back to a comment.
	 * @param json Comment as a string
	 * @return Comment
	 */
	public static Comment fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Comment.class);
	}
}

