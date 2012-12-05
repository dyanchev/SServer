package com.example.sserver.model;

import java.io.File;

public class FileSystemItem {

	private File file;
	private boolean isChecked = false;
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
}
