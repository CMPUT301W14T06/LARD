package ca.ualberta.lard.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * The User model deals with the unique username of the user. Username is 
 * decided by a component chosen by the user, which can be changed at any time,
 * and a hash determined by the device id. 
 */

public class User {
	// Shared preference file storing User data
	public static String PREFS_NAME = "username.pref";
	private String username;
	// The unique user ID
	private String userId;
	private SharedPreferences prefs;
	
	/**
	 * Constructor for instantiating a user.
	 * @param prefs SharedPreferences The preference object containing all the user data.
	 */
	public User(SharedPreferences prefs) {
		this.prefs = prefs;
		// We see if we already have a userID in Shared prefs.
		if (!prefs.contains("userId")) {
			this.set("userId", this.newUserId());
		}
		this.username = prefs.getString("username", "Anonymous");
		// We should never fall back to our default. If we do, then any user who has gotten to this state
		// will be able to edit this comment.
		this.userId = prefs.getString("userId", "defaultUser"); 
	}
	
	/**
	 * Removes all Octothorpe characters from a username if there are any, since
	 * this is a reserved character for delimiting a hash.
	 * @param name String A username to be checked
	 * @return String A valid username
	 */
	private String removeOctothorpe(String name) {
		if (name.contains("#")) {
			String cleanedName = name.replaceAll("#", "");
			return cleanedName;
		}
		return name;
	}
	
	/**
	 * Sets the username
	 * @param username String Username that we want to set
	 * @return User Itself, for chaining
	 */
	public User setUsername(String username) {
		this.username = removeOctothorpe(username);
		this.set("username", username);
		return this;
	}
	
	/**
	 * Returns the author name appended with a hash that is based off the id of their device.
	 * 
	 * Credit: Thanks to Brendan Long on stackoverflow for this: http://stackoverflow.com/a/3103722
	 * 
	 * @return Author name followed by a hash
	 * @author Brendan Long on stackoverflow
	 */
	public String getUsername() {
		return this.hashWithGivenName(this.username);
	}
	
	/**
	 * Computes what getUsername would return given any author name.
	 * @param givenUsername Any author name.
	 * @return authorname + # + hash as a String
	 */
	public String hashWithGivenName(String givenUsername) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			String usernameWithSalt = (givenUsername + this.userId);
			md.update(usernameWithSalt.getBytes(), 0, usernameWithSalt.length());
			 
	        //Converts message digest value in base 16 (hex) 
	        String hash = new BigInteger(1, md.digest()).toString(16);
			
			return givenUsername + "#" + hash;
			
		} catch (NoSuchAlgorithmException e) {
			System.err.println("No such algorithm");
		}
		// Couldn't compute the hash
		return "Anonymous#" + this.userId;
	}
	
	/**
	 * Sets a key => value pair in the SharedPreferences file
	 * @param key String Key that we want to save in
	 * @param value String the value we want to associate with the key
	 * @return boolean Did it work?
	 */
	private boolean set(String key, String value) {
		Editor editor = this.prefs.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	/**
	 * Generates a new user ID
	 * @return String a Unique user ID.
	 */
	private String newUserId() {
		return UUID.randomUUID().toString();
	}
}
