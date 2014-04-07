package ca.ualberta.lard.model;

import java.util.HashSet;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * The object that manages the users that we are following. Like favourites, it requires a SharedPreference
 * in order to function.
 * @author Troy Pavlek
 *
 */
public class Follow {
	// Name of the Shared Preference file
	public static final String PREFS_NAME = "follows.pref";
	// Name of the object in the Shared Preference containing the Author names.
	private final String PREFS_OBJ = "followsList";
	private SharedPreferences preferences;
	private Set<String> followsList;
	
	/**
	 * Instantiate a new Follow object (Constructor)
	 * @param prefs SharedPreferences The preferences object containing the data we want.
	 */
	public Follow(SharedPreferences prefs) {
		preferences = prefs;
		followsList = preferences.getStringSet(PREFS_OBJ, new HashSet<String>());
	}
	
	/**
	 * Is the user following the user specified?
	 * @param id String name of the author in question
	 * @return boolean Are we following id?
	 */
	public boolean isFollowing(String id) {
		return followsList.contains(id);
	}
	
	/**
	 * Follow an author
	 * @param id String the author we wish to follow
	 */
	public void addFollow(String id) {
		// We don't want to do anything if the comment is already a favourite
		if (this.isFollowing(id)) {
			return;
		}
		// We need to make a new instance of a set, since the one we got from Shared is immutable.
		Set<String> editSet = new HashSet<String>();
		editSet.addAll(followsList);
		
		// add our new follow
		editSet.add(id);
		Editor editor = preferences.edit();
		editor.putStringSet(PREFS_OBJ, editSet);
		//commit changes
		editor.commit();
	}

	/**
	 * Gets the set of author names that we follow
	 * @return Set<String> Followed author names
	 */
	public Set<String> getFollowed() {
		return followsList;
	}

}
