package id.artefact.kiblat;

import id.artefact.kiblat.help.EntryAdapter;
import id.artefact.kiblat.help.EntryItem;
import id.artefact.kiblat.help.LazyAdapterBehindMenu;
import id.artefact.kiblat.help.SectionItem;

import java.util.ArrayList;

import id.artefact.kiblat.help.Item;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class BehindFragment extends ListFragment {
	public final static String KEY_TITLE = "title";
	public final static String KEY_THUMB_URL = "thumb_url";

	ArrayList<Item> items = new ArrayList<Item>();

	LazyAdapterBehindMenu adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_behind, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// String[] colors = getResources().getStringArray(R.array.color_names);
		//
		// ArrayList<HashMap<String, String>> colorlist = new
		// ArrayList<HashMap<String, String>>();
		// // looping through all song nodes <song>
		// for (String c : colors) {
		// // creating new HashMap
		// HashMap<String, String> map = new HashMap<String, String>();
		//
		// // adding each child node to HashMap key => value
		// map.put(KEY_TITLE, c);
		//
		// map.put(KEY_THUMB_URL, null);
		//
		// // adding HashList to ArrayList
		// colorlist.add(map);
		// }
		//
		// //list = (ListView) findViewById(R.id.list);
		//
		// // Getting adapter by passing xml data ArrayList
		// adapter = new LazyAdapterBehindMenu(getActivity(), colorlist);
		// setListAdapter(adapter);

		items.add(new EntryItem(R.drawable.search, "Search"));
		items.add(new EntryItem(R.drawable.terkini, "Berita Terbaru"));
		items.add(new EntryItem(R.drawable.popular, "Berita Populer"));
		items.add(new SectionItem("Category"));
		items.add(new EntryItem(R.drawable.category, "Analisis"));
		items.add(new EntryItem(R.drawable.category, "Investigasi"));
		items.add(new EntryItem(R.drawable.category, "Siyasah"));
		items.add(new EntryItem(R.drawable.category, "Artikel"));
		items.add(new EntryItem(R.drawable.category, "Kolom"));
		items.add(new EntryItem(R.drawable.about, "About"));
		EntryAdapter adapter = new EntryAdapter(getListView().getContext(),
				items);
		getListView().setAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			searchPopUp();
			break;
		case 1:
			// Intent i = new Intent(getActivity(), MainActivity.class);
			// startActivity(i);
			newContent = new AboveFragment("terkini");
			break;
		case 2:
			newContent = new AbovePopuler("populer");
			break;
		case 4:
			newContent = new AboveCategory("713", "Analisis");
			break;
		case 5:
			newContent = new AboveCategory("1936", "Investigasi");
			break;
		case 6:
			newContent = new AboveCategory("1903", "Siyasah");
			break;
		case 7:
			newContent = new AboveCategory("20", "Artikel");
			break;
		case 8:
			newContent = new AboveCategory("1842", "Kolom");
			break;
		case 9:
			Intent j = new Intent(getActivity(), AboutActivity.class);
			startActivity(j);
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

	public void searchPopUp() {

		LayoutInflater inflater = (LayoutInflater) getLayoutInflater(getArguments());
		final View element = inflater
				.inflate(R.layout.form_search, null, false);
		final EditText txtSearch = (EditText) element.findViewById(R.id.search);
		final View header = inflater.inflate(R.layout.dialog_customtitle, null);
		TextView tx = (TextView) header.findViewById(R.id.text_title);
		tx.setText("Search");
		new AlertDialog.Builder(getActivity()).setView(element)
				.setCustomTitle(header)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@TargetApi(11)
					public void onClick(DialogInterface dialog, int id) {
						if (txtSearch.getText().toString() != null) {

							Log.d("texxxxxxxxxxxxxxxt...", txtSearch.getText()
									.toString());
							Fragment newContent = new AboveSearch("Hasil Pencarian", txtSearch.getText().toString());
							if (newContent != null)
								switchFragment(newContent);
						}
						dialog.cancel();
					}

				}).show();
	}

}
