package com.example.sserver.list;

import java.util.List;

import com.example.sserver.GenericActivity;
import com.example.sserver.R;
import com.example.sserver.list.FileListArrayAdapter.CheckBoxHolder;
import com.example.sserver.model.VlcPlaylistItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class RemotePlaylistAdapter extends ArrayAdapter<VlcPlaylistItem> {

	List<VlcPlaylistItem> objects; 
	Context context;
	
	public RemotePlaylistAdapter(Context context, int textViewResourceId,
			List<VlcPlaylistItem> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RowHolder holder = null;
		VlcPlaylistItem item = objects.get(position);
		if(row == null) {
			if(row == null) {
				LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
				row = layoutInflater.inflate(R.layout.remote_pl_row, parent,false);
				holder = new RowHolder();
				holder.name = (TextView) row.findViewById(R.id.remote_pl_row_name);
				row.setTag(holder);
			}
		}else {
			holder = (RowHolder)row.getTag();
		}
		if(item != null) {
			
			holder.name.setText(item.getName() != null ? item.getName() : "null");
			if(item.isSelected()) {
				holder.name.setTypeface(null, Typeface.BOLD);
				//Log.d(GenericActivity.TAG, "PlaylistAdapter : isSelected:"+item.isSelected());
			} else {
				holder.name.setTypeface(null, Typeface.NORMAL);
			}
		}
		return row;
	}

	private static class RowHolder {
		TextView name;
	}
}
