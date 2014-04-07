package ca.ualberta.lard;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ca.ualberta.lard.model.Comment;
import ca.ualberta.lard.model.GeoLocation;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

	/**
	 * Gets count of Comment list
	 */
	@Override
	public int getCount() {
		if (myList == null) {
			return 0;
		}
		else {
			return myList.size();
		}
	}

	/**
	 * Gets comment at position from Comment list
	 */
	@Override
	public Comment getItem(int position) {
		return myList.get(position);
	}

	/**
	 * Gets the Item id from a position
	 */
	@Override
	public long getItemId(int position) {
		return 0; // This is never used, but if it gets used we'll implement it
	}

	/**
	 * Gets the view used to populate a single item in the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
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
		mViewHolder.itemDistance = (TextView) convertView.findViewById(R.id.itemDistance);
		
		// Display the last known location, we'll update as the phone moves.
		GeoLocation currentLocation = new GeoLocation(this.context);
		int distanceFromComment = (int) Math.round(currentLocation.distanceFrom(comment.getLocation()));
		mViewHolder.itemDistance.setText(Integer.toString(distanceFromComment) + "m away");
		
		// Create a location listener with references to the location textview and the comment location
		final GeoLocation commentLocation = comment.getLocation();
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			
			final WeakReference<TextView> locationViewReference = new WeakReference<TextView>(mViewHolder.itemDistance);
			final WeakReference<GeoLocation> locationReference = new WeakReference<GeoLocation>(commentLocation);
			@Override
			public void onLocationChanged(Location location) {
				TextView textView = (TextView) locationViewReference.get();
				GeoLocation referencedLocation = (GeoLocation) locationReference.get();
				GeoLocation currentLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
				int distanceFromComment = (int) Math.round(currentLocation.distanceFrom(referencedLocation));
				textView.setText(Integer.toString(distanceFromComment) + "m away");
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				
			}
			
		};
		
		// Activate the location manager and request updates
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(provider, 20000, 100, locationListener);
		
		
		// NumReplies uses network, so this is done on a background thread
		mViewHolder.itemNumChildren = (TextView) convertView.findViewById(R.id.itemReplyCount);
		ANumReplies aNumReplies = new ANumReplies(mViewHolder.itemNumChildren);
		aNumReplies.execute(comment);
		if (comment.hasPicture()) {
			mViewHolder.itemIcon  = imageDetail(convertView, R.id.itemIcon, "@android:drawable/ic_menu_camera"); 
		} else {
			mViewHolder.itemIcon  = imageDetail(convertView, R.id.itemIcon, ""); 
		}
		
		return convertView;
	}
	
	/**
	 * Sets and returns a TextView that is set with the given text 
	 * @param v     Is the view
	 * @param resId Is the id of the TextView
	 * @param text  Is the text you want to set
	 * @return tv   A TextView of the detail
	 */
	private TextView detail(View v, int resId, String text) {
		TextView tv = (TextView) v.findViewById(resId);
		tv.setText(text);
		return tv;
	}
	
	/**
	 * Sets and returns an ImageView with the icon string
	 * @param v     Is the view
	 * @param resId Is the id of the imageView
	 * @param icon  Is the string of the icon you want to be set
	 * @return      An ImageView 
	 */
	private ImageView imageDetail(View v, int resId, String icon) {
		ImageView iv = (ImageView) v.findViewById(resId);
		iv.setImageResource(this.context.getResources().getIdentifier(icon, "drawable", this.context.getPackageName())); // 
		return iv;
	}
	
	/**
	 * A View Holder
	 */
	private class MyViewHolder {
		TextView itemPreview, itemAuthor, itemDistance, itemNumChildren;
		ImageView itemIcon;
	}
	
	/**
	 * Given a TextView and a comment, gives the number of replies of that comment to that TextView
	 */
	private class ANumReplies extends AsyncTask<Comment, Integer, String> {
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
    			textView.setText(result);
    		}
    	}
    }
}
