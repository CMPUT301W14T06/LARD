package ca.ualberta.lard;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ca.ualberta.lard.model.Comment;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Inflates test_list_item views with the contents of a comment object
 * Based off https://github.com/krrishnaaaa/CustomListViewDemo/blob/master/src/pcsalt/example/customlistviewdemo/MyBaseAdapter.java
 * <p>
 * KNOWN BUGS:
 * If you scroll faster than the numreplies fetch (which you will), it'll fill the previously off-screen comments
 * with the numreplies of the last comment it fetched for.
 *
 * @author Eldon
 */

// TODO: Replace all instances of Comment with Comment when we can get comments to construct properly (???)

public class CommentListBaseAdapter extends BaseAdapter {
	private ArrayList<Comment> myList = new ArrayList<Comment>(); 
	private LayoutInflater inflater;
	private Context context;
	private MyViewHolder mViewHolder;
	
	public CommentListBaseAdapter(Context context, ArrayList<Comment> myList) {
		this.myList = myList;
		this.context = context;
		inflater = LayoutInflater.from(this.context);	// only context can also be used
	}

	@Override
	public int getCount() {
		if (myList == null) {
			return 0;
		}
		else {
			return myList.size();
		}
	}

	@Override
	public Comment getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0; // TODO: This is never used, but if it gets used we'll implement it
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Gets the view used to populate a single item in the list
		
		if(convertView == null) { 
			convertView = inflater.inflate(R.layout.thread_list_item, null);
			mViewHolder = new MyViewHolder();
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (MyViewHolder) convertView.getTag();
		}
		Comment comment = myList.get(position);
		mViewHolder.itemPreview = detail(convertView, R.id.itemPreview, comment.toString());
		mViewHolder.itemAuthor  = detail(convertView, R.id.itemAuthor,  comment.getAuthor());
		mViewHolder.itemDistance  = detail(convertView, R.id.itemDistance, "?m away"); // TODO: Actually calculate distance
		// NumReplies uses network, so this is done on a background thread
		mViewHolder.itemNumChildren = (TextView) convertView.findViewById(R.id.itemReplyCount);
		ANumReplies aNumReplies = new ANumReplies(mViewHolder.itemNumChildren);
		aNumReplies.execute(comment);
		// mViewHolder.itemIcon  = detail(convertView, R.id.itemIcon, "@android:drawable/ic_menu_camera"); // TODO: Pick icon based on whether or not the comment has a picture
		return convertView;
	}
	
	private TextView detail(View v, int resId, String text) {
		TextView tv = (TextView) v.findViewById(resId);
		tv.setText(text);
		return tv;
	}
	
	/*
	private ImageView detail(View v, int resId, int icon) {
		ImageView iv = (ImageView) v.findViewById(resId);
		iv.setImageResource(icon); // 
		
		return iv;
	}
	*/
	
	private class MyViewHolder {
		@SuppressWarnings("unused") // TODO: Remove
		TextView itemPreview, itemAuthor, itemDistance, itemNumChildren;
		//ImageView itemIcon;
	}
	private class ANumReplies extends AsyncTask<Comment, Integer, String> {
		// Given a textview and a comment, gives the numreplies of that comment to that textview
		private final WeakReference<TextView> textViewReference;
		public ANumReplies(TextView textView) {
			textViewReference = new WeakReference<TextView>(textView);
		}
    	@Override
    	protected String doInBackground(Comment... params) {
    		return (Integer.toString(params[0].numReplies()) + " replies");
    	}

    	protected void onPostExecute(String result) {
    		if (result != null) {
    			TextView textView = (TextView) textViewReference.get();
    			textView.setText(result); // TODO: Concatenate distance, num replies, author, etc
    		}
    	}
    }
}
