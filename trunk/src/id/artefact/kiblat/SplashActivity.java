package id.artefact.kiblat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlrpc.android.FileHelper;
import org.xmlrpc.android.InternetHelper;
import org.xmlrpc.android.MCrypt;

import id.artefact.kiblat.db.DatabaseHandler;
import id.artefact.kiblat.db.Post;
import id.artefact.kiblat.help.ServiceHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {
	private static int SPLASH_TIME_OUT = 3500;
	DatabaseHandler db = new DatabaseHandler(this);
	ServiceHelper srv = new ServiceHelper();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				
				if (!db.is_exist()) {
					new getData().execute();
					// close this activity
				} else {
					Intent i = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(i);
					finish();
				}
			}
		}, SPLASH_TIME_OUT);
	}

	public class getData extends AsyncTask<String, String, Boolean> {

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
					fh.deleteData();
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

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent i = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(i);
			finish();
		}

	}

}
