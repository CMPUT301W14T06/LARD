package ca.ualberta.lard.model;

import java.util.HashSet;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Favourites {
	public static final String PREFS_NAME = "favourites.pref";
	private final String PREFS_OBJ = "favouritesList";
	private SharedPreferences preferences;
	private Set<String> favouritesList;
	
	public Favourites(SharedPreferences prefs) {
		preferences = prefs;
		favouritesList = preferences.getStringSet(PREFS_OBJ, new HashSet<String>());
	}
	
	public boolean isFavourite(String id) {
		return favouritesList.contains(id);
	}
	
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

	public Set<String> getFavouritesList() {
		return favouritesList;
	}

}
