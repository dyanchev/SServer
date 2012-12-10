package com.example.sserver;

import java.util.ArrayList;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.sserver.list.RemotePlaylistAdapter;
import com.example.sserver.list.VLCStatus;
import com.example.sserver.model.CategoryItem;
import com.example.sserver.model.InfoItem;
import com.example.sserver.model.VlcPlaylistItem;
import com.example.sserver.model.VlcStatusItem;
import com.example.sserver.receivers.PlaylistReceiver;

public class RemotePlaylistActivity extends GenericActivity {

	ListView pl_lv;
	RemotePlaylistAdapter pl_adapter;
	PlaylistReceiver plReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doBindService();
		setContentView(R.layout.remote_pl_layout);
		plReceiver = new PlaylistReceiver(this);
		pl_lv = (ListView) findViewById(R.id.remote_pl_list);
		pl_lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> listView, View parent,
					int position, long id) {
				VlcPlaylistItem plItem = (VlcPlaylistItem) pl_lv.getAdapter()
						.getItem(position);
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
		if (intent.getAction().equals(GenericActivity.INTENT_PLAYLIST)) {
			ArrayList<VlcPlaylistItem> list = intent
					.getParcelableArrayListExtra("Playlist");
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
		if (mIsBound && (mBoundService != null)) {
			mBoundService.sendPlaylistCommand();
		}
	}

	public void onPlaylistReceived(ArrayList<VlcPlaylistItem> playlist) {
		Log.d(TAG, "Playlist received");
		pl_adapter = new RemotePlaylistAdapter(this, 0, playlist);
		pl_lv.setAdapter(pl_adapter);
		for(VlcPlaylistItem item : playlist) {
			if(item.isSelected()) {
				int itemIndex = pl_adapter.getPosition(item);
				pl_lv.setSelection(itemIndex);
			}
		}
		// for(VlcPlaylistItem item : playlist) {
		// Log.d(TAG, item.getName());
		// }
//		VlcStatusItem statusItem = VLCStatus.getInstanse().getVlcStatus();
//		String name = null;
//		for (CategoryItem cat : statusItem.getCategory()) {
//			if (cat.getName().equals("meta")) {
//				for (InfoItem info : cat.getInfo()) {
//					if (info.getName().equals("title")) {
//						name = info.getValue();
//					}
//				}
//			}
//		}
//		int plIndex = -1;
//		if (name != null) {
//			//Log.d(TAG, "Name is:" + name);
//			for (VlcPlaylistItem item : playlist) {
//				//Log.d(TAG, "ITEM NAME:" + item.getName());
//				if (item.getName().equalsIgnoreCase(name)) {
//					plIndex = pl_adapter.getPosition(item);
//				}
//			}
			//Log.d(TAG, "current playing index:" + plIndex);
			//if (plIndex != -1) {
			//	pl_lv.setSelection(plIndex);
			//	pl_adapter.getItem(plIndex).setSelected(true);
			//	pl_adapter.notifyDataSetChanged();
				
			//}
		//}
	}
}
