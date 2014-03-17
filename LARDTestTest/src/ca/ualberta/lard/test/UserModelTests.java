package ca.ualberta.lard.test;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.User;
import android.test.ActivityInstrumentationTestCase2;

/**
 * JUnit tests for User model. Usernames should never be empty and should always
 * have a hash derived from the users device id appended to it. Test the behaviour
 * of User model when a username is changed.
 */

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
	
	/**
	 * Tests that a username is never null and has a hash appended to the end.
	 */
	public void testUsername() {
		String username = user.getUsername();
		
		assertNotNull(username);
		assertTrue(username.startsWith(this.username));
		// a md5 hash is 32 characters long. So length of username should be the length of input + 33 (one character is the octothorpe)
		assertEquals(username.length(), this.username.length() + 33);
	}
	
	/**
	 * Tests that when a username is changed, the hash appended to the end remains the same.
	 */
	public void testSetUsername() {
		String curName = user.getUsername();
		String curHash = curName.replace(this.username, "");
		user.setUsername("ilovepenguins");
		String newHash = user.getUsername().replace(this.username, "");
		
		assertTrue("The old username should not equal the new username.", curName != user.getUsername());
		assertEquals("Hash should not change when username is changed.", curHash, newHash);	
	}
	
	protected void tearDown() throws Exception {
	}

}
