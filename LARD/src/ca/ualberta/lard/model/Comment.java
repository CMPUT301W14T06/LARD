package ca.ualberta.lard.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.content.Context;

public class Comment {
	
	private String id;
	private String bodyText;
	private Date createdAt;
	private Date updatedAt;
	private String author;
	private GeoLocation location;
	private Picture picture;
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
		c.createdAt = new Date();
		c.updatedAt = new Date();
		
		User user = new User("Anonymous", context);// Todo put this in preferences
		this.bodyText = body;
		c.author = user.getUsername();
		c.location = new GeoLocation(context);
		c.id = UUID.randomUUID().toString();
		c.picture = null;
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
	
	public GeoLocation getLocation() {
		return this.location;
	}
	
	public Picture getPicture() {
		return this.picture;
	}
	
	public Comment getParent() {
		CommentRequest req = new CommentRequest(1);
		req.setId(this.parent);
		ArrayList<Comment> arr = DataModel.retrieveComments(req);
		
		if (arr.size() > 0) return arr.get(0);
		return null;
	}
	
	public ArrayList<Comment> children() {
		ArrayList<Comment> arr;
		return null;
	}
	
	// Setters
	
	public void setBodyText(String bodyText) {
		// Check the body text is not empty. If it is set to
		// [Comment Text Removed].
		if (bodyText == "" || bodyText == null) {
			bodyText = "[Comment Text Removed]";
		}
		this.bodyText = bodyText;
		this.setUpdated();
	}
	
	public void setAuthor(String username, Context context) {
		// Check the username is not empty. If it is set to Anonymous.
		if (username == "" || username == null) {
			username = "Anonymous";
		}
		
		User user = new User(username, context);
		this.author = user.getUsername();
		this.setUpdated();
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
	
	
	// Helper functions
	
	public boolean hasParent() {
		return (this.parent != null);
	}
	
	public void setUpdated() {
		this.updatedAt = new Date();
	}
	
	public boolean hasPicture() {
		if (this.picture == null) {
			return false;
		}
		return true;
	}
	
	public int numReplies() {
		if (this.children() == null) {
			return 0;
		}
		return this.children().size();
	}
	
	public boolean isLocal() {
		// TODO implement this
		return false;
	}
	
}

