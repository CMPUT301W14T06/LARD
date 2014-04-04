package ca.ualberta.lard.test;

import java.util.ArrayList;
import java.util.Date;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.DataModel;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.Picture;

/**
 * JUnit tests for Comment model. Tests creation of a new comment, updating
 * elements of the created comment, and helper functions to get useful 
 * information about the comment such as if it is saved locally.
 * @author Victoria
 */

public class CommentModelTests extends ActivityInstrumentationTestCase2<MainActivity> {
	private ca.ualberta.lard.model.Comment comment;
	
	public CommentModelTests() {
		super (MainActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		comment = new Comment("JUnit Comment Model Test Comment", getActivity());
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		comment = null;
	}
	
	/**
	 * Tests the creation of a Comment. All fields should have a value on creation with
	 * the exception of picture and parent. Picture is null until a picture is attached.
	 * Parent remains null if it is a top level comment.
	 */
	public void testConstructors() {
		assertNotNull("An id should be created.", comment.getId());
		assertNotNull("Comment text should exist.", comment.getBodyText());
		assertNotNull("A date should be created.", comment.getCreatedDate());
		
		// Compare substrings because seconds portion causes test failure occasionally.
		String date = new Date().toString().substring(0, 10);
		assertEquals("Created date should be the actual date.", date,
				comment.getCreatedDate().toString().substring(0, 10));
		assertEquals("Updated date should match created date.",
				comment.getCreatedDate().toString().substring(0,10), 
				comment.getUpdatedDate().toString().substring(0, 10));
		
		assertNotNull("A comment must have an author", comment.getAuthor());
		assertFalse("Should not have a picture on creation.", comment.hasPicture());
		assertNotNull("A comment must have a location.", comment.getLocation());	
		assertFalse("A comment has no parent by default.", comment.hasParent());
	}
	
	/**
	 * Tests updating the author field of a Comment. First tests "updating" the author
	 * to the same name. Next updating the author with a new name is tested. Finally, trying
	 * to update the author name to nothing should update it to anonymous.
	 */
	public void testAuthor() {
		String curAuthor = comment.getRawAuthor();
		// Updating to the same name should have no effect.
		comment.setAuthor(curAuthor);
		assertEquals("Updating author with same name should have no effect.", curAuthor, comment.getRawAuthor());
		
		// Try to change the author to a new name.
		String newAuthor = "FluffyBunny";
		comment.setAuthor(newAuthor);		
		assertEquals("Author should be updated to new author.", newAuthor, comment.getRawAuthor());
		
		// Try to change the user name to nothing.
		newAuthor = "";
		comment.setAuthor(newAuthor);
		assertFalse("A username cannot be blank.", comment.getRawAuthor().isEmpty());
		assertEquals("Updating username with nothing sets it to Anonymous", 
				"Anonymous", comment.getRawAuthor());
	}
	
	/**
	 * Tests updating the text of a comment. First tests "updating" the text to the same text.
	 * Next texts updating the text to different text. Finally, trying to update the text to
	 * nothing should update it to [Comment Text Removed].
	 */
	public void testSetBodyText() {
		// Try to update the text to the same thing.
		String curText = comment.getBodyText();
		comment.setBodyText(curText);
		assertEquals("Updating with the same body text should make no changes.",
				curText, comment.getBodyText());
		
		// Try to update the text to something new.
		String newText = "I like puppies.";
		comment.setBodyText(newText);	
		assertEquals("The text should match the new input text.", newText, comment.getBodyText());
		
		// Try to update the text to nothing.
		newText = "";
		comment.setBodyText(newText);
		assertEquals("Trying to update the text to nothing displays default text.",
				"[Comment Text Removed]", comment.getBodyText());
		
	}
	
	/**
	 * Tests that when a comment is updated, the date it was updated is set
	 * to the current date.
	 */
	public void testUpdatedAt() {
		Date date = new Date();
		comment.setUpdated();
		assertEquals("Should update to the correct date.", date, comment.getUpdatedDate());	
	}
	
	/**
	 * Tests that the GeoLocation of a comment can updated to a new location.
	 */
	public void testLocation() {
		GeoLocation curLocation = comment.getLocation();
		GeoLocation newLocation = new GeoLocation(53.526425, -113.520443);
		comment.setLocation(newLocation);
		
		assertFalse("After updating, new location should not equal old location",
				curLocation.equals(newLocation));
		assertEquals("Location should have been updated.", comment.getLocation(), newLocation);	
	}
	
	/**
	 * Tests that a newly created comment has picture set to null
	 * by default and returns false when asked if it has a picture.
	 * When a user attaches a picture, the same picture should be stored
	 * in the picture field of Comment and it will return true when asked 
	 * if it has a picture.
	 */
	public void testPicture() {
		// By default picture is null.
		assertFalse(comment.hasPicture());
		
		// Attach a picture to the comment.
		Picture picture = new Picture();
		picture.setImageString("kadj");
		comment.setPicture(picture);
		
		// Picture should no longer be null and should have the correct picture.
		assertNotNull(comment.getPicture());
		assertTrue(comment.hasPicture());
		assertEquals("Picture should be the same as the uploaded picture.",
				picture, comment.getPicture());
	}
	
	/**
	 * Tests that a Comment has no parent by default and returns false when
	 * asked if it has a parent. Next, tests that the Comment can be updated
	 * to have a parent and returns true when asked if it has a parent.
	 */
	public void testParent() {
		// By default parent = null
		assertFalse("A newly created comment has no parent by default.", comment.hasParent());
		assertNull("No parent means parent is set to null.", comment.getParent());
		
		// Update to have a parent
		Comment parent = new Comment("JUnit test: parent", getActivity());
		comment.setParent(parent.getId());
		assertTrue("Has parent should return true if you have a parent.", comment.hasParent());
		assertEquals("Comment should be able to get the correct parent.", parent, comment.getParent());
	}
	
	/**
	 * Tests that a newly created comment is not saved locally unless the DataModel
	 * is told to save it locally.
	 */
	public void testIsLocal() {
		assertFalse(comment.isLocal());
		DataModel.saveLocal(comment, false, getActivity().getBaseContext(), false);
		assertTrue(comment.isLocal());
	}
	
	/**
	 * Tests that Comment.toString returns the same text as
	 * Comment.bodyText. 
	 */
	public void testToString() {
		assertEquals("getTextBody and toString should return the same thing.", 
				comment.getBodyText(), comment.toString());
	}
	
	/**
	 * Tests that a comment has no children upon creation. Having no children means
	 * the number of children a comment has is zero and when asked for a list of its
	 * children it returns null.
	 * When a comment has children, this means that when asked about its children it 
	 * returns a list with at least 1 comment.
	 */
	public void testChildren() {
		// By default a comment has no children
		assertTrue("A comment with not replies should have no children.", 
				comment.children().isEmpty());
		
		// Create a child for the comment
		String parentId = comment.getId();
		Comment childComment = new Comment("I am a child.", getActivity());
		childComment.setParent(parentId);
		DataModel.save(childComment);
		
		// Check that the comment now has a child
		assertEquals(comment.numReplies(), 1);
		ArrayList<Comment> childList = new ArrayList<Comment>();
		childList.add(childComment);
		assertEquals("Child List should have the correct children.",
				childList, comment.children());
	}
	
	/**
	 * Tests that comments are equal as long as they have the same ids. If two comments with the same
	 * ids are compared, but have other differences, they are the same.
	 */
	public void testEquals() {
		assertTrue("A comment compared to itself should be equal.", comment.equals(comment));
		Comment different = new Comment("JUnit testing, different", getActivity());
		assertFalse("A comment with a different id is not equal.", comment.equals(different));
		Comment edited = comment;
		edited.setBodyText("hihihihi");
		assertTrue("A comment compared to another comment with the same id is equal, regardless of other fields.",
				comment.equals(edited));
	}

}
