package ca.ualberta.lard.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.Context;
import android.provider.Settings.Secure;

/**
 * The User model deals with the unique username of the user. Username is 
 * decided by a component chosen by the user, which can be changed at any time,
 * and a hash determined by the device id. 
 */

public class User {
	private String username;
	private String androidId;
	
	/**
	 * Constructor for User. Username is based off a name the user chooses and 
	 * the id is set based on the users device. 
	 * @param username
	 * @param context
	 */
	public User(String username, Context context) {
		this.username = username;
		// TODO: Make user Android_id a singleton
		this.androidId = Secure.getString(context.getContentResolver(),
	            Secure.ANDROID_ID);
	}
	
	public void setUsername(String username) {
		this.username = username;
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
		
		String usernameWithSalt = (this.username + this.androidId);
		md.update(usernameWithSalt.getBytes(), 0, usernameWithSalt.length());
		 
        //Converts message digest value in base 16 (hex) 
        String hash = new BigInteger(1, md.digest()).toString(16);
		
		return this.username + "#" + hash;
		
		} catch (NoSuchAlgorithmException e) {
			System.err.println("No such algorithm");
			// todo this should probably explode?
		}
		// We've gotten to an unsalvageable state. They're anonymous with their androidID now.
		return "Anonymous#" + androidId;
		
	}
}
