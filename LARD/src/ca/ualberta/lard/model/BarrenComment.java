package ca.ualberta.lard.model;

public class BarrenComment {
	
	private String body;
	private String author;
	private Boolean hasPicture;
	private int numChildren;
	private int distance;
	
	public BarrenComment(String body, String author, Boolean hasPicture, int numChildren, int distance) {
		this.body = body;
		this.author = author;
		this.hasPicture = hasPicture;
		this.numChildren = numChildren;
		this.distance = distance;
	}
	
	public int getNumChildren() {
		return numChildren;
	}

	public void setNumChildren(int numChildren) {
		this.numChildren = numChildren;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String toString() {
		return body.toString();
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Boolean getHasPicture() {
		return hasPicture;
	}
	public void setHasPicture(Boolean hasPicture) {
		this.hasPicture = hasPicture;
	}

}
