package id.artefact.kiblat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler  extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 9;
	private static final String DATABASE_NAME = "kiblat";
	
	private static final String TABLE_POST = "post";
	private static final String KEY_ID_POST = "id_post";
	private static final String KEY_TITLE = "title";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_TAX = "taxonomy"; //nama tag atau nama kategori
	private static final String KEY_POST_DATE = "post_date";
	private static final String KEY_GUID = "guid";
	private static final String KEY_TIPE_POST = "tipe"; //kategori atau tag atau kosong
	private static final String KEY_IMG = "img";
	private static final String KEY_COUNT_POST = "count";
	
	private static final String TABLE_POPTAG = "poptag";
	private static final String KEY_TAG = "tag";
	private static final String KEY_COUNT = "count";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public void addTes() {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TAG, "TES TES");
		values.put(KEY_COUNT, "309435");
		Log.i("db", "add");
		db.insert(TABLE_POPTAG, null, values);
		db.close(); //
	}
	
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i("db", "create db");
		String query_table_post = "CREATE TABLE " + TABLE_POST + "(" + 
				KEY_ID_POST + " TEXT, " + 
				KEY_TITLE + " TEXT, " + 
				KEY_CONTENT + " TEXT, " + 
				KEY_TAX + " TEXT, " + 
				KEY_POST_DATE + " TEXT, " + 
				KEY_GUID + " TEXT, " + 
				KEY_TIPE_POST + " TEXT, " + 
				KEY_IMG + " TEXT, " + 
				KEY_COUNT_POST + " TEXT " + 
		")";
		
		String query_table_taxonomy = "CREATE TABLE " + TABLE_POPTAG + "(" +  
				KEY_COUNT + " TEXT, " + 
				KEY_TAG + " TEXT " + 
				
		")";
		
		Log.i("db", "query table post: "+query_table_post);
		Log.i("db", "query table tax: "+query_table_taxonomy);
		db.execSQL(query_table_post);
		db.execSQL(query_table_taxonomy);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POPTAG);
		onCreate(db);
	}
	
	public void addPost(Post post) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID_POST, post.getId_post());
		values.put(KEY_POST_DATE, post.getDate_post());
		values.put(KEY_TITLE, post.getTitle());
		values.put(KEY_CONTENT, post.getContent());
		values.put(KEY_TAX, post.getTax());
		values.put(KEY_GUID, post.getGuid());
		values.put(KEY_TIPE_POST, post.getTipe());
		values.put(KEY_IMG, post.getImg());
		values.put(KEY_COUNT_POST, post.getCount());
		Log.i("post", "diinsert");
		// tessss
		
		db.insert(TABLE_POST, null, values);
		db.close(); //
		
	}
	
	public void deletePostbyTipe(String tipe) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_POST, KEY_TIPE_POST + " = ?",
	            new String[] { tipe });
	    db.close();
	}
	
	public void deletePostbyTag(String tag) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_POST, KEY_TAX + " = ?",
	            new String[] { tag });
	    db.close();
	}
	
	 public List<Post> getPostsByTipe(String tipe) {
	        List<Post> posts = new ArrayList<Post>();
	        // Select All Query
	        String selectQuery = "SELECT * FROM " + TABLE_POST + " WHERE "+KEY_TIPE_POST+"= '"+tipe+"'";
	 
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	                Post po = new Post();

	                po.setId_post(cursor.getString(0));
	                po.setTitle(cursor.getString(1));
	                po.setContent(cursor.getString(2));
	                po.setTax(cursor.getString(3));
	                po.setDate_post(cursor.getString(4));
	                po.setGuid(cursor.getString(5));
	               
	                po.setTipe("terkini");
	                // Adding contact to list
	                posts.add(po);
	            } while (cursor.moveToNext());
	        }
	 
	        // return contact list
	        return posts;
	    }
	
	
}
