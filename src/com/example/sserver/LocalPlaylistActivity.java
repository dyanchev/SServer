package com.example.sserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.sserver.list.FileListArrayAdapter;
import com.example.sserver.list.FileSystem;
import com.example.sserver.model.FileSystemItem;

public class LocalPlaylistActivity extends GenericActivity {

	File root;
	ListView fileListView;
	FileListArrayAdapter flArrayAdapter;
	FileSystem fileSystem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist_layout);
		root = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		fileSystem = new FileSystem(root);
		// checkedItems = new LinkedHashSet<FileSystemItem>();
		fileListView = (ListView) findViewById(R.id.fl_list_view);
		flArrayAdapter = new FileListArrayAdapter(this, 0,
				fileSystem.getCurrentDir());
		fileListView.setAdapter(flArrayAdapter);
		fileListView.setClickable(true);
		fileListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		fileListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> listView, View parent,
					int position, long id) {
				FileSystemItem fsItem = flArrayAdapter.getItem(position);
				if (fsItem.getFile().getName().equals("..")) {
					flArrayAdapter.clear();
					flArrayAdapter.addAll(fileSystem.getParent());
					flArrayAdapter.notifyDataSetChanged();
					return;
				} else if (fsItem.getFile().getName().equals(".")) {

				} else if (fsItem.getFile().isDirectory()) {
					flArrayAdapter.clear();
					flArrayAdapter.addAll(fileSystem.chDir(fsItem.getFile()));
					flArrayAdapter.notifyDataSetChanged();
					return;
				}
				flArrayAdapter.toggle(position);
				if (fsItem.isChecked()) {
					fileSystem.addCheckItem(fsItem);
				} else {
					fileSystem.removeCheckItem(fsItem);
				}
				Log.d(TAG, "OnItemClick position:" + position);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onPause() {
		super.onPause();
		File root = this.getDir("playlist", MODE_WORLD_READABLE);
		File playlist = new File(root.getPath() + "/playlist.m3u");
		try {
			BufferedWriter fwr = new BufferedWriter(new FileWriter(playlist));
			LinkedHashSet<FileSystemItem> hs = new LinkedHashSet<FileSystemItem>();
			hs = fileSystem.getCheckedItems();
			for (FileSystemItem item : flArrayAdapter.getObjects()) {
				if (item.isChecked()) {
					hs.add(item);
				}
			}
			for (FileSystemItem item : hs) {
				String line = "http://" + getLocalIpAddress() + ":"
						+ SERVICE_PORT_NUMBER + item.getFile().getPath();
				Log.d(TAG, "m3u line:" + line);
				fwr.write(line);
				fwr.newLine();
			}
			fwr.newLine();
			fwr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "Exception caught:" + e);
		}
		// Playlist.getInstanse().setPlaylist(fileSystem.getCheckedItems());
		// for(FileSystemItem item : flArrayAdapter.getObjects()) {
		// if(item.isChecked()) {
		// Playlist.getInstanse().addPlaylistItem(item);
		// }
		// }
	}

}
