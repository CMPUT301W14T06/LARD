package ca.ualberta.lard.test;

import ca.ualberta.lard.MainActivity;
import ca.ualberta.lard.model.Favourites;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import java.util.UUID;

public class FavouritesTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private SharedPreferences prefs;
	private Favourites favourites;
	private String id;

	public FavouritesTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		prefs = getActivity().getSharedPreferences(Favourites.PREFS_NAME, Context.MODE_PRIVATE);
		favourites = new Favourites(prefs);
		id = UUID.randomUUID().toString();
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
		
	public void testAddFavourite() {
		System.err.println("Adding favourite");
		favourites.addFavourite(id);
		System.err.println(prefs.getAll().toString());
		assertTrue(favourites.isFavourite(id));
	}
}
