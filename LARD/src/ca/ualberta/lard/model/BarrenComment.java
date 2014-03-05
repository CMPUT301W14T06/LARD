package ca.ualberta.lard.model;

public class BarrenComment {
	
	private String body;
	private String author;
	private Boolean hasPicture;
	
	public BarrenComment(String body, String author, Boolean hasPicture) {
		this.body = body;
		this.author = author;
		this.hasPicture = hasPicture;
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
