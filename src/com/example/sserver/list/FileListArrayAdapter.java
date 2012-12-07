package com.example.sserver.list;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.sserver.R;
import com.example.sserver.StreamServerActivity;
import com.example.sserver.model.FileSystemItem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FileListArrayAdapter extends ArrayAdapter<FileSystemItem> implements OnCheckedChangeListener {

	private Context context;
	private List<FileSystemItem> objects;
	

	public FileListArrayAdapter(Context context, int textViewResourceId,
			List<FileSystemItem> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FileSystemItem file = objects.get(position);
		View row = convertView;
		CheckBoxHolder holder = null;
		//if(row == null) {
			LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
			row = layoutInflater.inflate(R.layout.chech_box_row, parent,false);
			holder = new CheckBoxHolder();
			holder.setCb((CheckBox)row.findViewById(R.id.checkBox1));
			holder.getCb().setTag(position);
			holder.setFile(file);
			row.setTag(holder);
		//} else {
		//	holder = (CheckBoxHolder)row.getTag();
		//}
		//holder.setFile(file);
		holder.getCb().setText(file.getFile().getName());
		holder.getCb().setOnCheckedChangeListener(null);
		//Log.d(StreamServerActivity.TAG,"Adapter checked file/holder: = "+ file.isChecked()+ "/" + holder.getFile().isChecked() + " "+holder.getFile().getFile().getName());
		holder.getCb().setChecked(file.isChecked());
		holder.getCb().setOnCheckedChangeListener(this);
		//for(int i=0; i <= checked.size(); i++) {
		//	Log.d(StreamServerActivity.TAG, "Checked list Cheked pos:val=" + i+":"+checked.get(i));
		//}
		
		return row;
	}
	
	public boolean isChecked(int position) {
		return objects.get(position).isChecked();
	}
	
	public void setChecked(int position,boolean isChecked) {
		objects.get(position).setChecked(isChecked);
		notifyDataSetInvalidated();
	}
	
	public void toggle (int position) {
		setChecked(position, !isChecked(position));
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int position = (Integer) buttonView.getTag();
		objects.get(position).setChecked(isChecked);
	}

	public List<FileSystemItem> getObjects() {
		return objects;
	}

	public void setObjects(List<FileSystemItem> objects) {
		this.objects = objects;
	}
	static class CheckBoxHolder {
		private CheckBox cb;
		private FileSystemItem file;
		
		public CheckBox getCb() {
			return cb;
		}
		public void setCb(CheckBox cb) {
			this.cb = cb;
		}
		public FileSystemItem getFile() {
			return file;
		}
		public void setFile(FileSystemItem file) {
			this.file = file;
		}
	}

}
