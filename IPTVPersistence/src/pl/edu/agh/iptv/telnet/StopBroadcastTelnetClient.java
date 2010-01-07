package pl.edu.agh.iptv.telnet;

import java.io.IOException;

public class StopBroadcastTelnetClient extends AbstractTelnetWorker {

	public StopBroadcastTelnetClient(String remoteIp, String uuid) {
		super(remoteIp);
		this.uuid = uuid;
	}

	@Override
	void doTelnetWork() throws IOException {
		writeCommandAndRead("control "+ uuid + " stop");
	}

}
