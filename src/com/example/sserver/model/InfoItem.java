package com.example.sserver.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InfoItem implements Parcelable {

	private String name;
	private String value;
	
	public InfoItem() {
		name = "";
		value = "";
	}

	public InfoItem(Parcel source) {
		name = source.readString();
		value = source.readString();
	}
	
	public static final Parcelable.Creator<InfoItem> CREATOR = new Parcelable.Creator<InfoItem>() {
		public InfoItem createFromParcel(Parcel in) {
			return new InfoItem(in);
		}

		public InfoItem[] newArray(int size) {
			return new InfoItem[size];
		}
	};
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(value);
	}
	
}
