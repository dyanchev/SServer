package com.example.sserver.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VlcPlaylistItem implements Parcelable {

	String name;
	int id;
	int duration;
	String uri;
	boolean selected;
	
	public VlcPlaylistItem() {
		
	}
	
	public VlcPlaylistItem(Parcel source) {
		name = source.readString();
		id = source.readInt();
		duration = source.readInt();
		uri = source.readString();
		selected = Boolean.parseBoolean(source.readString());
	}
	
	public static final Parcelable.Creator<VlcPlaylistItem> CREATOR = new Parcelable.Creator<VlcPlaylistItem>() {
		public VlcPlaylistItem createFromParcel(Parcel in) {
			return new VlcPlaylistItem(in);
		}

		public VlcPlaylistItem[] newArray(int size) {
			return new VlcPlaylistItem[size];
		}
	};
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(id);
		dest.writeInt(duration);
		dest.writeString(uri);
		dest.writeString(String.valueOf(selected));
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
