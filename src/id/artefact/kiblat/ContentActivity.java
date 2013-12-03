package id.artefact.kiblat;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlrpc.android.MCrypt;

import id.artefact.kiblat.db.DatabaseHandler;
import id.artefact.kiblat.db.Post;
import id.artefact.kiblat.help.BitmapDecoder;
import id.artefact.kiblat.help.CustomAdapter;
import id.artefact.kiblat.help.FormatDate;
import id.artefact.kiblat.help.LazyAdapterAbove;
import id.artefact.kiblat.help.ScrollViewHelper;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContentActivity extends SherlockActivity{
	String id_post;
	String guid;
	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

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
				// TODO Auto-generated method stub
				// Intent i = new Intent(getApplicationContext(),
				// MainActivity.class);
				// startActivity(i);
				// finish();
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
		DatabaseHandler db = new DatabaseHandler(this);
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
		konten.setText(result.replaceAll("<[^>]*>", "")
				.replaceAll("&nbsp;", ""));
		guid = p.getGuid();
		BitmapDecoder b = new BitmapDecoder();
		try {
			File f = new File("/mnt/sdcard/kiblatartefact/"
					+ mc.bytesToHex(mc.encrypt(id_post + ".jpg")));
			if (f.exists())
				gambar.setImageBitmap(b.decodeFiles(f, 100));
			gambar.setScaleType(ScaleType.FIT_XY);
			Log.d("drawable", String.valueOf(Drawable.createFromPath(f
					.getAbsolutePath())));
		} catch (Exception e) {
			// TODO: handle exception
		}

		TextView closeiklan = (TextView) findViewById(R.id.closeiklan);
		closeiklan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout iklan = (LinearLayout) findViewById(R.id.iklan);
				iklan.setVisibility(View.GONE);

			}
		});
		
		setRelated(p.getTipe());

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
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/html");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					Html.fromHtml(guid));
			startActivity(Intent.createChooser(sharingIntent, "Share using"));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setRelated(String tipe) {
		ListView list = (ListView) findViewById(R.id.related);
		ArrayList<HashMap<String, Object>> postitem = new ArrayList<HashMap<String, Object>>();
		ListAdapter adapter;
		String KEY_ID = "id", KEY_TITLE ="title";
		DatabaseHandler db = new DatabaseHandler(getBaseContext());
		List<Post> related = db.getRelatedPost(tipe);
		for (Post p : related) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(KEY_ID, p.getId_post());
			map.put(KEY_TITLE, p.getTitle());
			postitem.add(map);
		}
		adapter = new CustomAdapter(getApplicationContext(), postitem, R.layout.list_related, new String[] {KEY_ID, KEY_TITLE}, new int[] { R.id.idpost, R.id.text_related});
		list.setAdapter(adapter);
		ScrollViewHelper.getListViewSize(list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView idpost = (TextView) arg1.findViewById(R.id.idpost);
				String id = idpost.getText().toString();
			    Intent i = new Intent(ContentActivity.this,ContentActivity.class);
		        i.putExtra("id", id);
			    startActivity(i);
			}
		});
	}


}
