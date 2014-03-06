package ca.ualberta.lard;

import java.util.ArrayList;

//import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.Comment; // Will use real comments when they're constructable

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentListBaseAdapter extends BaseAdapter {
	// Inflates test_list_item views with the contents of a comment object
	// Based off https://github.com/krrishnaaaa/CustomListViewDemo/blob/master/src/pcsalt/example/customlistviewdemo/MyBaseAdapter.java
	// Replace all instances of Comment with Comment when we can get comments to construct properly
	
	ArrayList<Comment> myList = new ArrayList<Comment>(); 
	LayoutInflater inflater;
	Context context;
	
	public CommentListBaseAdapter(Context context, ArrayList<Comment> myList) {
		this.myList = myList;
		this.context = context;
		inflater = LayoutInflater.from(this.context);	// only context can also be used
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Comment getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder mViewHolder;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.thread_list_item, null);
			mViewHolder = new MyViewHolder();
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (MyViewHolder) convertView.getTag();
		}
		
		mViewHolder.itemPreview = detail(convertView, R.id.itemPreview, myList.get(position).toString()); // TODO: Truncate to an appropriate length
		mViewHolder.itemAuthor  = detail(convertView, R.id.itemAuthor,  myList.get(position).getAuthor()); // TODO: Concatenate distance, num replies, author, etc
		mViewHolder.itemDistance  = detail(convertView, R.id.itemDistance, "?m away"); // TODO: Actually calculate distance
		mViewHolder.itemNumChildren  = detail(convertView, R.id.itemReplyCount,
				Integer.toString(myList.get(position).numReplies()) + " replies"); // TODO: Concatenate distance, num replies, author, etc
		
		// mViewHolder.itemIcon  = detail(convertView, R.id.itemIcon, "@android:drawable/ic_menu_camera"); // TODO: Pick icon based on whether or not the comment has a picture
		
		return convertView;
	}
	
	// or you can try better way
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
		TextView itemPreview, itemAuthor, itemDistance, itemNumChildren;
		//ImageView itemIcon;
	}

}
