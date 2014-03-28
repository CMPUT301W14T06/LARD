package ca.ualberta.lard.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;

/**
 * The User model deals with the unique username of the user. Username is 
 * decided by a component chosen by the user, which can be changed at any time,
 * and a hash determined by the device id. 
 */

public class User {
	public static String PREFS_NAME = "username.pref";
	private String username;
	private String userId;
	private SharedPreferences prefs;
	
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
	 * Removes all Octothorpe characters from a username if there are any
	 * @param name A username to be checked
	 * @return Name A valid username
	 */
	private String removeOctothorpe(String name) {
		if (name.contains("#")) {
			String cleanedName = name.replaceAll("#", "");
			return cleanedName;
		}
		return name;
	}
	
	public void setUsername(String username) {
		this.username = removeOctothorpe(username);
		this.set("username", username);
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
		try {
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		String usernameWithSalt = (this.username + this.userId);
		md.update(usernameWithSalt.getBytes(), 0, usernameWithSalt.length());
		 
        //Converts message digest value in base 16 (hex) 
        String hash = new BigInteger(1, md.digest()).toString(16);
		
		return this.username + "#" + hash;
		
		} catch (NoSuchAlgorithmException e) {
			System.err.println("No such algorithm");
			// todo this should probably explode?
		}
		// We've gotten to an unsalvageable state. They're anonymous with their androidID now.
		return "Anonymous#" + this.userId;
		
	}
	
	private boolean set(String key, String value) {
		Editor editor = this.prefs.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	private String newUserId() {
		return UUID.randomUUID().toString();
	}
}
