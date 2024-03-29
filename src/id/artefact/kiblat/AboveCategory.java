package id.artefact.kiblat;

import id.artefact.kiblat.db.DatabaseHandler;
import id.artefact.kiblat.db.Post;
import id.artefact.kiblat.help.BitmapDecoder;
import id.artefact.kiblat.help.LazyAdapterAbove;
import id.artefact.kiblat.help.MemoryCache;
import id.artefact.kiblat.help.ServiceHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlrpc.android.FileHelper;
import org.xmlrpc.android.InternetHelper;
import org.xmlrpc.android.MCrypt;

import com.markupartist.android.widget.LoadMoreListView;
import com.markupartist.android.widget.PullAndLoadListView;
import com.markupartist.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class AboveCategory extends ListFragment {

	LazyAdapterAbove adapter;
	public final static String KEY_TITLE = "title";
	public final static String KEY_THUMB_URL = "thumb_url";
	public final static String KEY_DATE = "date";
	public final static String KEY_ID = "id";
	private LinkedList<String> mListItems;

	String last_id_list = "0";
	String id_category = "0";
	String tipe_category = "Analisis";
	String last_list = "0";

	ArrayList<HashMap<String, String>> postitem;
	DatabaseHandler db;
	ServiceHelper srv;

	View adsdel;
	String urlads = "";
	Bitmap bmp;
	MemoryCache memoryCache = new MemoryCache();
	FrameLayout fadsfl;
	ImageView imgads;

	public AboveCategory(String id_kategori, String nama_kategori) {
		id_category = id_kategori;
		tipe_category = nama_kategori;
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

		((PullAndLoadListView) getListView())
				.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						// Do work to refresh the list here.
						// new GetDataTask().execute();
						// execute
						new UpdateTask().execute();
					}
				});
		((PullAndLoadListView) getListView())
				.setOnLoadMoreListener(new OnLoadMoreListener() {

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub

						new LoadDataTask().execute();

					}
				});

		TextView subtitle = (TextView) getView().findViewById(R.id.subtitle);
		subtitle.setText(tipe_category);
		if (db.is_exis_cat(tipe_category) == false) {
			new UpdateTask().execute();
		}
		setList();
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
		if (id != -1) {
			// Toast.makeText(getActivity(), id_p, Toast.LENGTH_LONG).show();
			Intent i = new Intent(v.getContext(), ContentActivity.class);
			i.putExtra("id", id_p);
			startActivity(i);
		}
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
			dialog.setMessage("Refresh....");
			dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			((PullAndLoadListView) getListView()).onRefreshComplete();
			setList();
			dialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			((PullAndLoadListView) getListView()).onLoadMoreComplete();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String srvice = srv.category(id_category, "100000000000000");
				Log.i("xmlrpc", srvice);

				try {
					Log.i("xmlrpc", "try mulai insert");
					JSONArray jsonArray = new JSONArray("[" + srvice + "]");
					JSONArray innerJsonArray = jsonArray.getJSONArray(0);
					for (int i = 0; i < innerJsonArray.length(); i++) {
						JSONObject json = innerJsonArray.getJSONObject(i);
						Post p = new Post();
						p.setId_post(json.getString("ID"));
						p.setDate_post(json.getString("post_date"));
						p.setContent(json.getString("content"));
						p.setTitle(json.getString("title"));
						p.setGuid(json.getString("guid"));
						p.setTax(tipe_category);
						p.setTipe(tipe_category);
						p.setCount("");
						String url_img = json.getString("img");
						Log.i("img", url_img);
						p.setImg(url_img);
						db.addPost(p);

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

	private class LoadDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			String minibytipe = "0";
			minibytipe = db.getminidbytipe(tipe_category, tipe_category);
			if (Integer.parseInt(last_list) == Integer.parseInt(minibytipe)) {
				try {
					String srvice = srv.category(id_category, last_list);
					Log.i("xmlrpc", srvice);
					if (!getserverisnull(srvice)) {
						try {
							Log.i("xmlrpc", "try mulai insert");
							JSONArray jsonArray = new JSONArray("[" + srvice
									+ "]");
							JSONArray innerJsonArray = jsonArray
									.getJSONArray(0);
							for (int i = 0; i < innerJsonArray.length(); i++) {
								JSONObject json = innerJsonArray
										.getJSONObject(i);
								Post p = new Post();
								p.setId_post(json.getString("ID"));
								p.setDate_post(json.getString("post_date"));
								p.setContent(json.getString("content"));
								p.setTitle(json.getString("title"));
								p.setGuid(json.getString("guid"));
								p.setTax(tipe_category);
								p.setTipe(tipe_category);
								p.setCount("");
								String url_img = json.getString("img");
								Log.i("img", url_img);
								p.setImg(url_img);
								db.addPost(p);
							}

						} catch (Exception e) {
							Log.i("xmlrpc", "gagal jadi array");
						}

					}

					return null;
				} catch (Exception e) {
					// TODO: handle exception
					return null;
				}
			} else
				return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			List<Post> posts = db.getPostsByTipe(tipe_category, tipe_category,
					last_list);

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
			}

			((LazyAdapterAbove) getListAdapter()).notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			((PullAndLoadListView) getListView()).onLoadMoreComplete();

			// super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		}
	}

	public void setList() {
		Log.i("tipe", tipe_category);
		List<Post> posts = db.getPostsByTipe(tipe_category, tipe_category,
				"10000000000000");
		postitem = new ArrayList<HashMap<String, String>>();
		// creating new HashMap
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

	}

	private class AdsTask extends AsyncTask<String, Void, Boolean> {

		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (bmp != null)
				imgads.setImageBitmap(bmp);
			imgads.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent l = new Intent(Intent.ACTION_VIEW, Uri.parse(urlads));
					startActivity(l);
				}
			});
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream in = null;
			BufferedOutputStream out = null;
			try {
				String ads = srv.ads();
				Log.i("ads", ads);
				JSONArray jsonArr = new JSONArray("[" + ads + "]");
				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject json = jsonArr.getJSONObject(i);
					try {
						URL url = new URL(json.getString("image"));
						Log.i("img", json.getString("image"));
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.setDoInput(true);
						connection.connect();
						InputStream input = connection.getInputStream();
						bmp = BitmapFactory.decodeStream(input);

					} catch (IOException e) {
						e.printStackTrace();

					}
					urlads = json.getString("url");
					Log.i("url", json.getString("url"));
				}
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
	}
}