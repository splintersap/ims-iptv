package pl.edu.agh.iptv.telnet;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class RecordingTelnetClient extends AbstractTelnetWorker {

	private String source;
	private Date startDate;
	private Date endDate;
	
	public RecordingTelnetClient(String source, Date startDate, Date endDate) {
		this.source = source;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@Override
	void doTelnetWork() throws IOException {
		// new rec broadcast enabled
		// setup rec input mms://stream.onet.pl/media.wsx?/live/aljazeera
		// setup rec output file/avi:c:/Movies/aljazera2.avi
		//
		// new startR schedule enabled
		// setup startR date 2009/11/23-16:21:00
		// setup startR append control rec play
		//
		// new stopR schedule enabled
		// setup stopR date 2009/11/23-16:22:20
		// setup stopR append control rec stop
		// setup stopR append new e vod enabled
		// setup stopR append setup e input c:/Movies/aljazera2.avi
		// setup stopR append del rec
		// setup stopR append del startR
		// setup stopR append del stopR

		Format formatter = new SimpleDateFormat("yyyy/M/d-HH:mm:ss");
		
		String startFormatedDate = formatter.format(startDate);
		String endFormatedDate = formatter.format(endDate);
		
		String recorederUuid = UUID.randomUUID().toString();
		String starterUuid = UUID.randomUUID().toString();
		String stoperUuid = UUID.randomUUID().toString();
		
		writeCommand("new " + recorederUuid + " broadcast enabled");
		writeCommand("setup " + recorederUuid + " input " + source);
		writeCommand("setup " + recorederUuid + " output file/avi:c:/Movies/" + uuid.toString() + ".avi");
		//#transcode{vcodec=mp4v}:std{access=file,mux=mov,dst=c:\\output.mov}
		writeCommand("setup " + recorederUuid + " output #transcode{vcodec=mp4v}:std{access=file,mux=mov,dst=c:/Movies/" + uuid.toString() + ".mov");
		
		writeCommand("new " + starterUuid + " schedule enabled");
		writeCommand("setup " + starterUuid + " date " + startFormatedDate);
		writeCommand("setup " + starterUuid +  " append control " + recorederUuid + " play");
		
		writeCommand("new " + stoperUuid + " schedule enabled");
		writeCommand("setup " + stoperUuid + " date " + endFormatedDate);
		writeCommand("setup " + stoperUuid + " append control");
		writeCommand("setup " + stoperUuid + " append del " + recorederUuid);
		writeCommand("setup " + stoperUuid + " append del " + starterUuid);
		writeCommand("setup " + stoperUuid + " append new " + uuid.toString() + " vod enabled");
		writeCommand("setup " + stoperUuid + " append setup " + uuid.toString() + " input c:/Movies/" + uuid.toString() +".mov");
		writeCommand("setup " + stoperUuid + " append del " + stoperUuid);
		
	}

}
