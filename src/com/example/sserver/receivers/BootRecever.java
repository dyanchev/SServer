package com.example.sserver.receivers;

import com.example.sserver.service.VLCService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootRecever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context,VLCService.class);
		context.startService(i);
		Log.d("Boot Recever", "Service Intent Start");
	}

}
