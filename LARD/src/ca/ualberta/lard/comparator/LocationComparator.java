package ca.ualberta.lard.comparator;

import java.util.Comparator;

import android.util.Pair;
import ca.ualberta.lard.model.Comment;

/**
 * Sorts an ArrayList of Pairs which contain a Double and a Comment. The Double
 * represents the distance of the Comment's GeoLocation from a specified GeoLocation
 * This is useful for sorting Comments by distance from a device's current GeoLocation
 * or by sorting based on a specified GeoLocation.
 * @author Victoria
 *
 */

public class LocationComparator implements Comparator<Pair<Double, Comment>> {

	@Override
	public int compare(Pair<Double, Comment> lhs, Pair<Double, Comment> rhs) {
		return lhs.first.compareTo(rhs.first);
	}

}
