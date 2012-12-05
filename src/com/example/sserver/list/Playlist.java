package com.example.sserver.list;

import java.util.LinkedHashSet;
import java.util.Set;

import com.example.sserver.model.FileSystemItem;

public class Playlist {

	private static Playlist instanse = new Playlist();
	
	private Set<FileSystemItem> playlist;
	
	private Playlist() {
		setPlaylist(new LinkedHashSet<FileSystemItem>());
	}
	
	public static final Playlist getInstanse() {
		return instanse;
	}

	public Set<FileSystemItem> getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Set<FileSystemItem> playlist) {
		this.playlist = playlist;
	}
	
	public void addPlaylistItem(FileSystemItem item) {
		playlist.add(item);
	}
	
}
