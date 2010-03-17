package pl.edu.agh.iptv.telnet;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import pl.edu.agh.iptv.persistence.Quality;

public class RecordingTelnetClient extends AbstractTelnetWorker {

	private String source;
	private Date startDate;
	private Date endDate;
	private Quality quality;
	
	public RecordingTelnetClient(String source, Date startDate, Date endDate, String uuid, String remoteIp, Quality quality) {
		super(remoteIp);
		this.source = source;
		this.startDate = startDate;
		this.endDate = endDate;
		this.uuid = uuid;
		this.quality = quality;
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

		String scale = quality.getScale();
		
		Format formatter = new SimpleDateFormat("yyyy/M/d-HH:mm:ss");
		
		String startFormatedDate = formatter.format(startDate);
		String endFormatedDate = formatter.format(endDate);
		
		String recorederUuid = UUID.randomUUID().toString();
		String starterUuid = UUID.randomUUID().toString();
		String stoperUuid = UUID.randomUUID().toString();
		
		writeCommandAndRead("new " + recorederUuid + " broadcast enabled");
		writeCommandAndRead("setup " + recorederUuid + " input " + source);
		//#transcode{vcodec=mp4v}:std{access=file,mux=mov,dst=c:\\output.mov}
		writeCommandAndRead("setup " + recorederUuid + " output #transcode{vcodec=mp4v,acodec=mp3,scale=" + scale +"}:std{access=file,mux=mov,dst=c:/Movies/" + uuid.toString() + ".mov}");
		
		writeCommandAndRead("new " + starterUuid + " schedule enabled");
		writeCommandAndRead("setup " + starterUuid + " date " + startFormatedDate);
		writeCommandAndRead("setup " + starterUuid +  " append control " + recorederUuid + " play");
		
		writeCommandAndRead("new " + stoperUuid + " schedule enabled");
		writeCommandAndRead("setup " + stoperUuid + " date " + endFormatedDate);
		writeCommandAndRead("setup " + stoperUuid + " append control");
		writeCommandAndRead("setup " + stoperUuid + " append del " + recorederUuid);
		writeCommandAndRead("setup " + stoperUuid + " append del " + starterUuid);
		writeCommandAndRead("setup " + stoperUuid + " append new " + uuid.toString() + " vod enabled");
		writeCommandAndRead("setup " + stoperUuid + " append setup " + uuid.toString() + " input c:/Movies/" + uuid.toString() +".mov");
		writeCommandAndRead("setup " + stoperUuid + " append del " + stoperUuid);
		
	}

}
