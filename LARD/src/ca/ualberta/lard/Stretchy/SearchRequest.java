package ca.ualberta.lard.Stretchy;

import ca.ualberta.lard.model.CommentRequest;

public class SearchRequest {
	
	private int _size;
	private String bodyText;
	private String parent;
	
	public SearchRequest(int size) {
		this._size = size;
	}
	
	public SearchRequest(CommentRequest req) {
		this.bodyText = req.getBodyText();
	}
	
	public String toString() {
		String ret = 
			"{ "
			+ "\"size\" : " + this._size + ", "
			+ "\"query\" : { ";
		if (this.bodyText != null) {
			ret += "\"term\" : { \"bodyText\" : \"" + this.bodyText + "\" } "; 
		}
		ret += "} ";
		ret += "}";
		return ret;
	}
	
	public void bodyText(String text) {
		this.bodyText = text;
	}
	
	public void parent(String id) {
		this.parent = id;
	}

}
