package com.example.sserver.list;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import android.graphics.Path;
import android.util.Log;

import com.example.sserver.GenericActivity;
import com.example.sserver.model.FileSystemItem;

public class FileSystem {

	private File currentDir;
	
	private LinkedHashSet<FileSystemItem> checkedItems;

	public FileSystem(File currentDir) {
		this.currentDir = currentDir;
		checkedItems = new LinkedHashSet<FileSystemItem>();
	}
	
	public List<FileSystemItem> getCurrentDir() {
		File[] list = currentDir.listFiles();
		ArrayList<File> files = null;
		if(list != null) {
			files = new ArrayList<File>(Arrays.asList(list));
		} else {
			files = new ArrayList<File>();
		}
		
		ArrayList<FileSystemItem> result = new ArrayList<FileSystemItem>();
		File parent = new File(currentDir.getPath() + "/..");
		File current = new File(currentDir.getPath() + "/.");
		files.add(0, parent);
		files.add(0, current);
		for(File file : files) {
			FileSystemItem item = new FileSystemItem();
			item.setFile(file);
			result.add(item);
		}
		return result;
	}
	
	public List<FileSystemItem> getParent() {
		currentDir = currentDir.getParentFile();
		Log.d(GenericActivity.TAG, "Path variabale:"+currentDir.getPath());
		return getCurrentDir();
	}
	
	public List<FileSystemItem> chDir(File dir) {
		currentDir = dir;
		return getCurrentDir();
	}
	
	public void  addCheckItem(FileSystemItem item) {
		checkedItems.add(item);
	}
	
	public void removeCheckItem(FileSystemItem item) {
		checkedItems.remove(item);
	}
	
	public void syncCheckList(List<FileSystemItem> source) {
		checkedItems.addAll(source);
	}
	
	public LinkedHashSet<FileSystemItem> getCheckedItems() {
		return checkedItems;
	}
	
}
