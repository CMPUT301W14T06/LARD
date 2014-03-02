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
		this.bodyText = bodyText;
		this.setUpdated();
	}
	
	public void setAuthor(String username, Context context) {
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
	
}

