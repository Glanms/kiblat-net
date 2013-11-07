package org.xmlrpc.android;

import java.io.File;

public class FileHelper {
	
	private String path;
	
	public FileHelper(){
		path = "mnt/sdcard/mobilelearning/";
	}
	
	public String getPath(){
		return path;
	}
	
	public void setPath(){
		this.path = path;
	}
	
	public void deleteData(){
		File file = new File(path);
		
		if(file.exists()){
			String deleteCmd = "rm -r " + path;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public void createDir(){
		new File(path).mkdirs();
	}
	
	public boolean checkDir(){
		return new File(path).isDirectory();
	}

}
