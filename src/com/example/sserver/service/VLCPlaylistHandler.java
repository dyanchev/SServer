package com.example.sserver.service;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.sserver.model.VlcPlaylistItem;

public class VLCPlaylistHandler extends DefaultHandler {

	
	String currentValue;
	Boolean currentElement;
	
	ArrayList<VlcPlaylistItem> playlist = new ArrayList<VlcPlaylistItem>();
	
	public ArrayList<VlcPlaylistItem> getPlaylist() {
		return playlist;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		currentElement = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		currentElement = true;
		if(localName.equals("leaf")) {
			VlcPlaylistItem item = new VlcPlaylistItem();
			item.setName(attributes.getValue(1));
			item.setId(Integer.parseInt(attributes.getValue(2)));
			item.setDuration(Integer.parseInt(attributes.getValue(3)));
			item.setUri(attributes.getValue(4));
			playlist.add(item);
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