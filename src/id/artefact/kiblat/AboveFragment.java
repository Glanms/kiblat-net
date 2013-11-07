package id.artefact.kiblat;

import java.util.ArrayList;
import java.util.HashMap;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import id.artefact.kiblat.help.LazyAdapterAbove;
import id.artefact.kiblat.help.LazyAdapterBehindMenu;
import id.artefact.kiblat.help.ServiceHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AboveFragment extends ListFragment {

	LazyAdapterAbove adapter;
	public final static String KEY_TITLE = "title";
	public final static String KEY_THUMB_URL = "thumb_url";
	public final static String KEY_DATE = "date";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_above, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// SampleAdapter adapter = new SampleAdapter(getActivity());
		// for (int i = 0; i < 20; i++) {
		// adapter.add(new SampleItem("Sample List",
		// android.R.drawable.ic_menu_search));
		// }
		// setListAdapter(adapter);
		((PullToRefreshListView) getListView())
				.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						// Do work to refresh the list here.
						// new GetDataTask().execute();
					}
				});

		ArrayList<HashMap<String, String>> abovelist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 20; i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();

			// adding each child node to HashMap key => value
			map.put(KEY_TITLE,
					"Ini Title Panjang Banget Sumpeh Sampe Tiga Line Yeeeewwww");
			map.put(KEY_DATE, "2013-10-10 23:00");
			map.put(KEY_THUMB_URL, null);

			// adding HashList to ArrayList
			abovelist.add(map);
		}
		adapter = new LazyAdapterAbove(getActivity(), abovelist);
		setListAdapter(adapter);
		// execute
		new UpdateTask().execute();
	}

	private class SampleItem {
		public String tag;
		public int iconRes;

		public SampleItem(String tag, int iconRes) {
			this.tag = tag;
			this.iconRes = iconRes;
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub

		Intent i = new Intent(v.getContext(), ContentActivity.class);
		startActivity(i);
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.row_behind, null);
			}
			ImageView icon = (ImageView) convertView
					.findViewById(R.id.rowbehind_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView
					.findViewById(R.id.rowbehind_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}

	private class UpdateTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog = new ProgressDialog(getActivity());

		protected void onPreExecute() {
			dialog.setMessage("Loading....");
			dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHelper srv = new ServiceHelper();
			if (srv.getPost(getActivity()) != null) {
				return true;
			} else {
				return false;
			}
		}
	}
}
