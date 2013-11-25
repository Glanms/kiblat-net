package id.artefact.kiblat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import android.widget.BaseAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlrpc.android.FileHelper;
import org.xmlrpc.android.InternetHelper;
import org.xmlrpc.android.MCrypt;

import com.markupartist.android.widget.LoadMoreListView;
import com.markupartist.android.widget.PullAndLoadListView;
import com.markupartist.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import id.artefact.kiblat.db.DatabaseHandler;
import id.artefact.kiblat.db.Post;
import id.artefact.kiblat.help.BitmapDecoder;
import id.artefact.kiblat.help.LazyAdapterAbove;
import id.artefact.kiblat.help.LazyAdapterBehindMenu;
import id.artefact.kiblat.help.ServiceHelper;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Im;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class AboveFragment extends ListFragment {

	LazyAdapterAbove adapter;
	public final static String KEY_TITLE = "title";
	public final static String KEY_THUMB_URL = "thumb_url";
	public final static String KEY_DATE = "date";
	public final static String KEY_ID = "id";
	private LinkedList<String> mListItems;

	String last_id_list = "0";

	String last_list = "0";
	String tipe = "";
	ArrayList<HashMap<String, String>> postitem;
	DatabaseHandler db;
	ServiceHelper srv;
	View header;

	public AboveFragment(String tipez) {
		tipe = tipez;

	}

	public AboveFragment() {
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
		((PullAndLoadListView) getListView())
				.setOnLoadMoreListener(new OnLoadMoreListener() {

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub

						new LoadDataTask().execute();

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
		TextView id_post = (TextView) v.findViewById(R.id.id);
		String id_p = id_post.getText().toString();
		Toast.makeText(getActivity(), id_p, Toast.LENGTH_LONG).show();
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
			InternetHelper inet = new InternetHelper();
			MCrypt mc = new MCrypt();
			byte[] en;
			try {
				String srvberitaterkini = srv.beritaterkini("100000000000000");
				Log.i("xmlrpc", srvberitaterkini);

				try {
					Log.i("xmlrpc", "try mulai insert");
					JSONArray jsonArray = new JSONArray("[" + srvberitaterkini
							+ "]");
					JSONArray innerJsonArray = jsonArray.getJSONArray(0);

					FileHelper fh = new FileHelper();
					db.deletePostbyTipe("terkini");
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
						p.setTipe("terkini");
						p.setCount("");
						String url_img = json.getString("img");
						Log.i("img", url_img);
						// donlod gambar disini
						// kalau berhasil disimpen path nya
						if (url_img != null) {
							try {
								en = mc.encrypt(json.getString("ID") + ".jpg");
								inet.downloadImage(url_img, mc.bytesToHex(en));
								Log.i("download", json.getString("ID") + ".jpg");
								p.setImg(json.getString("ID") + ".jpg");
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						} else {
							p.setImg(null);
						}

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

	private class LoadDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// last_id_list = db.getminidterkini();
			// Log.i("xmlrpc", "min id " + last_id_list);
			InternetHelper inet = new InternetHelper();
			MCrypt mc = new MCrypt();
			byte[] en;

			if (Integer.parseInt(last_list) == Integer.parseInt(db
					.getminidbytipe("terkini", ""))) {
				try {
					String srvberitaterkini = srv.beritaterkini(last_list);
					Log.i("xmlrpc", srvberitaterkini);
					if (!getserverisnull(srvberitaterkini)) {
						try {
							Log.i("xmlrpc", "try mulai insert");
							JSONArray jsonArray = new JSONArray("["
									+ srvberitaterkini + "]");
							JSONArray innerJsonArray = jsonArray
									.getJSONArray(0);
							// db.deletePostbyTipe("terkini");
							Log.i("xmlrpc", "deleted");
							for (int i = 0; i < innerJsonArray.length(); i++) {
								JSONObject json = innerJsonArray
										.getJSONObject(i);
								Post p = new Post();
								p.setId_post(json.getString("ID"));
								p.setDate_post(json.getString("post_date"));
								p.setContent(json.getString("content"));
								p.setTitle(json.getString("title"));
								p.setGuid(json.getString("guid"));
								p.setTax("");
								p.setTipe("terkini");
								p.setCount("");
								String url_img = json.getString("img");
								Log.i("img", url_img);
								// donlod gambar disini
								// kalau berhasil disimpen path nya
								if (url_img != null) {
									try {
										en = mc.encrypt(json.getString("ID")
												+ ".jpg");
										inet.downloadImage(url_img,
												mc.bytesToHex(en));
										Log.i("download", json.getString("ID")
												+ ".jpg");
										p.setImg(json.getString("ID") + ".jpg");
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
								} else {
									p.setImg(null);
								}
								db.addPost(p);
								Log.i("xmlrpc", "insert");
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

			List<Post> posts = db.getPostsByTipe("terkini", "", last_list);

			for (Post p : posts) {
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key => value
				map.put(KEY_ID, p.getId_post().toString());
				last_list = p.getId_post().toString();
				map.put(KEY_TITLE, p.getTitle().toString());
				map.put(KEY_DATE, p.getDate_post().toString());
				map.put(KEY_THUMB_URL, p.getImg());
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

		List<Post> posts = db.getPostsByTipe("terkini", "", "10000000000000");
		postitem = new ArrayList<HashMap<String, String>>();
		// creating new HashMap
		getListView().removeHeaderView(header);
		int y = 0;
		for (Post p : posts) {
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			if (y == 0) {
				TextView title = (TextView) header.findViewById(R.id.headJudul);
				TextView tgl = (TextView) header.findViewById(R.id.headerDate);
				RelativeLayout ndas = (RelativeLayout) header
						.findViewById(R.id.headImg);
				BitmapDecoder b = new BitmapDecoder();
				MCrypt mc = new MCrypt();
				File f;
				try {
					f = new File(
							"/mnt/sdcard/kiblatartefact/"
									+ mc.bytesToHex(mc.encrypt(p.getId_post()
											+ ".jpg")));
					Bitmap bm = b.decodeFiles(f, 100);
					Drawable d = new BitmapDrawable(getResources(), bm);
					ndas.setBackgroundDrawable(d);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				title.setText(p.getTitle().toString());
				tgl.setText(p.getDate_post().toString());
				getListView().addHeaderView(header);
				final String id_p = p.getId_post().toString();
				title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(v.getContext(),
								ContentActivity.class);
						i.putExtra("id", id_p);
						startActivity(i);
					}
				});
			} else {
				map.put(KEY_ID, p.getId_post().toString());
				last_list = p.getId_post().toString();
				map.put(KEY_TITLE, p.getTitle().toString());
				map.put(KEY_DATE, p.getDate_post().toString());
				map.put(KEY_THUMB_URL, p.getImg());
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
				// Toast.makeText(getActivity(), "asdasd",
				// Toast.LENGTH_LONG).show();
			}
		});

	}

}
