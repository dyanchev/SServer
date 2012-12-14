package com.example.sserver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.example.sserver.service.VLCService;

public class GenericActivity extends Activity {

	public static final String INTENT_SEEK = "com.exaple.seek";
	public static final String INTENT_PLAYLIST = "com.example.playlist";
	public static final String SHARED_PREFERENCES = "com.example.preferences";
	public static final int SERVICE_PORT_NUMBER = 8080;
	public static final boolean EMULATION = true;
	
	public static String TAG = "MP3 Server";
	
	protected VLCService mBoundService;
	protected boolean mIsBound;
	
	private ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	        // This is called when the connection with the service has been
	        // established, giving us the service object we can use to
	        // interact with the service.  Because we have bound to a explicit
	        // service that we know is running in our own process, we can
	        // cast its IBinder to a concrete class and directly access it.
	        mBoundService = ((VLCService.LocalBinder)service).getService();

	         //Tell the user about this for our demo.
	        Toast.makeText(GenericActivity.this, "Local Service Connected",
	                Toast.LENGTH_SHORT).show();
	        onServiceBound();
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        // This is called when the connection with the service has been
	        // unexpectedly disconnected -- that is, its process crashed.
	        // Because it is running in our same process, we should never
	        // see this happen.
	        mBoundService = null;
	       Toast.makeText(GenericActivity.this, "Local Service Disconnected",
	                Toast.LENGTH_SHORT).show();
	    }
	};

	void doBindService() {
	    // Establish a connection with the service.  We use an explicit
	    // class name because we want a specific service implementation that
	    // we know will be running in our own process (and thus won't be
	    // supporting component replacement by other applications).
		Log.d(TAG, "Do Bind Service call");
	    getApplicationContext().bindService(new Intent(GenericActivity.this, 
	            VLCService.class), mConnection, Context.BIND_AUTO_CREATE);
	    mIsBound = true;
	}

	void doUnbindService() {
	    if (mIsBound) {
	        // Detach our existing connection.
	        getApplicationContext().unbindService(mConnection);
	        mIsBound = false;
	    }
	}

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    doUnbindService();
	    Log.d(TAG, "On Destroy");
	}
	
	protected void onServiceBound() {
		
	}
	
	public String getLocalIpAddress() {
		WifiManager wm = (WifiManager)getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wm.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		String ipString = String.format(
				   "%d.%d.%d.%d",
				   (ip & 0xff),
				   (ip >> 8 & 0xff),
				   (ip >> 16 & 0xff),
				   (ip >> 24 & 0xff));
	if(GenericActivity.EMULATION) {
		return "localhost:8081";
	}
	return ipString;
	}
	
	public int getScreenOrientation()
	{
	    Display getOrient = getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
}
