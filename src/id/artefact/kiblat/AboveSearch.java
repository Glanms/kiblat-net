package id.artefact.kiblat;



import id.artefact.kiblat.db.DatabaseHandler;
import id.artefact.kiblat.db.Post;
import id.artefact.kiblat.help.LazyAdapterAbove;
import id.artefact.kiblat.help.MemoryCache;
import id.artefact.kiblat.help.ServiceHelper;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlrpc.android.FileHelper;
import org.xmlrpc.android.InternetHelper;
import org.xmlrpc.android.MCrypt;

import com.markupartist.android.widget.PullAndLoadListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class AboveSearch extends ListFragment {

	LazyAdapterAbove adapter;
	public final static String KEY_TITLE = "title";
	public final static String KEY_THUMB_URL = "thumb_url";
	public final static String KEY_DATE = "date";
	public final static String KEY_ID = "id";
	private LinkedList<String> mListItems;

	String last_id_list = "0";

	String last_list = "0";
	String tipe = "";
	String param ="";
	ArrayList<HashMap<String, String>> postitem;
	DatabaseHandler db;
	ServiceHelper srv;
	View header;
	View adsdel;
	String urlads = "";
	Bitmap bmp;
	MemoryCache memoryCache = new MemoryCache();
	FrameLayout fadsfl;
	ImageView imgads;

	public AboveSearch(String tipez, String paramz ) {
		tipe = tipez;
		param = paramz;
	}

	public AboveSearch() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.list_above, null);

	}

	@SuppressLint("NewApi")
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db = new DatabaseHandler(getActivity());
		srv = new ServiceHelper();
		header = getActivity().getLayoutInflater().inflate(R.layout.headerlist,
				null);
		TextView subtitle = (TextView) getView().findViewById(R.id.subtitle);
		subtitle.setText(tipe);
		new UpdateTask().execute();
		new AdsTask().execute();
		Button clsads = (Button) getActivity().findViewById(R.id.clsikl);
		fadsfl = (FrameLayout) getActivity().findViewById(R.id.adsfl);
		fadsfl.setVisibility(View.VISIBLE);
		imgads = (ImageView) getActivity().findViewById(R.id.imgads);
		clsads.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fadsfl.setVisibility(View.GONE);
			}
		});
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
		TextView id_post = (TextView) v.findViewById(R.id.id);
		String id_p = id_post.getText().toString();
		//Toast.makeText(getActivity(), id_p, Toast.LENGTH_LONG).show();
		Intent i = new Intent(v.getContext(), ContentActivity.class);
		i.putExtra("id", id_p);
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
			
			setList();
			dialog.dismiss();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			InternetHelper inet = new InternetHelper();
			MCrypt mc = new MCrypt();
			byte[] en;
			try {
				String srvsearch = srv.searchberita(param);
				Log.i("xmlrpc", srvsearch);

				try {
					Log.i("xmlrpc", "try mulai insert");
					JSONArray jsonArray = new JSONArray("[" + srvsearch
							+ "]");
					JSONArray innerJsonArray = jsonArray.getJSONArray(0);

					FileHelper fh = new FileHelper();
					db.deletePostbyTipe(tipe);
					Log.i("xmlrpc", "deleted");
					for (int i = 0; i < innerJsonArray.length(); i++) {
						JSONObject json = innerJsonArray.getJSONObject(i);
						Post p = new Post();
						p.setId_post(json.getString("ID"));
						p.setDate_post(json.getString("post_date"));
						p.setContent(json.getString("content"));
						p.setTitle(json.getString("title"));
						p.setGuid(json.getString("guid"));
						p.setTax("");
						p.setTipe(tipe);
						p.setCount(json.getString("count"));
						String url_img = json.getString("img");
						Log.i("img", url_img);
						p.setImg(url_img);
						db.addPost(p);
						Log.i("xmlrpc", "insert");
					}
				} catch (Exception e) {
					Log.i("xmlrpc", "gagal jadi array");
				}
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
	}

	public Boolean getserverisnull(String dariserver) {
		if (dariserver.equals("[]"))
			return true;
		else
			return false;
	}

	public void setList() {

		List<Post> posts = db.getPostsByTipe(tipe, "", "10000000000000");
		postitem = new ArrayList<HashMap<String, String>>();
		// creating new HashMap
		getListView().removeHeaderView(header);
		int y = 0;
		for (Post p : posts) {
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value

			map.put(KEY_ID, p.getId_post().toString());
			last_list = p.getId_post().toString();
			map.put(KEY_TITLE, p.getTitle().toString());
			map.put(KEY_DATE, p.getDate_post().toString());
			map.put(KEY_THUMB_URL, p.getImg().toString());
			// adding HashList to ArrayList
			postitem.add(map);

			y++;
		}
		adapter = new LazyAdapterAbove(getActivity(), postitem);
		setListAdapter(adapter);

		header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(getActivity(), "asdasd",
				// Toast.LENGTH_LONG).show();
			}
		});

	}
	private class AdsTask extends AsyncTask<String, Void, Boolean> {

		protected void onPreExecute() {
			// dialog.setMessage("Loading....");
			// dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			// ((PullAndLoadListView) getListView()).onRefreshComplete();
			// tampilads();
			// dialog.dismiss();
			// imgads.set
			if (bmp != null)
				imgads.setImageBitmap(bmp);

		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				urlads = srv.ads();
				Log.i("ads", urlads);
				try {
					URL url = new URL(urlads);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					bmp = BitmapFactory.decodeStream(input);

				} catch (IOException e) {
					e.printStackTrace();

				}

				return true;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
	}
}