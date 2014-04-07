package ca.ualberta.lard.model;

import java.util.HashSet;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Follow {
	public static final String PREFS_NAME = "follows.pref";
	private final String PREFS_OBJ = "followsList";
	private SharedPreferences preferences;
	private Set<String> followsList;
	
	public Follow(SharedPreferences prefs) {
		preferences = prefs;
		followsList = preferences.getStringSet(PREFS_OBJ, new HashSet<String>());
	}
	
	public boolean isFollowing(String id) {
		return followsList.contains(id);
	}
	
	public void addFollow(String id) {
		// We don't want to do anything if the comment is already a favourite
		if (this.isFollowing(id)) {
			return;
		}
		// We need to make a new instance of a set, since the one we got from Shared is immutable.
		Set<String> editSet = new HashSet<String>();
		editSet.addAll(followsList);
		
		// add our new favourite
		editSet.add(id);
		Editor editor = preferences.edit();
		editor.putStringSet(PREFS_OBJ, editSet);
		//commit changes
		editor.commit();
	}

	public Set<String> getFollowed() {
		return followsList;
	}

}
