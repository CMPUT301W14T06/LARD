package ca.ualberta.lard.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Pair;
import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.comparator.CreationDateComparator;
import ca.ualberta.lard.comparator.LocationComparator;
import ca.ualberta.lard.comparator.PictureComparator;
import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.GeoLocation;
import ca.ualberta.lard.model.Picture;

/**
 * JUnit tests for testing the comparators used to sort comments by creation date,
 * proximity to a GeoLocation, or by pictures first.
 * @author Victoria
 *
 */

public class ComparatorTests extends ActivityInstrumentationTestCase2<MainActivity>{

	private ArrayList<Comment> comments;
	private Comment comment1;
	private Comment comment2;
	private Comment comment3;
	
	public ComparatorTests() {
		super(MainActivity.class);
	}
	
	protected void setUp() {
		comments = new ArrayList<Comment>();
		comment1 = new Comment("Hello", getActivity());
		comment2 = new Comment("Cats", getActivity());
		comment3 = new Comment("Apples", getActivity());
	}
	
	protected void tearDown() {
		comments = null;
		comment1 = null;
		comment2 = null;
		comment3 = null;
	}
	
	/**
	 * Tests that comments can correctly be sorted according to creation date
	 * using the CreationDateComparator.
	 */
	@SuppressWarnings("deprecation")
	public void testCreationDateComparator() {
		// Set some dates with c1 as newest and c3 as oldest.
		Date date = new Date();
		date.setYear(2015);
		comment2.setCreationDate(date);
		date.setYear(2016);
		comment3.setCreationDate(date);
		
		
		// Add the comments in the wrong order
		comments.add(comment3);
		comments.add(comment1);
		comments.add(comment2);
		
		Collections.sort(comments, new CreationDateComparator());

		// Check comments are sorted
		// index 0 < index 1 --> returns -1
		assertEquals("Comments should be sorted by creation date", 
				comments.get(0).getCreatedDate().compareTo(comments.get(1).getCreatedDate()), -1);
	}
	
	/**
	 * Checks that comments can be sorted based on some fixed GeoLocation using
	 * the LocationComparator.
	 */
	public void testLocationComparator() {
		// Change the GeoLocation of the comments
		GeoLocation specificLoc = new GeoLocation(0.0, 0.0);
		GeoLocation geoLoc1 = new GeoLocation(1.4, 0.0);
		GeoLocation geoLoc2 = new GeoLocation(8.0, 20.0);
		GeoLocation geoLoc3 = new GeoLocation(-2.0, -4.0);
		
		// Attach locations to comments
		comment1.setLocation(geoLoc1);
		comment2.setLocation(geoLoc2);
		comment3.setLocation(geoLoc3);
		
		// Create a list of (distance from current location, comment) pairs.
		ArrayList<Pair<Double, Comment>> pairs = new ArrayList<Pair<Double, Comment>>();
		for(Comment comment: comments) {
			Double distance = specificLoc.distanceFrom(comment.getLocation());
			pairs.add(Pair.create(distance, comment));
		}
		Collections.sort(pairs, new LocationComparator());
		
		// Check the comments were sorted by a specific location.
		assertTrue("Comments should be sorted by a specific geolocation",
				pairs.get(0).first < pairs.get(1).first);
		assertTrue(pairs.get(1).first < pairs.get(2).first);	
	}
	
	/**
	 * Checks that comments with pictures are placed before comments without
	 * pictures.
	 * Providing a list with no pictures should have no effect on sorting order
	 * and providing a list with all pictures should have no effect on sorting
	 * order.
	 */
	public void testPictureComparator() {
		// Try sorting a list that does not contain pictures.
		ArrayList<Comment> copy = comments;
		Collections.sort(comments, new PictureComparator());
		assertTrue("", copy.equals(comments));
		
		// Try sorting with some pictures.
		Picture picture = new Picture();
		picture.setImageByte(new byte[] {(byte) 0x00, (byte)0xf3});
		comment2.setPicture(picture);
		Collections.sort(comments, new PictureComparator());
		assertFalse(copy.equals(comments));
		assertTrue(comments.get(0).hasPicture());
		assertFalse(comments.get(1).hasPicture());
		
		// Try sorting if all comments have a picture.
		Picture pic2 = new Picture();
		picture.setImageByte(new byte[] {(byte) 0x00, (byte)0xf3});
		comment1.setPicture(pic2);
		Picture pic3 = new Picture();
		picture.setImageByte(new byte[] {(byte) 0x00, (byte)0xf3});
		comment3.setPicture(pic3);
		copy = comments;
		Collections.sort(comments, new PictureComparator());
		assertTrue(copy.equals(comments));
	}

}
