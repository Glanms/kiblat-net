package id.artefact.kiblat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.markupartist.android.widget.PullAndLoadListView;
import com.markupartist.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import id.artefact.kiblat.db.DatabaseHandler;
import id.artefact.kiblat.db.Post;
import id.artefact.kiblat.help.LazyAdapterAbove;
import id.artefact.kiblat.help.LazyAdapterBehindMenu;
import id.artefact.kiblat.help.ServiceHelper;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AboveFragment extends ListFragment {

	LazyAdapterAbove adapter;
	public final static String KEY_TITLE = "title";
	public final static String KEY_THUMB_URL = "thumb_url";
	public final static String KEY_DATE = "date";

	DatabaseHandler db;
	ServiceHelper srv;
	View header;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_above, null);
	}

	@SuppressLint("NewApi")
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db = new DatabaseHandler(getActivity());
		srv = new ServiceHelper();
		header = getActivity().getLayoutInflater().inflate(
				R.layout.headerlist, null);

		// SampleAdapter adapter = new SampleAdapter(getActivity());
		// for (int i = 0; i < 20; i++) {
		// adapter.add(new SampleItem("Sample List",
		// android.R.drawable.ic_menu_search));
		// }
		// setListAdapter(adapter);
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
	     ((PullAndLoadListView) getListView()).setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
				new LoadTask().execute();
				
			}
		});


		setList();
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
			// dialog.setMessage("Loading....");
			// dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			((PullAndLoadListView) getListView()).onRefreshComplete();
			setList();
			// dialog.dismiss();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String srvberitaterkini = srv.beritaterkini();
				Log.i("xmlrpc", srvberitaterkini);

				try {
					Log.i("xmlrpc", "try mulai insert");
					JSONArray jsonArray = new JSONArray("[" + srvberitaterkini
							+ "]");
					JSONArray innerJsonArray = jsonArray.getJSONArray(0);
					db.deletePostbyTipe("terkini");
					Log.i("xmlrpc", "deleted");
					for (int i = 0; i < innerJsonArray.length(); i++) {
						JSONObject json = innerJsonArray.getJSONObject(i);
						Post p = new Post();
						p.setId_post(json.getString("ID"));
						p.setDate_post(json.getString("post_date"));
						p.setContent(json.getString("post_content"));
						p.setTitle(json.getString("post_title"));
						p.setGuid(json.getString("guid"));
						p.setTax(json.getString("name"));
						p.setTipe("terkini");
						p.setCount("");
						String url_img=json.getString("img");
						Log.i("img",url_img);
						//donlod gambar disini
						//kalau berhasil disimpen path nya
						
						p.setImg("diisi path");
						
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
	
	private class LoadTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog = new ProgressDialog(getActivity());

		protected void onPreExecute() {
			// dialog.setMessage("Loading....");
			// dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			((PullAndLoadListView) getListView()).onRefreshComplete();
			setList();
			// dialog.dismiss();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
//			try {
//				String srvberitaterkini = srv.beritaterkini();
//				Log.i("xmlrpc", srvberitaterkini);
//
//				try {
//					Log.i("xmlrpc", "try mulai insert");
//					JSONArray jsonArray = new JSONArray("[" + srvberitaterkini
//							+ "]");
//					JSONArray innerJsonArray = jsonArray.getJSONArray(0);
//					db.deletePostbyTipe("terkini");
//					Log.i("xmlrpc", "deleted");
//					for (int i = 0; i < innerJsonArray.length(); i++) {
//						JSONObject json = innerJsonArray.getJSONObject(i);
//						Post p = new Post();
//						p.setId_post(json.getString("ID"));
//						p.setDate_post(json.getString("post_date"));
//						p.setContent(json.getString("post_content"));
//						p.setTitle(json.getString("post_title"));
//						p.setGuid(json.getString("guid"));
//						p.setTax(json.getString("name"));
//						p.setTipe("terkini");
//						p.setCount("");
//						String url_img=json.getString("img");
//						Log.i("img",url_img);
//						//donlod gambar disini
//						//kalau berhasil disimpen path nya
//						
//						p.setImg("diisi path");
//						
//						db.addPost(p);
//						Log.i("xmlrpc", "insert");
//					}
//				} catch (Exception e) {
//					Log.i("xmlrpc", "gagal jadi array");
//				}
//				return true;
//			} catch (Exception e) {
//				// TODO: handle exception
//				return false;
//			}
			Log.wtf("Loadmore", "You've got mine !");
			return true;
		}
	}

	public void setList() {
		
		List<Post> posts = db.getPostsByTipe("terkini");
		ArrayList<HashMap<String, String>> postitem = new ArrayList<HashMap<String, String>>();
		// creating new HashMap
		getListView().removeHeaderView(header);
		int y = 0;
		for (Post p : posts) {
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			if (y == 0) {
				TextView title= (TextView) header.findViewById(R.id.headJudul);
				TextView tgl= (TextView) header.findViewById(R.id.headerDate);
				title.setText(p.getTitle().toString());
				tgl.setText(p.getDate_post().toString());
				getListView().addHeaderView(header);
			} else {
				map.put(KEY_TITLE, p.getTitle().toString());
				map.put(KEY_DATE, p.getDate_post().toString());
				map.put(KEY_THUMB_URL, null);
				// adding HashList to ArrayList
				postitem.add(map);
			}

			y++;
		}
		adapter = new LazyAdapterAbove(getActivity(), postitem);
		setListAdapter(adapter);
		
		header.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "asdasd", Toast.LENGTH_LONG).show();
			}
		});
		
		
		
	}

}
