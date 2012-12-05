package com.example.sserver.receivers;

import com.example.sserver.GenericActivity;
import com.example.sserver.PlayerActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SeekReceiver extends BroadcastReceiver {

	private PlayerActivity context;
	public SeekReceiver(PlayerActivity context) {
		this.context = context;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(GenericActivity.TAG, "Recever intent receved");
		Intent i = new Intent(context,PlayerActivity.class);
		i.setAction(GenericActivity.INTENT_SEEK);
		i.putExtras(intent.getExtras());
		this.context.onNewIntent(i);
	}

}
