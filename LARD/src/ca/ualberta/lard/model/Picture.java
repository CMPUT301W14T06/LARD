package ca.ualberta.lard.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Simple picture model
 * <p>
 * Stores a string which is a Base64 encoded Byte Array
 * </p>
 * @author Dylan
 */

public class Picture {
	// Image as a base64 string
	private String imageString;
	
	/**
	 * Is the picture null?
	 * @return boolean picture null?
	 */
	public boolean isNull() {
		if (imageString != null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Sets the Byte Array representation of the Picture
	 * @param byte[] A Byte Array representation of the Picture
	 * @return Picture Itself, for chaining
	 */
	public Picture setImageByte(byte[] byteArray) {
		imageString = Base64.encodeToString(byteArray, Base64.URL_SAFE);
		return this;
	}

	/**
	 * Gets the byte array representation of the picture.
	 * @return byte[] the Byte Array representation of the Picture
	 */
	public byte[] getImageByte() {
		return Base64.decode(imageString, Base64.URL_SAFE);
	}
	
	/**
	 * Gets the bitmap representation of the picture.
	 * @return Bitmap the Bitmap representation of the Picture
	 */
	public Bitmap getBitmap() {
		byte[] decodedByte = getImageByte();
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
}
