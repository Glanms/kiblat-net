package id.artefact.kiblat.help;

import id.artefact.kiblat.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

	public EntryAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
			if (i.isSection()) {
				SectionItem si = (SectionItem) i;
				v = vi.inflate(R.layout.list_item_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView sectionView = (TextView) v
						.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());

			} else {
				EntryItem ei = (EntryItem) i;
				v = vi.inflate(R.layout.row_behind, null);

				final ImageView img = (ImageView) v
						.findViewById(R.id.rowbehind_icon);
				final TextView title = (TextView) v
						.findViewById(R.id.rowbehind_title);

				if (img != null)
					img.setImageResource(ei.draw);
				if (title != null)
					title.setText(ei.title);
			}
		}
		return v;
	}

}
