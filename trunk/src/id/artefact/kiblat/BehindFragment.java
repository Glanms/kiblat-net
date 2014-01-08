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
import android.net.Uri;
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
		items.add(new EntryItem(R.drawable.search, "Search"));
		items.add(new EntryItem(R.drawable.terkini, "Berita Terbaru"));
		items.add(new EntryItem(R.drawable.popular, "Berita Populer"));
		items.add(new SectionItem("Category"));
		items.add(new EntryItem(R.drawable.category, "Analisis"));
		items.add(new EntryItem(R.drawable.category, "Investigasi"));
		items.add(new EntryItem(R.drawable.category, "Siyasah"));
		items.add(new EntryItem(R.drawable.category, "Artikel"));
		items.add(new EntryItem(R.drawable.category, "Kolom"));
		items.add(new EntryItem(R.drawable.category, "Rohah"));
		items.add(new EntryItem(R.drawable.category, "Profil"));
		items.add(new EntryItem(R.drawable.category, "Manhaj"));
		items.add(new EntryItem(R.drawable.category, "Munaqosyah"));
		items.add(new EntryItem(R.drawable.category, "Tarbiyah Jihadiyah"));
		items.add(new EntryItem(R.drawable.category, "Opini"));
		items.add(new EntryItem(R.drawable.category, "Info Event"));
		items.add(new EntryItem(R.drawable.category, "Suara Pembaca"));
		items.add(new EntryItem(R.drawable.about, "About"));
		items.add(new EntryItem(R.drawable.web, "Mobile Site"));
		items.add(new EntryItem(R.drawable.email, "Feedback"));
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
			newContent = new AboveCategory("1002", "Kolom");
			break;
		case 9:
			newContent = new AboveCategory("1901", "Rohah");
			break;
		case 10:
			newContent = new AboveCategory("1844", "Profil");
			break;
		case 11:
			newContent = new AboveCategory("1935", "Manhaj");
			break;
		case 12:
			newContent = new AboveCategory("1902", "Munaqosyah");
			break;
		case 13:
			newContent = new AboveCategory("1964", "Tarbiyah Jihadiyah");
			break;
		case 14:
			newContent = new AboveCategory("767", "Opini");
			break;
		case 15:
			newContent = new AboveCategory("771", "Info Event");
			break;
		case 16:
			newContent = new AboveCategory("770", "Suara Pembaca");
			break;
		case 17:
			Intent j = new Intent(getActivity(), AboutActivity.class);
			startActivity(j);
			break;
		case 18:
			GoTo("Web");
			break;
		case 19:
			GoTo("Email");
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

	private void GoTo(String Var) {
		if (Var.equals("Web")) {
			Intent l = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://m.kiblat.net"));
			startActivity(l);
		} else if (Var.equals("Email")) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			String aEmailList[] = { "kiblatmedia@gmail.com" };
			emailIntent
					.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Feedback");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					"Tuliskan Saran untuk Kiblat.Net");
			startActivityForResult(emailIntent, 0);
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
							Fragment newContent = new AboveSearch(
									"Hasil Pencarian", txtSearch.getText()
											.toString());
							if (newContent != null)
								switchFragment(newContent);
						}
						dialog.cancel();
					}

				}).show();
	}

}
