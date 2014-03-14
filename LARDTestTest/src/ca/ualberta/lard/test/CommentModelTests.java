/**
 * JUnit tests for Comment model. Tests creation of a new comment, updating
 * elements of the created comment, and helper functions to get useful 
 * information about the comment such as if it is saved locally.
 * @author Victoria
 */

package ca.ualberta.lard.test;

import java.util.ArrayList;
import java.util.Date;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.DataModel;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.Picture;

public class CommentModelTests extends ActivityInstrumentationTestCase2<MainActivity> {
	private ca.ualberta.lard.model.Comment comment;
	private Date date;
	
	public CommentModelTests() {
		super (MainActivity.class);
	}
	
	protected void setUp() {
		comment = new Comment("I like kitties", getActivity());
	}
	
	/**
	 * Tests the creation of a Comment. All fields should have a value on creation with
	 * the exception of picture and parent. Picture is null until a picture is attached.
	 * Parent remains null if it is a top level comment.
	 */
	public void testConstructors() {
		assertNotNull("An id should be created.", comment.getId());
		assertNotNull("Comment text should exist.", comment.getBodyText());
		date = new Date();
		assertNotNull("A date should be created.", comment.getCreatedDate());
		assertEquals("Created date should be the actual date.", date, comment.getCreatedDate());
		assertEquals("Updated date should match created date.", comment.getCreatedDate(), comment.getUpdatedDate());
		assertNotNull("A comment must have an author", comment.getAuthor());
		assertNull("Picture should be null on creation.", comment.getPicture());
		assertNotNull("A comment must have a location.", comment.getLocation());	
	}
	
	/**
	 * Tests updating the author field of a Comment. First tests "updating" the author
	 * to the same name. Next updating the author with a new name is tested. Finally, trying
	 * to update the author name to nothing should update it to anonymous.
	 */
	public void testAuthor() {
		String curAuthor = comment.getAuthor();
		// Try to change the author to the current name. This shouldn't affect anything.
		comment.setAuthor(curAuthor, getActivity());
		assertEquals("Updating with the same author name should make no changes",
				curAuthor, comment.getAuthor());
			
		// Try to change the author to a new name.
		String newAuthor = "FluffyBunny";
		comment.setAuthor(newAuthor, getActivity());		
		assertFalse("New author should not be the same as the old author.",
				curAuthor.equals(comment.getAuthor()));
		assertEquals("Author should be updated to new author.", newAuthor, comment.getAuthor());
		
		// Try to change the user name to nothing.
		newAuthor = "";
		comment.setAuthor(newAuthor, getActivity());
		assertFalse("A username cannot be blank.", comment.getAuthor().equals(newAuthor));
		assertEquals("Updating username with nothing sets it to Anonymous", 
				"Anonymous", comment.getAuthor());
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
		date = new Date();
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
		Picture picture = null; //TODO Attach an actual picture here
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
		String parentId = "1111";
		comment.setParent("1111");
		assertTrue("Has parent should return true if you have a parent.", comment.hasParent());
		assertEquals("Id should be same as parent id", parentId, comment.getParent());
	}
	
	/**
	 * Tests that a newly created comment is not saved locally unless the DataModel
	 * is told to save it locally.
	 */
	public void testIsLocal() {
		assertFalse(comment.isLocal());
		DataModel.saveLocal(comment, false, getActivity().getBaseContext());
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
		assertNull(comment.children());
		assertEquals("A comment with no replies should have no children.",
				comment.numReplies(), 0);
		
		// Create a child for the comment
		String parentId = comment.getId();
		Comment childComment = new Comment("I am a child.", getActivity());
		childComment.setParent(parentId);
		
		// Check that the comment now has a child
		assertEquals(comment.numReplies(), 1);
		ArrayList<Comment> childList = new ArrayList<Comment>();
		childList.add(childComment);
		assertEquals("Child List should have the correct children.",
				childList, comment.children());
	}

}
