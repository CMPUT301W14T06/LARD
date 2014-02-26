package ca.ualberta.lard.model;

import java.util.Date;

import android.content.Context;

public class Comment {
	
	private String Id;
	private String bodyText;
	private Date createdAt;
	private Date updatedAt;
	private String author;
	private GeoLocation location;
	private Picture picture;
	private Comment parent;
	
	/* Minimal constructor, contains only body text */
	public Comment(String body, Context context) {
		this.createdAt = new Date();
		this.updatedAt = new Date();
		
		User user = new User("Anonymous", context);// Todo put this in preferences
		this.author = user.getUsername();
		this.location = new GeoLocation(context);
	} 
	
	public String toString() {
		return this.bodyText;
	}
	
	// Getters
	
	public String getId() {
		return this.Id;
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
		return this.parent;
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
	
	public void setParent(Comment comment) {
		this.parent = comment;
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

