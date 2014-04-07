package ca.ualberta.lard.model;

import java.util.HashSet;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * The Favourites object manages the tracking of the IDs of the favourite comments on the system.
 * The set of IDs that we have favourited is stored in a SharedPreference file on the system, the
 * file name denoted by PREFS_NAME. In order to be instantiated, the class must be passed an object
 * that implements SharedPreferences.
 * @author Troy Pavlek
 *
 */
public class Favourites {
	// Name of the shared preferences file where the IDs are stored.
	public static final String PREFS_NAME = "favourites.pref";
	// Name of the object that contains the IDs in the Shared Preferences
	private final String PREFS_OBJ = "favouritesList";
	private SharedPreferences preferences;
	private Set<String> favouritesList;
	
	/**
	 * Instantiate the Favourites object
	 * @param prefs SharedPreferences The preferences object containing our Favourites.
	 */
	public Favourites(SharedPreferences prefs) {
		preferences = prefs;
		favouritesList = preferences.getStringSet(PREFS_OBJ, new HashSet<String>());
	}
	
	/**
	 * Is the ID the ID of a favourite comment?
	 * @param id String ID of the Comment we're checking
	 * @return boolean Is the ID a favourite?
	 */
	public boolean isFavourite(String id) {
		return favouritesList.contains(id);
	}
	
	/**
	 * Adds an ID to the list of favourites
	 * @param id String ID of the comment we want to favourite
	 */
	public void addFavourite(String id) {
		// We don't want to do anything if the comment is already a favourite
		if (this.isFavourite(id)) {
			return;
		}
		// We need to make a new instance of a set, since the one we got from Shared is immutable.
		Set<String> editSet = new HashSet<String>();
		editSet.addAll(favouritesList);
		
		// add our new favourite
		editSet.add(id);
		Editor editor = preferences.edit();
		editor.putStringSet(PREFS_OBJ, editSet);
		//commit changes
		editor.commit();
	}

	/**
	 * Get the list of favourites.
	 * @return Set<String> favourite comment IDs.
	 */
	public Set<String> getFavouritesList() {
		return favouritesList;
	}

}
