package ca.ualberta.lard.model;

import android.util.Base64;

/**
 * Simple picture model
 * <p>
 * Stores a string which is a Base64 encoded Byte Array
 *
 * @author Dylan
 */

public class Picture {
	private String imageString;
	
	/**
	 * Sets the Byte Array representation of the Picture
	 * @param byteArray A Byte Array representation of the Picture
	 */
	public void setImageByte(byte[] byteArray) {
		imageString = Base64.encodeToString(byteArray, Base64.URL_SAFE);
	}
	
	/**
	 * Returns the Byte Array representation of the Picture
	 */
	public byte[] getImageByte() {
		return Base64.decode(imageString, Base64.URL_SAFE);
	}

	/**
	 * Sets the Base64 encoded String representation of the Picture
	 * <p>
	 * Might become DEPRECATED
	 * @param string A Base64 encoded String representation of the Picture
	 */
	public void setImageString(String string) {
		imageString = string;
	}
	
	/**
	 * Returns the Base64 encoded String representation of the Picture
	 * <p>
	 * Might become DEPRECATED
	 */
	public String getImageString() {
		return imageString;
	}

}
