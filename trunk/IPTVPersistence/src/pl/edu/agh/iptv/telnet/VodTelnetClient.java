package pl.edu.agh.iptv.telnet;

import java.io.IOException;

import pl.edu.agh.iptv.persistence.Quality;

public class VodTelnetClient extends AbstractTelnetWorker {

	String source;
	Quality quality;

	public VodTelnetClient(String source, String uuid, Quality quality, String remoteIp) {
		super(remoteIp);
		this.uuid = uuid;
		this.source = source;
		this.quality = quality;
	}

	@Override
	void doTelnetWork() throws IOException {
		// example of creating VOD
		// new MyMusic vod enabled
		// setup MyMusic input
		// C:\Users\michal\Downloads\WyznaniaGejszy.1.LekPL.avi
		writeCommandAndRead("new " + uuid + " vod enabled");
		writeCommandAndRead("setup " + uuid + " input " + source);
		String scale = "1";
		if (quality.equals(Quality.MEDIUM)) {
			scale = "0.4";
		} else if (quality.equals(Quality.LOW)) {
			scale = "0.2";
		}

		if (!quality.equals(Quality.HIGH)) {
			writeCommandAndRead("setup " + uuid
					+ " output #transcode{vcodec=h264,scale=" + scale
					+ ",acodec=mp4a,channels=2}");
		}
	}

}
