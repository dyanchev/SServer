package com.example.sserver.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;
import android.util.Log;

import com.example.sserver.GenericActivity;
import com.example.sserver.model.VlcStatusItem;

public class VLCClient {

	String serverIpAddress;
	DefaultHttpClient httpClient;
	String STATUS_PATH = "/requests/status.xml";
	String PLAYLIST_PATH = "/requests/playlist.xml";
	String TAG = "VLC Client";
	VLCAsyncTask vlcTask;
	TimerTask timerTask;
	Timer timer;
	boolean isTimerRunning;

	IVLCClientListener statusListener;

	public VLCClient(String ipAddress) {
		this.serverIpAddress = ipAddress;
		httpClient = new DefaultHttpClient();
		vlcTask = new VLCAsyncTask();
		// Log.d(GenericActivity.TAG, "New Timer Created");
		// timerTask = new TimerTask() {
		//
		// @Override
		// public void run() {
		// sendStatusCommand();
		// }

		// };
		// timer = new Timer();

	}

	public void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
		if (timerTask != null) {
			timerTask.cancel();
		}
		isTimerRunning = false;
	}

	public void sendStopCommand() {

		vlcTask = new VLCAsyncTask();
		vlcTask.execute("stop");
		// vlcTask.cancel(true);
		if (timer != null) {
			timer.cancel();
		}
		if (timerTask != null) {
			timerTask.cancel();
		}
		isTimerRunning = false;
	}

	public void sendPauseCommand() {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("pause");
	}

	public void sendPlayCommand(String url) {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("play", url);
		if (!isTimerRunning) {
			startPulling();
		}

	}

	public void startPulling() {
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				sendStatusCommand();
			}

		};
		timer.schedule(timerTask, 0, 1000);
		isTimerRunning = true;
	}

	public void sendStatusCommand() {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("status");
	}

	public void sentSeekCommand(double seek) {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("seek", String.valueOf(seek));
	}

	public void sendPlaylstCommand() {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("playlist");
	}

	public void sendPlPlayCommand(int id) {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("pl_play", "" + id);
	}

	public void sendNextCommand() {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("next");
	}

	public void sendPrevCommand() {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("prev");
	}

	public void sendVolumeCommand(int volume) {
		vlcTask = new VLCAsyncTask();
		vlcTask.execute("volume", "" + volume);
	}

	private class VLCAsyncTask extends AsyncTask<String, Void, VlcStatusItem> {

		@Override
		protected VlcStatusItem doInBackground(String... urls) {
			VlcStatusItem statusItem = new VlcStatusItem();
			String command = urls[0];
			HttpGet request = null;
			boolean isPlaylistRequest = false;
			// request = new
			// HttpGet("http://"+serverIpAddress+"/requests/status.xml");
			if (command.equals("pause")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH
						+ "?command=pl_pause");
				request.setHeader("User-Agent", "set your desired User-Agent");
			} else if (command.equals("stop")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH
						+ "?command=pl_stop");
				request.setHeader("User-Agent", "set your desired User-Agent");
			} else if (command.equals("play")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH
						+ "?command=in_play&input=" + urls[1]);
				request.setHeader("User-Agent", "set your desired User-Agent");
			} else if (command.equals("status")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH);
				request.setHeader("User-Agent", "set your desired User-Agent");
			} else if (command.equals("seek")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH
						+ "?command=seek&val=" + urls[1] + "%25");
				request.setHeader("User-Agent", "set your desired User-Agent");
			} else if (command.equals("playlist")) {
				request = new HttpGet("http://" + serverIpAddress
						+ PLAYLIST_PATH);
				isPlaylistRequest = true;
			} else if (command.equals("pl_play")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH
						+ "?command=pl_play&id=" + urls[1]);
			} else if (command.equals("prev")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH
						+ "?command=pl_previous");
			} else if (command.equals("next")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH
						+ "?command=pl_next");
			} else if (command.equals("volume")) {
				request = new HttpGet("http://" + serverIpAddress + STATUS_PATH
						+ "?command=volume&val=" + urls[1]);
			}
			// Log.d(TAG, request.getRequestLine().toString());
			try {
				if (request != null) {
					HttpResponse response = httpClient.execute(request);
					StatusLine status = response.getStatusLine();
					if (status.getStatusCode() != 200) {
						BasicResponseHandler handler = new BasicResponseHandler();
						String httpResponse = handler.handleResponse(response);
						Log.d(TAG, "Responce > " + httpResponse);
						response.getEntity().consumeContent();
						throw new IOException("Invalid response from server:"
								+ status.toString());
					}
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xmlReader = sp.getXMLReader();
					if (!isPlaylistRequest) {
						VLCStatusHandler statusHandler = new VLCStatusHandler();
						statusHandler.setStatusItem(statusItem);
						xmlReader.setContentHandler(statusHandler);
						xmlReader.parse(new InputSource(is));
					} else {
						VLCPlaylistHandler playlistHandler = new VLCPlaylistHandler();
						xmlReader.setContentHandler(playlistHandler);
						xmlReader.parse(new InputSource(is));
						statusListener.onVlcPlaylistReceved(playlistHandler
								.getPlaylist());
					}
				}

			} catch (Exception e) {
				Log.d(TAG, "Eception caught:" + e);
				// return statusItem;
			}
			return statusItem;
		}

		@Override
		protected void onPostExecute(VlcStatusItem result) {
			statusListener.onVlcStatusChanged(result);
		}

	}

	public void setVlcClientListener(IVLCClientListener client) {
		statusListener = client;
	}
}
