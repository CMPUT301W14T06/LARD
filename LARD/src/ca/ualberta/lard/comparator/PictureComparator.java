package ca.ualberta.lard.comparator;

import java.util.Comparator;

import ca.ualberta.lard.model.Comment;

/**
 * Sorts an ArrayList of comments by putting pictures first.
 * @author Victoria
 *
 */

public class PictureComparator implements Comparator<Comment>{

	@Override
	public int compare(Comment lhs, Comment rhs) {
		/* If there is no picture set value to one, because we want
		* comments with pictures to be put in front, and comparators
		* use ascending order. */
		Integer lhsHasPic = 1; // 1 if no pic, 0 if has pic
		Integer rhsHasPic = 1; 
		if (lhs.hasPicture()) {
			lhsHasPic = 0;
		}
		if (rhs.hasPicture()) {
			rhsHasPic = 0;
		}
		return lhsHasPic.compareTo(rhsHasPic);
	}

}
