package pl.edu.agh.iptv.telnet;

import java.io.IOException;

public class CheckConnetcionTelnetClient extends AbstractTelnetWorker {

	public CheckConnetcionTelnetClient(String remoteIp) {
		super(remoteIp);
	}

	@Override
	void doTelnetWork() throws IOException {
		
	}

}
