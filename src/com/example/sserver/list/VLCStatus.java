package com.example.sserver.list;

import java.util.LinkedHashSet;
import java.util.Set;

import com.example.sserver.model.FileSystemItem;
import com.example.sserver.model.VlcStatusItem;

public class VLCStatus {

	private static VLCStatus instanse = new VLCStatus();
	
	//private Set<FileSystemItem> playlist;
	private VlcStatusItem vlcStatus;
	
	private VLCStatus() {
		//setPlaylist(new LinkedHashSet<FileSystemItem>());
		setVlcStatus(new VlcStatusItem());
	}
	
	public static final VLCStatus getInstanse() {
		return instanse;
	}

	public VlcStatusItem getVlcStatus() {
		return vlcStatus;
	}

	public void setVlcStatus(VlcStatusItem vlcStatus) {
		this.vlcStatus = vlcStatus;
	}
	
}
