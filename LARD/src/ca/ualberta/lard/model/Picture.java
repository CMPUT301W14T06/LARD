package ca.ualberta.lard.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	 * Returns true if the picture is null (ie not set)
	 */
	public boolean isNull() {
		if (imageString != null) {
			return false;
		}
		return true;
	}
	
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
	 * Returns the Bitmap representation of the Picture
	 */
	public Bitmap getBitmap() {
		byte[] decodedByte = getImageByte();
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
}
