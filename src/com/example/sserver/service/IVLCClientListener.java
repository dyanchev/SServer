package com.example.sserver.service;

import java.util.ArrayList;

import com.example.sserver.model.VlcPlaylistItem;
import com.example.sserver.model.VlcStatusItem;

public interface IVLCClientListener {

	public void onVlcStatusChanged(VlcStatusItem status);
	public void onVlcPlaylistReceved(ArrayList<VlcPlaylistItem> playlist);
}
