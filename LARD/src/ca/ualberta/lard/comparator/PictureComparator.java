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
		Integer lhsHasPic = 0; // 0 if no pic, 1 if has pic
		Integer rhsHasPic = 0; 
		if (lhs.hasPicture()) {
			lhsHasPic = 1;
		}
		if (rhs.hasPicture()) {
			rhsHasPic = 1;
		}
		return lhsHasPic.compareTo(rhsHasPic);
	}

}
