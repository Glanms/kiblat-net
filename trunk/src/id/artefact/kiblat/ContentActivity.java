package id.artefact.kiblat;

import java.io.File;

import org.xmlrpc.android.MCrypt;

import id.artefact.kiblat.db.DatabaseHandler;
import id.artefact.kiblat.db.Post;
import id.artefact.kiblat.help.BitmapDecoder;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ContentActivity extends SherlockActivity {
	String id_post;
	String guid;
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

		ImageView home = (ImageView) findViewById(R.id.home);
		home.setBackgroundResource(R.drawable.icon_back);
		home.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(getApplicationContext(),
//						MainActivity.class);
//				startActivity(i);
				//finish();
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
		tanggal.setText(p.getDate_post());
		konten.setText(p.getContent());
		guid = p.getGuid();
		BitmapDecoder b = new BitmapDecoder();
		try {
			File f = new File("/mnt/sdcard/kiblatartefact/"
					+ mc.bytesToHex(mc.encrypt(id_post + ".jpg")) );
			if (f.exists())
				gambar.setImageBitmap(b.decodeFiles(f, 100));
				gambar.setScaleType(ScaleType.FIT_XY);
			Log.d("drawable", String.valueOf(Drawable.createFromPath(f
					.getAbsolutePath())));
		} catch (Exception e) {
			// TODO: handle exception
		}

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

}
