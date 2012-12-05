package com.example.sserver;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class MP3Service extends NanoHTTPD {

	public MP3Service(int port) throws IOException {
		super(port);
	}

	@Override
	public Response serveFile(String uri, Properties header, File homeDir,
			boolean allowDirectoryListing) {
		return super.serveFile(uri, header, homeDir, allowDirectoryListing);
	}
	
	

}
