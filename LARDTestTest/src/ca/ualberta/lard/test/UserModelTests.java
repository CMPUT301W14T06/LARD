package ca.ualberta.lard.test;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.User;
import android.test.ActivityInstrumentationTestCase2;

public class UserModelTests extends ActivityInstrumentationTestCase2<MainActivity> {

	private User user;
	private String username = "Test";
	
	public UserModelTests() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		this.user = new User(this.username, this.getActivity());
	}
	
	public void testPreConditions() {
		assertNotNull(this.user);
		assertEquals(this.user.getClass(), User.class);
	}
	
	public void testUsername() {
		String username = user.getUsername();
		
		assertNotNull(username);
		assertTrue(username.startsWith(this.username));
		// a md5 hash is 32 characters long. So length of username should be the length of input + 33 (one character is the octothorpe)
		assertEquals(username.length(), this.username.length() + 33);
	}
	
	public void testSetUsername() {
		fail();
	}
	
	protected void tearDown() throws Exception {
	}

}
