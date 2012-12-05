package com.example.sserver.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryItem implements Parcelable {
	String name;
	ArrayList<InfoItem> info = new ArrayList<InfoItem>();
	
	public CategoryItem() {
		name = "";
	}
	
	public CategoryItem(Parcel source) {
		name = source.readString();
		info = (ArrayList<InfoItem>)source.readArrayList(getClass().getClassLoader());
	}
	
	public static final Parcelable.Creator<CategoryItem> CREATOR = new Parcelable.Creator<CategoryItem>() {
		public CategoryItem createFromParcel(Parcel in) {
			return new CategoryItem(in);
		}

		public CategoryItem[] newArray(int size) {
			return new CategoryItem[size];
		}
	};
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<InfoItem> getInfo() {
		return info;
	}
	public void setInfo(ArrayList<InfoItem> info) {
		this.info = info;
	}
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeList(info);
	}
	
}
