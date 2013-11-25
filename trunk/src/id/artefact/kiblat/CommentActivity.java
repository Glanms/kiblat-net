package id.artefact.kiblat;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;



public class CommentActivity extends SherlockActivity {
	String id_post;
	String guid;
	WebView webDisqus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_comment);
		
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#FFFFFF")));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(R.layout.actionbar_custom);
		Bundle extras = getIntent().getExtras();
		id_post = extras.getString("id");
		guid = extras.getString("guid");
		
		ImageView logo = (ImageView) findViewById(R.id.kiblat_logo);
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		ImageView home = (ImageView) findViewById(R.id.home);
		home.setBackgroundResource(R.drawable.icon_back);
		home.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), ContentActivity.class);
				i.putExtra("id", id_post);
				startActivity(i);
			}
		});
		
		
		String htmlComments = getHtmlComment(id_post +" "+guid, "kiblatnet");
		Log.d("identifier", id_post+" "+guid);
		webDisqus = (WebView) findViewById(R.id.disqus);
		// set up disqus
		WebSettings webSettings2 = webDisqus.getSettings();
		webSettings2.setJavaScriptEnabled(true);
		webSettings2.setBuiltInZoomControls(true);
		webDisqus.requestFocusFromTouch();
		webDisqus.setWebViewClient(new WebViewClient());
		webDisqus.setWebChromeClient(new WebChromeClient());
		webDisqus.loadData(htmlComments, "text/html", null);
	}

	public String getHtmlComment(String idPost, String shortName) {
		 
		return "<div id='disqus_thread'></div>"
				+ "<script type='text/javascript'>"
				+ "var disqus_identifier = '"
				+ idPost
				+ "';"
				+ "var disqus_shortname = '"
				+ shortName
				+ "';"
				+ " (function() { var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;"
				+ "dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';"
				+ "(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq); })();"
				+ "</script>";
	}


}
