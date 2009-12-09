package pl.edu.agh.iptv.telnet;

import java.io.IOException;

public class VodTelnetClient extends AbstractTelnetWorker {

	String source;
	
	public VodTelnetClient(String source) {
		super();
		this.source = source;
	}
	
	@Override
	void doTelnetWork() throws IOException {
		// example of creating VOD
		// new MyMusic vod enabled
		// setup MyMusic input
		// C:\Users\michal\Downloads\WyznaniaGejszy.1.LekPL.avi
		writeCommandAndRead("new " + uuid + " vod enabled");
		writeCommandAndRead("setup " + uuid + " input " + source);
	}

}
