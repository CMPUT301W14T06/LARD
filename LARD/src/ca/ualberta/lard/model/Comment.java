package ca.ualberta.lard.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.content.Context;

import com.google.gson.Gson;

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
	 * @param c Comment The Comment object to populate
	 * @param body String BodyText of the Comment
	 * @param context Context Application context of the current Comment Creation request
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
	
	/**
	 * Constructor for creating a body given only the body text
	 * @param body String body text
	 * @param context Context Application context
	 */
	public Comment(String body, Context context) {
		this.init(this, body, context);
	}
	
	/**
	 * Constructor used for creating replies
	 * @param body String body text
	 * @param parentID String The ID of the comment we are replying to
	 * @param context Context application context
	 */
	public Comment (String body, String parentID, Context context) {
		this.init(this, body, context);
		this.parent = parentID;
	}
	
	/**
	 * Overridden toString
	 * @return String Body text of comment
	 */
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
	
	/**
	 * Get the author name without any hashing appended to it
	 * @return String Author name without any hash.
	 */
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
	 * Get the parent comment of the current object.
	 * @return null or the parent Comment
	 */
	public Comment getParent() {
		// Check the comment has a parent first.
		if (this.parent == null) {
			return null;
		}		
		CommentRequest req = new CommentRequest(1);
		req.setId(this.parent);
		ArrayList<Comment> arr = DataModel.retrieveComments(req, context);
		
		if (arr.size() > 0) {
			return arr.get(0);
		}		
		return null;
	}
	
	/**
	 * Get the ID of the parent of this comment
	 * @return String parent id
	 */
	public String getParentId() {
		return parent;
	}
	
	/**
	 * Get the children of the current comment object.
	 * Children should be sorted by date created.
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
	// By default all setters retrun themselves for chaining.
	/**
	 * Sets the text of the comment. If an empty input is given the text will
	 * be set to [Comment Text Removed] by default. 
	 * @param bodyText
	 */
	public Comment setBodyText(String bodyText) {
		// Check the body text is not empty.
		if (bodyText == "" || bodyText == null) {
			bodyText = "[Comment Text Removed]";
		}
		this.bodyText = bodyText;
		this.setUpdated();
		return this;
	}
	
	/**
	 * Sets the author of the comment. The author will be the user name appended
	 * with a hash unique to the device being used. If an empty user name or no name
	 * is given author will be set to Anonymous by default.
	 * @param username String the username we want to set as the author.
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
	
	public Comment setLocation(GeoLocation location) {
		this.location = location;
		this.setUpdated();
		return this;
	}
	
	public Comment setPicture(Picture picture) {
		this.picture = picture;
		this.setUpdated();
		return this;
	}
	
	public Comment setParent(String parentID) {
		this.parent = parentID;
		this.setUpdated();
		return this;
	}
	
	// For testing comparators
	public Comment setCreationDate(Date date) {
		this.createdAt = date;
		return this;
	}
	
	public Comment setContext(Context context) {
		this.context = context;
		return this;
	}
	
	public Comment setUpdated() {
		this.updatedAt = new Date();
		return this;
	}
	
	
	// Helper functions
	
	/**
	 * Check if the comment is a top level comment or a reply
	 * @return boolean Does this comment have a parent?
	 */
	public boolean hasParent() {
		return (this.parent != null);
	}
	
	/**
	 * Check if the comment has a picture associated with it
	 * @return boolean Does the comment have a picture?
	 */
	public boolean hasPicture() {
		if (this.picture == null || this.picture.isNull()) {
			return false;
		} else if (this.picture.getImageByte() == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * The number of children is 0 if the list
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
	 * @return boolean Is the comment local?
	 */
	public boolean isLocal() {
		return DataModel.isLocal(this, context);
	}
	
	/**
	 * Checks whether the id's of 2 comments are the same. This is
	 * used by DataModel to decide if a comment has been saved locally
	 * but might have had it's fields edited.
	 * @param Object The comment object we're comparing
	 * @return boolean true if IDs are same, false otherwise
	 */
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		// Don't call comment.equal on a non-comment object
		try {
			if (((Comment) o).getId().equals(this.getId())) {
				return true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Override hashcode so that it's solely dependent on id, for equivalency checking
	 */
	@Override
	public int hashCode () {
		return this.id.hashCode();
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

