package com.example.sserver;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class StreamServerActivity extends TabActivity  {

	public static String TAG = "MP3 Server";
	public static int SERVER_PORT = 8080;
	//Set<FileSystemItem> checkedItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TabHost tabHost = getTabHost();
		 
        // Tab for Photos
        TabSpec player = tabHost.newTabSpec("Player");
        // setting Title and Icon for the Tab
        player.setIndicator("Player", getResources().getDrawable(R.drawable.emblem_multimedia));
        Intent playerIntent = new Intent(this, PlayerActivity.class);
        player.setContent(playerIntent);
        
        TabSpec playlist = tabHost.newTabSpec("Local playlist");
        // setting Title and Icon for the Tab
        playlist.setIndicator("Local playlist", getResources().getDrawable(R.drawable.applications_multimedia));
        Intent playlistIntent = new Intent(this, LocalPlaylistActivity.class);
        playlist.setContent(playlistIntent);
        
        TabSpec remotePlaylist = tabHost.newTabSpec("Remote playlist");
        // setting Title and Icon for the Tab
        remotePlaylist.setIndicator("Remote playlist", getResources().getDrawable(R.drawable.multimedia2));
        Intent remotePlaylistIntent = new Intent(this, RemotePlaylistActivity.class);
        remotePlaylist.setContent(remotePlaylistIntent);
        
        
        tabHost.addTab(player); // Adding photos tab
        tabHost.addTab(playlist);
        tabHost.addTab(remotePlaylist);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_stream_server, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.menu_settings) {
			Intent intent = new Intent(this,ServerActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
