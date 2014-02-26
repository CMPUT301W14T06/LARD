package ca.ualberta.lard.model;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.provider.Settings.Secure;

public class User {
	private String username;
	private String androidId;
	
	public User(String username, Context context) {
		this.username = username;
		this.androidId = Secure.getString(context.getContentResolver(),
	            Secure.ANDROID_ID);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	// thanks to brendan long on stackoverflow for this: http://stackoverflow.com/a/3103722
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
