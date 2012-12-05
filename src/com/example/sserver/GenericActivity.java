package com.example.sserver;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.example.sserver.service.VLCService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

public class GenericActivity extends Activity {

	public static final String INTENT_SEEK = "com.exaple.seek";
	public static final String INTENT_PLAYLIST = "com.example.playlist";
	public static final int SERVICE_PORT_NUMBER = 8081;
	public static final boolean EMULATION = true;
	
	public static String TAG = "MP3 Server";
	
	protected VLCService mBoundService;
	private boolean mIsBound;
	
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
		/*try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String ip = Formatter.formatIpAddress(inetAddress
								.hashCode());
						Log.i(TAG, "IP=" + ip);
						return ip;
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, ex.toString());
		}*/
		String result = "localhost";
		return result;
	}
	
}
