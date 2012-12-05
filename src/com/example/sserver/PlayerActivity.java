package com.example.sserver;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Logger;

import com.example.sserver.list.FileListArrayAdapter;
import com.example.sserver.list.FileSystem;
import com.example.sserver.list.Playlist;
import com.example.sserver.model.CategoryItem;
import com.example.sserver.model.FileSystemItem;
import com.example.sserver.model.InfoItem;
import com.example.sserver.model.VlcStatusItem;
import com.example.sserver.receivers.SeekReceiver;
import com.example.sserver.service.VLCClient;
import com.example.sserver.service.VLCService;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayerActivity extends GenericActivity {

	public static int SERVER_PORT = 8080;

	Button playButton;
	Button pauseButton;
	Button stopButton;
	Button nextButton;
	Button prevButton;
	
	TextView timePast;
	TextView totalTime;
	TextView title;

	SeekBar progressBar;
	VlcStatusItem vlcStatus;
	boolean usesProgressBar = false;
	boolean isPulling;
	double seekTo;
	SeekReceiver seekReceiver;

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(GenericActivity.INTENT_SEEK);
		this.registerReceiver(seekReceiver, iFilter);
		Log.d(TAG, "Register Receiver");
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(seekReceiver);
		Log.d(TAG, "Unregister Receiver");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Intent intent = new Intent(this,VLCService.class);
		// startService(intent);

		doBindService();
		// mBoundService.start
		// /mBoundService.ons
		setContentView(R.layout.activity_stream_server);
		seekReceiver = new SeekReceiver(this);
		timePast = (TextView) findViewById(R.id.pl_time_esp);
		totalTime = (TextView) findViewById(R.id.pl_time_total);
		title = (TextView) findViewById(R.id.pl_title);
		progressBar = (SeekBar) findViewById(R.id.pl_progress_bar);
		progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				onSeekBarChanged(progress, fromUser);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				mBoundService.sendSeekCommand(seekTo);
			}

		});
		playButton = (Button) findViewById(R.id.bnt_play);
		playButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onPlay();
			}

		});
		pauseButton = (Button) findViewById(R.id.bnt_pause);
		pauseButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onPauseButton();
			}
		});
		stopButton = (Button) findViewById(R.id.bnt_stop);
		stopButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onStopButton();
			}
		});

		prevButton = (Button) findViewById(R.id.bnt_prev);
		prevButton.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				onPrevButton();
			}
		});
		
		nextButton = (Button) findViewById(R.id.bnt_next);
		nextButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				onNextButton();
			}
		}); 

	}

	@Override
	protected void onServiceBound() {
		mBoundService.sendStatusCommand();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getAction().equals(GenericActivity.INTENT_SEEK)) {
			VlcStatusItem vlcStatus = intent.getParcelableExtra("Status");
			onVlcStatusChanged(vlcStatus);
		}
	}

	public final void onPlay() {
		isPulling = true;
		
		File root = this.getDir("playlist", MODE_WORLD_READABLE);
		File playlist = new File(root.getPath() + "/playlist.m3u");
		
		if(playlist.exists()) {
			Log.d(TAG, "Playlist exist:" + playlist.getPath());
			mBoundService.sendPalyCommand("http://"+getLocalIpAddress() + ":"+ SERVICE_PORT_NUMBER + playlist.getPath());
		}
		
	}

	public final void onPauseButton() {
		// super.onPause();
		mBoundService.sendPauseCommand();
	}

	public final void onStopButton() {
		// super.onStop();
		mBoundService.sendStopCommand();
		isPulling = false;
	}
	
	public final void onNextButton() {
		mBoundService.sendNextCommand();
	}

	public final void onPrevButton() {
		mBoundService.sendPrevCommand();
	}

	public void onVlcStatusChanged(VlcStatusItem status) {
		vlcStatus = status;
		if (vlcStatus != null) {
			if (vlcStatus.getState().equals("playing") && !isPulling) {
				isPulling = true;
				mBoundService.sendStartPulling();
				Log.d(TAG, "Send start pulling");
			} else if(vlcStatus.getState().equals("stopped")) {
				mBoundService.sendStopPulling();
				isPulling = false;
			}

			if (progressBar.getMax() != status.getLength()) {
				progressBar.setMax(status.getLength());
			}

			if (!usesProgressBar) {
				progressBar.setProgress((int) status.getTime());
			}
			//CALC TOTAL TIME
			int totalMM,totalSS,length;
			length = status.getLength();
			totalMM = length / 60;
			totalSS = length % 60;
			StringBuilder tTime = new StringBuilder();
			tTime.append(totalMM < 10 ? "0"+totalMM : totalMM  );
			tTime.append(":");
			tTime.append(totalSS < 10 ? "0" + totalSS : totalSS);
			totalTime.setText(tTime.toString());
			//CAL PAST TIME
			int pastMM,pastSS,time;
			time = (int)status.getTime();
			pastMM = time / 60;
			pastSS = time % 60;
			StringBuilder pTime = new StringBuilder();
			pTime.append(pastMM < 10 ? "0"+pastMM : pastMM);
			pTime.append(":");
			pTime.append(pastSS < 10 ? "0"+pastSS : pastSS);
			timePast.setText(pTime);
			for(CategoryItem category: vlcStatus.getCategory()) {
				if(category.getName().equals("meta")) {
					for(InfoItem info : category.getInfo()) {
						if(info.getName().equals("title")) {
							title.setText(info.getValue());
						}
					}
				}
			}
		}
	}

	public void onSeekBarChanged(int progress, boolean fromUser) {
		if (fromUser) {
			usesProgressBar = true;
			seekTo = (float) ((float) progress / (float) vlcStatus.getLength()) * 100;
			// Log.d(TAG,"SEEK BAR PERCENT = " + percent);
			// percent = percent * vlcStatus.getLength();
			// Log.d(TAG,"SEEK BAR PERCENT = " + percent);

			//
			usesProgressBar = false;
		}
	}

	public Context This() {
		return this;
	}

}