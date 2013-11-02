package id.artefact.kiblat.help;

import id.artefact.kiblat.BaseActivity;

import id.artefact.kiblat.BehindFragment;

import id.artefact.kiblat.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapterBehindMenu extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapterBehindMenu(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.row_behind, null);

        TextView title = (TextView)vi.findViewById(R.id.rowbehind_title); // title

        ImageView thumb_image=(ImageView)vi.findViewById(R.id.rowbehind_icon); // thumb image
        
        HashMap<String, String> behindmenu = new HashMap<String, String>();
        behindmenu = data.get(position);
        
        // Setting all values in listview
        title.setText(behindmenu.get(BehindFragment.KEY_TITLE));

        imageLoader.DisplayImage(behindmenu.get(BehindFragment.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}