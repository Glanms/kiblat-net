package id.artefact.kiblat;

public class IsiComment {
	private String nama;
	private String tanggal;
	private int iconID;
	private String isi;
	
	public IsiComment(String nama, String tanggal, int iconID, String isi) {
		super();
		this.nama = nama;
		this.tanggal = tanggal;
		this.iconID = iconID;
		this.isi = isi;
	}

	public String getNama() {
		return nama;
	}

	public String getTanggal() {
		return tanggal;
	}

	public int getIconID() {
		return iconID;
	}

	public String getIsi() {
		return isi;
	}
	
	

}
