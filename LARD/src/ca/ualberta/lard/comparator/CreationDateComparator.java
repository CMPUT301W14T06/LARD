package ca.ualberta.lard.comparator;

import java.util.Comparator;

import ca.ualberta.lard.model.Comment;

/**
 * Sorts an ArrayList of comments by creation date.
 * @author Victoria
 *
 */

public class CreationDateComparator implements Comparator<Comment>{

	@Override
	public int compare(Comment lhs, Comment rhs) {
		return rhs.getCreatedDate().compareTo(lhs.getCreatedDate());
	}

}
