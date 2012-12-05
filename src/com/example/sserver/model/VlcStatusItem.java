package com.example.sserver.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class VlcStatusItem implements Parcelable {
	int fullscreen;
	int apiVersion;
	long time;
	int length;
	int volume;
	boolean random;
	String state;
	boolean loop;
	String version;
	float position;
	ArrayList<CategoryItem> category;

	public VlcStatusItem() {
		state = "";
		version = "";
		category = new ArrayList<CategoryItem>();
	}

	public VlcStatusItem(Parcel source) {
		fullscreen = source.readInt();
		apiVersion = source.readInt();
		time = source.readLong();
		length = source.readInt();
		volume = source.readInt();
		random = Boolean.parseBoolean(source.readString());
		state = source.readString();
		loop = Boolean.parseBoolean(source.readString());
		version = source.readString();
		position = source.readFloat();
		category = (ArrayList<CategoryItem>)source.readArrayList(getClass().getClassLoader());
	}

	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<VlcStatusItem> CREATOR = new Parcelable.Creator<VlcStatusItem>() {
		public VlcStatusItem createFromParcel(Parcel in) {
			return new VlcStatusItem(in);
		}

		public VlcStatusItem[] newArray(int size) {
			return new VlcStatusItem[size];
		}
	};

	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(fullscreen);
		out.writeInt(apiVersion);
		out.writeLong(time);
		out.writeInt(length);
		out.writeInt(volume);
		out.writeString(Boolean.toString(random));
		out.writeString(state);
		out.writeString(Boolean.toString(loop));
		out.writeString(version);
		out.writeFloat(position);
		out.writeList(category);
	}

	public int getFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(int fullscreen) {
		this.fullscreen = fullscreen;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(int apiVersion) {
		this.apiVersion = apiVersion;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	public ArrayList<CategoryItem> getCategory() {
		return category;
	}

	public void setCategory(ArrayList<CategoryItem> category) {
		this.category = category;
	}

}
