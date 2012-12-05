package com.example.sserver.service;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.example.sserver.GenericActivity;
import com.example.sserver.model.CategoryItem;
import com.example.sserver.model.InfoItem;
import com.example.sserver.model.VlcStatusItem;

public class VLCStatusHandler extends DefaultHandler {

	
	String currentValue;
	Boolean currentElement;
	
	VlcStatusItem vlcStatus;
	InfoItem currentInfo;
	CategoryItem currentCategory;
	
	public void setStatusItem(VlcStatusItem vlcStatus) {
		this.vlcStatus = vlcStatus;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		currentElement = false;
		if(localName.equals("fullscreen")) {
			vlcStatus.setFullscreen(Integer.parseInt(currentValue));
		}else if(localName.equals("apiversion")) {
			vlcStatus.setApiVersion(Integer.parseInt(currentValue));
		}else if(localName.equals("time")) {
			vlcStatus.setTime(Long.parseLong(currentValue));
		}else if(localName.equals("length")) {
			vlcStatus.setLength(Integer.parseInt(currentValue));
		}else if(localName.equals("volume")) {
			vlcStatus.setVolume(Integer.parseInt(currentValue));
		}else if(localName.equals("random")) {
			vlcStatus.setRandom(Boolean.parseBoolean(currentValue));
		}else if(localName.equals("loop")) {
			vlcStatus.setLoop(Boolean.parseBoolean(currentValue));
		}else if(localName.equals("state")) {
			vlcStatus.setState(currentValue);
		}else if(localName.equals("version")) {
			vlcStatus.setVersion(currentValue);
		}else if(localName.equals("info")) {
			currentInfo.setValue(currentValue);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		currentElement = true;
		if(localName.equals("category")) {
			String name = attributes.getValue(0);
			//Log.d(GenericActivity.TAG, "Category attribute:"+name);
			currentCategory = new CategoryItem();
			currentCategory.setName(name);
			vlcStatus.getCategory().add(currentCategory);
		} else if(localName.equals("info")) {
			currentInfo = new InfoItem();
			String name = attributes.getValue(0);
			currentInfo.setName(name);
			//Log.d(GenericActivity.TAG, "Info Attribute:"+name);
			currentCategory.getInfo().add(currentInfo);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if(currentElement) {
			currentValue = new String(ch,start,length);
			currentElement = false;
		}
	}

	
}
