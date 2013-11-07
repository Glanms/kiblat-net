package id.artefact.kiblat.help;

import id.artefact.kiblat.db.DatabaseHandler;

import java.net.URI;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlrpc.android.XMLRPCClient;

import android.content.Context;
import android.util.Log;

public class ServiceHelper {


	private URI uri = URI.create("http://192.168.21.1/kiblat-net/");
	private XMLRPCClient client = new XMLRPCClient(uri);
	
	public boolean daftar(Context contex, String nama, String email, String alamat){

		try {
			client.call("reg", nama, email, alamat);
			//JSONArray jsonArray = new JSONArray("[" + text + "]");
			//JSONArray innerJsonArray = jsonArray.getJSONArray(0);
			//JSONObject json = innerJsonArray.getJSONObject(0);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
		
		
		
	}
	
	public String getPost(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		try {
			String text = (String) client.call("getPost");
			JSONArray jsonArray = new JSONArray("[" + text + "]");
			JSONArray innerJsonArray = jsonArray.getJSONArray(0);
			if (text != null) {
				for (int i = 0; i < innerJsonArray.length(); i++) {
					JSONObject json = innerJsonArray.getJSONObject(i);
					Log.wtf("--Title--", json.getString("post_title"));
					Log.wtf("--date--", json.getString("post_date"));
					Log.wtf("--content--", json.getString("post_content"));
					Log.wtf("--image--", json.getString("meta_value"));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		return null;
		
	}


	

}
