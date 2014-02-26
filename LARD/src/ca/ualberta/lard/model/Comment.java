package ca.ualberta.lard.model;

import java.util.Date;

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
	public Comment(String body) {
		this.createdAt = new Date();
		this.updatedAt = new Date();
		this.author = "Anonymous"; // Todo put this in preferences
		// Todo geolocation
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
	
	public void setAuthor(String username) {
		// Todo proper username getting.
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

