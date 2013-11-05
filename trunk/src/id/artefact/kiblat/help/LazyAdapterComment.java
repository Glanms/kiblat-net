package id.artefact.kiblat.help;


import id.artefact.kiblat.CommentActivity;

import id.artefact.kiblat.BehindFragment;

import id.artefact.kiblat.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapterComment extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public LazyAdapterComment(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.row_comment, null);
		TextView date = (TextView) vi.findViewById(R.id.rowcomment_date);
		TextView title = (TextView) vi.findViewById(R.id.rowcomment_isi); // title

		ImageView thumb_image = (ImageView) vi.findViewById(R.id.rowcomment_icon); // thumb
																					// image

		HashMap<String, String> commentlist = new HashMap<String, String>();
		commentlist = data.get(position);

		// Setting all values in listview
		title.setText(commentlist.get(CommentActivity.KEY_TITLE));
		date.setText(commentlist.get(CommentActivity.KEY_DATE));

		imageLoader.DisplayImage(commentlist.get(CommentActivity.KEY_THUMB_URL),
				thumb_image);
		return vi;
	}
}