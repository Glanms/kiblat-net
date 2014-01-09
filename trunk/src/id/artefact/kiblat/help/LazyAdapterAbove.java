package id.artefact.kiblat.help;

import id.artefact.kiblat.AboveFragment;
import id.artefact.kiblat.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlrpc.android.MCrypt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class LazyAdapterAbove extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public LazyAdapterAbove(Activity a, ArrayList<HashMap<String, String>> d) {
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
			vi = inflater.inflate(R.layout.row_above, null);
		TextView date = (TextView) vi.findViewById(R.id.rowabove_date);
		TextView title = (TextView) vi.findViewById(R.id.rowabove_title); // title
		TextView id = (TextView) vi.findViewById(R.id.id);
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.rowabove_icon); // thumb
																					// image

		HashMap<String, String> behindmenu = new HashMap<String, String>();
		behindmenu = data.get(position);

		// Setting all values in listview
		title.setText(behindmenu.get(AboveFragment.KEY_TITLE));
		date.setText(behindmenu.get(AboveFragment.KEY_DATE));
		id.setText(behindmenu.get(AboveFragment.KEY_ID));
		if (behindmenu.get(AboveFragment.KEY_THUMB_URL) != null) {
			imageLoader.DisplayImage(behindmenu.get(AboveFragment.KEY_THUMB_URL), thumb_image);
		}else{
			thumb_image.setBackgroundResource(R.drawable.no_image);
		}
		return vi;
	}
}