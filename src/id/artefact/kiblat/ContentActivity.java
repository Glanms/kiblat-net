package id.artefact.kiblat;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlrpc.android.MCrypt;


import id.artefact.kiblat.db.DatabaseHandler;
import id.artefact.kiblat.db.Post;
import id.artefact.kiblat.help.CustomAdapter;
import id.artefact.kiblat.help.FormatDate;
import id.artefact.kiblat.help.ImageLoader;
import id.artefact.kiblat.help.MemoryCache;
import id.artefact.kiblat.help.ScrollViewHelper;
import id.artefact.kiblat.help.ServiceHelper;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import android.widget.AdapterView.OnItemClickListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContentActivity extends SherlockActivity {
	String id_post;
	String guid;
	View adsdel;
	String urlads = "";
	Bitmap bmp;
	MemoryCache memoryCache = new MemoryCache();
	FrameLayout fadsfl;
	ImageView imgads;
	ServiceHelper srv;
	DatabaseHandler db = new DatabaseHandler(this);

	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		srv = new ServiceHelper();
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#FFFFFF")));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar_custom);

		ImageView logo = (ImageView) findViewById(R.id.kiblat_logo);
		logo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
			}
		});
		FormatDate fd = new FormatDate();
		ImageView home = (ImageView) findViewById(R.id.home);
		home.setBackgroundResource(R.drawable.icon_back);
		home.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});
		TextView title = (TextView) findViewById(R.id.postTitle);
		TextView tanggal = (TextView) findViewById(R.id.postDate);
		ImageView gambar = (ImageView) findViewById(R.id.postImage);
		TextView konten = (TextView) findViewById(R.id.postContent);
		MCrypt mc = new MCrypt();
		Bundle extras = getIntent().getExtras();
		id_post = extras.getString("id");
		Post p = db.getPostById(id_post);
		title.setText(p.getTitle());
		tanggal.setText(fd.convert(p.getDate_post()));
		// konten.setText(Html.fromHtml(p.getContent()));
		InputStream is = null;
		BufferedReader reader;
		String result = p.getContent();
		try {
			reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// System.out.print(result.substring(startIndex + 19, EndIndex));
		konten.setText(result
				.replaceAll("(?s)<!--\\[if(.*?)\\[endif\\] *-->", "")
				.replaceAll("</p>", "\n").replaceAll("<[^>]*>", "")
				.replaceAll("&nbsp;", ""));
		guid = p.getGuid().toString();
		String url = p.getImg().toString();
		URL onlineUrl;
		if (isNetworkAvailable(getApplicationContext()) == true) {
			try {
				onlineUrl = new URL(url);
				new imageTask(gambar).execute(onlineUrl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			ImageLoader imgLoad = new ImageLoader(getApplicationContext());
			imgLoad.DisplayImage(url, gambar);
		}
		Log.d("--url gambar--", url);
		new AdsTask().execute();
		Button clsads = (Button) this.findViewById(R.id.clsikl);
		fadsfl = (FrameLayout) this.findViewById(R.id.adsfl);
		fadsfl.setVisibility(View.VISIBLE);
		imgads = (ImageView) this.findViewById(R.id.imgads);
		clsads.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fadsfl.setVisibility(View.GONE);
			}
		});
		setRelated(p.getTipe());

	}

	private class imageTask extends AsyncTask<URL, Void, Bitmap> {
		ImageView tIV;

		public imageTask(ImageView iv) {
			tIV = iv;
		}

		@Override
		protected Bitmap doInBackground(URL... params) {
			// TODO Auto-generated method stub
			Bitmap networkBitmap = null;

			URL networkUrl = params[0]; // Load the first element
			try {
				networkBitmap = BitmapFactory.decodeStream(networkUrl
						.openConnection().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			return networkBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			tIV.setImageBitmap(result);
		}

	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (mConnectivityManager != null && mConnectivityManager
				.getActiveNetworkInfo().isConnectedOrConnecting()) ? true
				: false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu("");
		subMenu1.add(0, 1, Menu.NONE, "Comment");
		subMenu1.add(0, 2, Menu.NONE, "Share");

		MenuItem SubMenu1Item = subMenu1.getItem();
		SubMenu1Item.setIcon(R.drawable.ic_action_overflow);
		SubMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent icomment = new Intent(this, CommentActivity.class);
			icomment.putExtra("id", id_post);
			icomment.putExtra("guid", guid);
			startActivity(icomment);
			return true;
		case 2:
			shareIt();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void shareIt() {
		Post p = db.getPostById(id_post);
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, p.getTitle().toString());
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, p.getGuid().toString());
		startActivity(Intent.createChooser(sharingIntent, "Share via Kiblat.Net for Android"));
	}
	
	


	public void setRelated(String tipe) {
		ListView list = (ListView) findViewById(R.id.related);
		ArrayList<HashMap<String, Object>> postitem = new ArrayList<HashMap<String, Object>>();
		ListAdapter adapter;
		String KEY_ID = "id", KEY_TITLE = "title";
		DatabaseHandler db = new DatabaseHandler(getBaseContext());
		List<Post> related = db.getRelatedPost(tipe);
		for (Post p : related) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(KEY_ID, p.getId_post());
			map.put(KEY_TITLE, p.getTitle());
			postitem.add(map);
		}
		adapter = new CustomAdapter(getApplicationContext(), postitem,
				R.layout.list_related, new String[] { KEY_ID, KEY_TITLE },
				new int[] { R.id.idpost, R.id.text_related });
		list.setAdapter(adapter);
		ScrollViewHelper.getListViewSize(list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView idpost = (TextView) arg1.findViewById(R.id.idpost);
				String id = idpost.getText().toString();
				Intent i = new Intent(ContentActivity.this,
						ContentActivity.class);
				i.putExtra("id", id);
				startActivity(i);
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
