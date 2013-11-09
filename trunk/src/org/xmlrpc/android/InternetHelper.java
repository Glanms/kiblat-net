package org.xmlrpc.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

public class InternetHelper {

	public void downloadImage(String imgUrl, String fileName){
		FileHelper fh = new FileHelper();
		
		if(!fh.checkDir())
			fh.createDir();
		
		File f = new File(fh.getPath() + fileName);
		try {
			URL url = new URL(imgUrl);
			
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(100);
			
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(baf.toByteArray());
			fos.close();
			
			
			
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	
	
}
