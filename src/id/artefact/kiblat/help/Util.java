package id.artefact.kiblat.help;

import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Util {

	public static void goToGitHub(Context context) {
		Uri uriUrl = Uri.parse("http://github.com/jfeinstein10/slidingmenu");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		context.startActivity(launchBrowser);
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

}
