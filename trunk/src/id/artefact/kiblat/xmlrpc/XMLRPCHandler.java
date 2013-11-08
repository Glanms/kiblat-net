package id.artefact.kiblat.xmlrpc;

import java.net.URI;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.util.Log;

public class XMLRPCHandler {
	private XMLRPCClient client;
	private URI uri;

	String text;

	public XMLRPCHandler() {
		uri = URI
				.create("http://192.168.1.3/pmi_server/index.php/xmlrpc_server");
		client = new XMLRPCClient(uri);
	}

	public String beritaterkini() {
		String data = "";
		try {
			Log.i("xmlrpc", "masuk try");
			data = (String) client.call("GetUdd");
			Log.i("xmlrpc", data);
		} catch (XMLRPCException e) {
			e.printStackTrace();
		}
		Log.i("xmlrpc", "return: "+data);
		return data;
	}
	

	
	public String tes() {
		String data = "hahahahahahaha";
	
		return data;
	}
	
	public String ambiludd()
	{
		String data="";
		try {
			 data = (String) client.call("GetUdd");
			 Log.i("udd", data);
		} catch (XMLRPCException e) {
			Log.i("udd", "gak jalan");
		}
		return data;
	}
	
}
