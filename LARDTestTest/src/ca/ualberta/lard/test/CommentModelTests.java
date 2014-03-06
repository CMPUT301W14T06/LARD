package ca.ualberta.lard.test;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.Comment;

public class CommentModelTests extends ActivityInstrumentationTestCase2<MainActivity> {
	private ca.ualberta.lard.model.Comment comment;
	
	public CommentModelTests() {
		super (MainActivity.class);
	}
	
	protected void setUp() {
	}
	
	public void testConstructors() {
		Comment comment = new Comment("Kitties", getActivity());
		
	}
	
	public void testAuthor() {
		fail();
	}
	
	public void testId() {
		fail();
	}
	
	public void testBodyText() {
		fail();
	}
	
	public void testCreatedAt() {
		fail();
	}
	
	public void testUpdatedAt() {
		fail();
	}
	
	public void testLocation() {
		fail();
	}
	
	public void testPicture() {
		fail();
	}
	
	public void testParent() {
		fail();
	}
	
	public void testIsLocal() {
		fail();
	}
	
	public void testHasPicture() {
		fail();
	}
	
	public void testToString() {
		fail();
	}

}
