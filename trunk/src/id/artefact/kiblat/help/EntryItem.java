package id.artefact.kiblat.help;


public class EntryItem implements Item{
	
	public final int draw;
	public final String title;


	public EntryItem(int draw, String title) {
		this.draw = draw;
		this.title = title;
	}
	
	@Override
	public boolean isSection() {
		return false;
	}

}
