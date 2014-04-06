package ca.ualberta.lard.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

/**
 * A static utility class that abstracts out common IO and File-based actions. Class has no state and is simply called as
 * needed to reduce code duplication
 * @author Troy Pavlek
 *
 */
public class File {

	public static String parseJsonFromResponse(HttpResponse response) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String result = "";
			String output;
			while ((output = br.readLine()) != null) {
				result += output;
			}
			
			return result;
		} catch (IOException e) {
			System.err.println("Error parsing JSON from HTTP response " + e.getMessage());
		} catch (NullPointerException e) {
			System.err.println("Cannot parse a null");
		}
		return null;
	}
}
