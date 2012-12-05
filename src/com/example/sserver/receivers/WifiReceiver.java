package com.example.sserver.receivers;

import com.example.sserver.service.VLCService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {

	VLCService vlcService;
	
	public WifiReceiver(VLCService vlcService) {
		this.vlcService = vlcService;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if(action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
			if(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
				vlcService.onWifiConnecred();
			} else {
				vlcService.onWifiDisconnected();
			}
		}
	}
}
