package com.example.sserver;

import java.util.ArrayList;

import com.example.sserver.list.RemotePlaylistAdapter;
import com.example.sserver.model.VlcPlaylistItem;
import com.example.sserver.receivers.PlaylistReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class RemotePlaylistActivity extends GenericActivity {

	ListView pl_lv;
	PlaylistReceiver plReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doBindService();
		setContentView(R.layout.remote_pl_layout);
		plReceiver = new PlaylistReceiver(this);
		pl_lv = (ListView) findViewById(R.id.remote_pl_list);
		pl_lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> listView, View parent, int position,
					long id) {
			VlcPlaylistItem plItem = (VlcPlaylistItem) pl_lv.getAdapter().getItem(position);
			mBoundService.sendPlPlayCommand(plItem.getId());
			}
		});
	}
	
	
	
	@Override
	protected void onServiceBound() {
		super.onServiceBound();
		mBoundService.sendPlaylistCommand();
	}



	@Override
	public void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if(intent.getAction().equals(GenericActivity.INTENT_PLAYLIST)) {
			ArrayList<VlcPlaylistItem> list = intent.getParcelableArrayListExtra("Playlist");
			onPlaylistReceived(list);
		}
	}



	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(plReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(GenericActivity.INTENT_PLAYLIST);
		this.registerReceiver(plReceiver, intentFilter);
	}

	public void onPlaylistReceived(ArrayList<VlcPlaylistItem> playlist) {
		Log.d(TAG, "Playlist received");
		pl_lv.setAdapter(new RemotePlaylistAdapter(this,0,playlist));
		//for(VlcPlaylistItem item : playlist) {
		//	Log.d(TAG, item.getName());
		//}
	}
	
	
}
