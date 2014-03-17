package ca.ualberta.lard.model;

/**
 * A CommentRequest is a structure that aids in the retrieving of comments. It unifies the arguments passed around, by being a single structure
 * in which the client can say any number of things they expect of the comment they want. All parameters are optional, and if any of them are set
 * it is assumed the user is requesting Comments relevant to that parameter.
 * @author Troy Pavlek
 *
 */
public class CommentRequest {
	
	/**
	 * The most important parameter. If the CommentRequest contains an ID, then the user is searching for a specific, single comment.
	 */
	private String id;
	
	/**
	 * We can specify that the comment must be a child of a particular comment.
	 */
	@SuppressWarnings("unused") // TODO: Remove
	private String parentId;
	
	/**
	 * If the location property is set, Comments nearest this location will be returned over those further from this location.
	 */
	@SuppressWarnings("unused") // TODO: Remove
	private GeoLocation location;
	
	/**
	 * Do we only want to search threads, or are replies acceptable also?
	 */
	@SuppressWarnings("unused") // TODO: Remove
	private boolean topLevel;
	
	/**
	 * Do we only want comments with pictures?
	 */
	@SuppressWarnings("unused") // TODO: Remove
	private boolean hasPicture;
	
	/**
	 * The number of results we want returned
	 */
	private int resultSize;
	
	/**
	 * Key words to search the body text for
	 */
	private String bodyText;
	
	/**
	 * Instantiates the Comment Request
	 * <p>
	 * All the constructor does is instantiates the object with our expected return size. The user should be setting the remainder of the parameters of the
	 * CommentRequest with the available setters.
	 * </p>
	 * @param num The number of results we want. We may get less, but we will never get more.
	 */
	public CommentRequest(int num) {
		this.resultSize = num;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setParentId(String id) {
		this.parentId = id;
	}
	
	public void setLocation(GeoLocation loc) {
		this.location = loc;
	}
	
	public void onlyThreads() {
		this.topLevel = true;
	}
	
	public void onlyReplies() {
		this.topLevel = false;
	}
	
	public void withPictures() {
		this.hasPicture = true;
	}
	
	public void withoutPictures() {
		this.hasPicture = false;
	}
	
	public void bodyText(String body) {
		this.bodyText = body;
	}
		
	public String getId() {
		return this.id;
	}
	
	public String getBodyText() {
		return this.bodyText;
	}
	
	public int size() {
		return this.resultSize;
	}

}
