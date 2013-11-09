package id.artefact.kiblat.help;

import id.artefact.kiblat.db.DatabaseHandler;

import java.net.URI;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.content.Context;
import android.util.Log;

public class ServiceHelper {

	private URI uri = URI
			.create("http://192.168.1.3/kiblat-webservice/index.php/server");
	private XMLRPCClient client = new XMLRPCClient(uri);

	public boolean daftar(Context contex, String nama, String email,
			String alamat) {

		try {
			client.call("reg", nama, email, alamat);
			// JSONArray jsonArray = new JSONArray("[" + text + "]");
			// JSONArray innerJsonArray = jsonArray.getJSONArray(0);
			// JSONObject json = innerJsonArray.getJSONObject(0);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	public String getPost() {
		String text = "";
		try {
			text = (String) client.call("getPost");
		} catch (Exception e) {
			// TODO: handle exception
			text = "";
		}
		return text;
	}

	public String beritaterkini(String date) {
		String data = "";
		try {
			Log.i("xmlrpc", "masuk try");
			data = (String) client.call("beritaterkini",date);
			Log.i("xmlrpc", data);
		} catch (XMLRPCException e) {
			e.printStackTrace();
		}
		Log.i("xmlrpc", "return: " + data);
		return data;
	}

	public String tes() {
		String data = "";
		try {
			Log.i("xmlrpc", "masuk try");
			data = (String) client.call("GetPost");
			Log.i("xmlrpc", data);
		} catch (XMLRPCException e) {
			e.printStackTrace();
		}
		Log.i("xmlrpc", "return: " + data);
		return data;
	}

}
