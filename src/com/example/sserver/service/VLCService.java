package com.example.sserver.service;

import java.io.IOException;
import java.util.ArrayList;

import com.example.sserver.GenericActivity;
import com.example.sserver.MP3Service;
import com.example.sserver.PlayerActivity;
import com.example.sserver.model.VlcPlaylistItem;
import com.example.sserver.model.VlcStatusItem;
import com.example.sserver.receivers.WifiReceiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class VLCService extends Service implements IVLCClientListener {

	public class LocalBinder extends Binder {
		public VLCService getService() {
			return VLCService.this; 
		}
	}
	
	private PhoneStateListener mPhoneStateListener = new PhoneStateListener(){

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch(state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if(vlcStatus != null) {
					if(vlcStatus.getState().equals("paused")) {
						sendPauseCommand();
					}
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				if(vlcStatus != null) {
					if(vlcStatus.getState().equals("playing")) {
						sendPauseCommand();
					}
				}
				break;
			}
		}
		
	};
	
	TelephonyManager tm;
	
	WifiReceiver wifiReceiver;
	VlcStatusItem vlcStatus;
	boolean isWifiConnected;
	
	private final IBinder mBinder = new LocalBinder();
	public String TAG = "VLC Service";
	
	public static String VLC_HOST = "10.0.0.70:8080";
	private VLCClient vlcClient;
	
	private MP3Service mp3Service;
	private int SERVER_PORT = 8080;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, "On BIND");
		return mBinder;
	}
	
	

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "Service created");
		SharedPreferences sp = this.getSharedPreferences(GenericActivity.SHARED_PREFERENCES, Context.MODE_WORLD_READABLE);
		String server = sp.getString("server", "10.0.2.2:8080");
		Log.d(TAG, "Server host:"+server);
		if(GenericActivity.EMULATION) {
			server = "10.0.2.2:8080";
		}
		vlcClient = new VLCClient(server);
		vlcClient.setVlcClientListener(this);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d(TAG, "Connection type :"+cm.getActiveNetworkInfo().getTypeName());
		if(cm.getActiveNetworkInfo().getTypeName().equalsIgnoreCase("WIFI")){
			Log.d(TAG, "Connection is wifi");
			isWifiConnected = true;
			try {
				mp3Service = new MP3Service(SERVER_PORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Exception caught:"+ e);
			}
		}
		if(GenericActivity.EMULATION) {
			isWifiConnected = true;
			try {
				mp3Service = new MP3Service(SERVER_PORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Exception caught:"+ e);
			}
		}
		wifiReceiver = new WifiReceiver(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		registerReceiver(wifiReceiver,intentFilter);
	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		vlcClient.cancelTimer();
		unregisterReceiver(wifiReceiver);
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "On Start Command");
		return Service.START_NOT_STICKY;
	}

	public void onVlcStatusChanged(VlcStatusItem status) {
		// TODO Auto-generated method stub
		vlcStatus = status;
		Intent intent = new Intent();
		intent.setAction(GenericActivity.INTENT_SEEK);
		intent.putExtra("Status", status);
		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		sendBroadcast(intent);
	}
	
	public void onVlcPlaylistReceved(ArrayList<VlcPlaylistItem> playlist) {
		Intent intent = new Intent();
		intent.setAction(GenericActivity.INTENT_PLAYLIST);
		intent.putExtra("Playlist", playlist);
		sendBroadcast(intent);
	}	
	
	public void sendPalyCommand(String url) {
		if(isWifiConnected) {
			vlcClient.sendPlayCommand(url);
		}
	}
	
	public void sendStopCommand() {
		Log.d(TAG, "Send Stop command");
		if(isWifiConnected) {
			vlcClient.sendStopCommand();
		}
	}
	
	public void sendPauseCommand() {
		if(isWifiConnected) {
			vlcClient.sendPauseCommand();
		}
	}
	
	public void sendSeekCommand(double seek) {
		if(isWifiConnected) {
			vlcClient.sentSeekCommand(seek);
		}
	}
	
	public void sendStatusCommand() {
		if(isWifiConnected) {
			vlcClient.sendStatusCommand();
		}
	}
	
	public void sendStartPulling() {
		if(isWifiConnected) {
			vlcClient.startPulling();
		}
	}
	
	public void sendStopPulling() {
		vlcClient.cancelTimer();
	}
	
	public void sendPlaylistCommand() {
		if(isWifiConnected) {
			vlcClient.sendPlaylstCommand();
		}
	}
	
	public void sendPlPlayCommand(int id) {
		if(isWifiConnected) {
			vlcClient.sendPlPlayCommand(id);
		}
	}
	
	public void sendNextCommand() {
		if(isWifiConnected) {
			vlcClient.sendNextCommand();
		}
	}
	
	public void sendPrevCommand() {
		if(isWifiConnected) {
			vlcClient.sendPrevCommand();
		}
	}
	
	public void onServerChanged() {
		vlcClient.cancelTimer();
		vlcClient.setVlcClientListener(null);
		SharedPreferences sp = this.getSharedPreferences(GenericActivity.SHARED_PREFERENCES, Context.MODE_WORLD_READABLE);
		String server  = sp.getString("server", "10.0.2.2:8080");
		Log.d(TAG, "Server host:"+server);
		vlcClient = new VLCClient(server);
		vlcClient.setVlcClientListener(this);
	}
	
	public void onWifiConnecred() {
		Log.d(TAG, "On Wifi Connected");
		isWifiConnected = true;
		try {
			mp3Service = new MP3Service(SERVER_PORT);
		} catch (IOException e) {
			Log.d(TAG, "Exception caught:"+ e);
		}
	}
	
	public void onWifiDisconnected() {
		Log.d(TAG, "On Wifi Disconnected");
		isWifiConnected = false;
		mp3Service.stop();
	}
}
