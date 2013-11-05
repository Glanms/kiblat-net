package id.artefact.kiblat;


import id.artefact.kiblat.help.LazyAdapterComment;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;



import android.view.View;

import android.view.View.OnClickListener;
import android.widget.ImageView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class CommentActivity extends SherlockActivity {

	LazyAdapterComment adapter;
	public final static String KEY_TITLE = "title";
	public final static String KEY_THUMB_URL = "thumb_url";
	public final static String KEY_DATE = "date";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_comment);
		
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#FFFFFF")));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(R.layout.actionbar_custom);
		
		ImageView logo = (ImageView) findViewById(R.id.kiblat_logo);
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
			}
		});
		
		ImageView home = (ImageView) findViewById(R.id.home);
		home.setBackgroundResource(R.drawable.ic_launcher);
		home.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
			}
		});
	}
	

	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.kosong, menu);
		return true;
	}


}
