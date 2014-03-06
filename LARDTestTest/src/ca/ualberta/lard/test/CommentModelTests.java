/**
 * JUnit tests for 
 * @author Victoria
 */

package ca.ualberta.lard.test;

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
		date = new Date();
	}
	
	/**
	 * Test the creation of comment. All fields should have a value on creation with
	 * the exception of picture and parent. Picture is null until a picture is attached.
	 * Parent remains null if it is a top level comment.
	 */
	public void testConstructors() {		
		assertNotNull("An id should be created.", comment.getId());
		assertNotNull("Comment text should exist.", comment.getBodyText());
		assertNotNull("A date should be created.", comment.getCreatedDate());
		assertEquals("Created date should be the actual date.", date, comment.getCreatedDate());
		assertEquals("Updated date should match created date.", comment.getCreatedDate(), comment.getUpdatedDate());
		assertNotNull("A comment must have an author", comment.getAuthor());
		assertNull("Picture should be null on creation.", comment.getPicture());
		assertNotNull("A comment must have a location.", comment.getLocation());	
	}
	
	public void testAuthor() {
		String curAuthor = comment.getAuthor();
		// Try to change the author to the current name, shouldn't affect anything
		comment.setAuthor(curAuthor, getActivity());
		assertEquals("Updating with the same author name shoudl make no changes",
				curAuthor, comment.getAuthor());
			
		// Try to change the author to a new name
		String newAuthor = "FluffyBunny";
		comment.setAuthor(newAuthor, getActivity());		
		assertFalse("New author should not be the same as the old author.",
				curAuthor.equals(comment.getAuthor()));
		assertEquals("Author should be updated to new author.", newAuthor, comment.getAuthor());
	}
	
	/**
	 * When the text of a comment is updated, 
	 * the text should be changed to the new text.
	 */
	public void testSetBodyText() {
		// Get the current comment text
		String newText = "I like puppies.";
		
		// Change the comment text
		comment.setBodyText(newText);
		
		assertEquals("The text should match the new input text.", newText, comment.getBodyText());
	}
	
	/**
	 * When a comment is updated the updated date should not match the created date
	 * but should be updated to the correct date.
	 */
	public void testUpdatedAt() {
		date = new Date();
		comment.setUpdated();
		assertFalse("Creation date and updated date should differ.", 
				(comment.getCreatedDate().equals(comment.getUpdatedDate())));
		assertEquals("Should update to the correct date.", date, comment.getUpdatedDate());	
	}
	
	public void testLocation() {
		GeoLocation curLocation = comment.getLocation();
		GeoLocation newLocation = new GeoLocation(9999);
		comment.setLocation(newLocation);
		
		assertFalse("After updating, new location should not equal old location",
				curLocation.equals(newLocation));
		assertEquals("Location should have been updated.", comment.getLocation(), newLocation);	
	}
	
	/**
	 * When a picture is added to a comment, the picture object should match
	 * and not be null.
	 */
	public void testPicture() {
		// By default picture is null.
		assertFalse(comment.hasPicture());
		
		// Attach a picture to the comment.
		Picture picture = null; //TODO Attach an actual picture here
		comment.setPicture(picture);
		
		// Picture should no longer be null and should have the correct picture.
		assertNotNull("After adding a valid picture to a comment, picture field should not be null.",
				comment.getPicture());
		assertTrue(comment.hasPicture());
		assertEquals("Picture should be the same as the uploaded picture.", picture, comment.getPicture());
	}
	
	public void testParent() {
		// null or set to parents id
		assertFalse("A newly created comment has no parent by default.", comment.hasParent());
		assertNull(comment.getParent());
		
		// Update to have a parent
		String parentId = "1111";
		comment.setParent("1111");
		assertTrue(comment.hasParent());
		assertEquals("Id should be same as parent id", parentId, comment.getParent());
	}
	
	public void testIsLocal() {
		assertFalse(comment.isLocal());
		DataModel.saveLocal(comment, false);
		assertTrue(comment.isLocal());
	}
	
	public void testToString() {
		assertEquals("getTextBody and toString should return the same thing.", 
				comment.getBodyText(), comment.toString());
		
	}

}
