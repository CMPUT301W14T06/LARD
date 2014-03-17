package ca.ualberta.lard.test;

import java.util.Random;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.Picture;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Tests the LARD Picture Model. Test String and byte array getters and setters
 */
public class PictureModelTests extends ActivityInstrumentationTestCase2<MainActivity> {
	
	public PictureModelTests() {
		super (MainActivity.class);
	}
	
	/**
	 * Tests the String setters and getters with an arbitrary String
	 */
	public void testSetImageString() {
		Picture picture = new Picture();
		String string = "The quick brown fox jumps over the lazy dog!";
		picture.setImageString(string);
		assertEquals("The String getter should return the setted string", string, picture.getImageString() );
	}
	
	/**
	 * Tests the byte getters and setters to make sure they are not returning null
	 */
	public void testByteArray(){
		Picture picture = new Picture();
		byte[] byteArray = new byte[] {(byte) 0x00, (byte)0xf3};
		picture.setImageByte(byteArray);
		assertNotNull("The setted byteArray should not be null", picture.getImageByte() );
	}

}
