package id.artefact.kiblat;

import id.artefact.kiblat.help.LazyAdapterBehindMenu;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BehindFragment extends ListFragment {
	public final static String KEY_TITLE = "title";
	public final static String KEY_THUMB_URL = "thumb_url";
	LazyAdapterBehindMenu adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_behind, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] colors = getResources().getStringArray(R.array.color_names);

		ArrayList<HashMap<String, String>> colorlist = new ArrayList<HashMap<String, String>>();
		// looping through all song nodes <song>
		for (String c : colors) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();

			// adding each child node to HashMap key => value
			map.put(KEY_TITLE, c);

			map.put(KEY_THUMB_URL, null);

			// adding HashList to ArrayList
			colorlist.add(map);
		}

		//list = (ListView) findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
		adapter = new LazyAdapterBehindMenu(getActivity(), colorlist);
		setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			newContent = new ColorFragment(R.color.red);
			break;
		case 1:
			newContent = new ColorFragment(R.color.green);
			break;
		case 2:
			newContent = new ColorFragment(R.color.blue);
			break;
		case 3:
			newContent = new ColorFragment(android.R.color.white);
			break;
		case 4:
			newContent = new ColorFragment(android.R.color.black);
			break;
		case 5:
			newContent = new ColorFragment(R.color.biru_danker);
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		} else {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
