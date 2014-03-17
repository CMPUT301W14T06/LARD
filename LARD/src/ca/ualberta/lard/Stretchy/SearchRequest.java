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
		this._size = req.size();
		this.bodyText = req.getBodyText();
	}
	
	public String toString() {
		String ret = 
			"{ "
			+ "\"size\" : " + this._size + ", "
			+ "\"query\" : { ";
		if (!this.anyQuery()) {
			ret += "\"match_all\" : { } ";
		} else {
			ret += this.bodyString();
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
	
	public boolean anyQuery() {
		return !(this.bodyText == null && this.parent == null);
	}
	
	public String bodyString() {
		if (this.bodyText != null && !this.bodyText.isEmpty()) {
			return "\"term\" : { \"bodyText\" : \"" + this.bodyText + "\" } "; 
		}
		return "";
	}

}
