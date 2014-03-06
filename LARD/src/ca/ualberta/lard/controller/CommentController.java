package ca.ualberta.lard.controller;

import java.util.ArrayList;

import android.content.Context;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.CommentRequest;
import ca.ualberta.lard.model.DataModel;
import ca.ualberta.lard.model.GeoLocation;

/**
 * 
 * @author Troy Pavlek
 *
 */
public class CommentController {
	
	private ArrayList<Comment> buffer;
	
	/**
	 * 
	 * @param req
	 */
	public void init(CommentRequest req) {
		buffer = DataModel.retrieveComments(req);
	}
	
	/**
	 * 
	 * @param context
	 */
	public CommentController(Context context) {
		CommentRequest req = new CommentRequest(10); //TODO pull this from configuration.
		req.setLocation(new GeoLocation(context));
		init(req);
	}
	
	/**
	 * 
	 * @param req
	 */
	public CommentController(CommentRequest req) {
		init(req);
	}
	
	public CommentController request(CommentRequest req) {
		init(req);
		return this;
	}
	
	public boolean any() {
		return buffer.size() > 0;
	}

}
