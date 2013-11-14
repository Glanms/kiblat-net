package id.artefact.kiblat;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class CommentActivity extends SherlockActivity {
	String id_post;
	private List<IsiComment> myComment = new ArrayList<IsiComment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_comment);
		
		populateCommentList();
		populateListView();
		
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#FFFFFF")));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(R.layout.actionbar_custom);
		Bundle extras = getIntent().getExtras();
		id_post = extras.getString("id");
		
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
	}

	private void populateCommentList() {
		myComment.add(new IsiComment("Abidarin Abas ke-1", "13-10-2013 22:10", R.drawable.abidarin, "Wah saya berani sumpah pocoooooong! bagus sekali artikel ini, DAFUQ"));
		myComment.add(new IsiComment("Abidarin Abas ke-2", "13-10-2013 22:20", R.drawable.abidarin, "Wah saya berani sumpah pocoooooong! bagus sekali artikel ini, DAFUQ"));
		myComment.add(new IsiComment("Abidarin Abas ke-3", "13-10-2013 22:30", R.drawable.abidarin, "Wah saya berani sumpah pocoooooong! bagus sekali artikel ini, DAFUQ"));
		myComment.add(new IsiComment("Abidarin Abas ke-4", "13-10-2013 22:40", R.drawable.abidarin, "Wah saya berani sumpah pocoooooong! bagus sekali artikel ini, DAFUQ"));
		myComment.add(new IsiComment("Abidarin Abas ke-5", "13-10-2013 22:50", R.drawable.abidarin, "Wah saya berani sumpah pocoooooong! bagus sekali artikel ini, DAFUQ"));
		
	}

	private void populateListView() {
		ArrayAdapter<IsiComment> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.listComment);
		list.setAdapter(adapter);

	}
	
	private class MyListAdapter extends ArrayAdapter<IsiComment>{
		public MyListAdapter(){
			super(CommentActivity.this, R.layout.item_comment, myComment);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.item_comment, parent, false);
			}
			
			IsiComment currentIsi = myComment.get(position);
			
			//ambilgambar
			ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
			imageView.setImageResource(currentIsi.getIconID());
			
			//ambilnama
			TextView namaText = (TextView) itemView.findViewById(R.id.item_textNama);
			namaText.setText(currentIsi.getNama());
			
			//ambiltanggal
			TextView tanggalText = (TextView) itemView.findViewById(R.id.item_textTanggal);
			tanggalText.setText(currentIsi.getTanggal());
			
			//ambilisi
			TextView isiText = (TextView) itemView.findViewById(R.id.item_textIsi);
			isiText.setText(currentIsi.getIsi());
			
			return itemView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu("");
		
		MenuItem SubMenu1Item = subMenu1.getItem();
		SubMenu1Item.setIcon(R.drawable.img_kosong);
		SubMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return super.onCreateOptionsMenu(menu);
	}


}
