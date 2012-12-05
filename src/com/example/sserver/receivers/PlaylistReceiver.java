package com.example.sserver.receivers;

import com.example.sserver.GenericActivity;
import com.example.sserver.RemotePlaylistActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PlaylistReceiver extends BroadcastReceiver {

	private RemotePlaylistActivity context;
	
	public PlaylistReceiver(RemotePlaylistActivity context) {
		this.context = context;
	}
	
	@Override
	public void onReceive(Context contect, Intent intent) {
		Intent i = new Intent(this.context,RemotePlaylistActivity.class);
		i.setAction(GenericActivity.INTENT_PLAYLIST);
		i.putExtras(intent.getExtras());
		this.context.onNewIntent(i);
	}

}
