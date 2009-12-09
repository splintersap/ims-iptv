package pl.edu.agh.iptv.telnet;

import java.io.IOException;

public class RemovingTelnetClient extends AbstractTelnetWorker {

	private String removingUuid;
	
	public RemovingTelnetClient(String removingUuid) {
		super();
		this.removingUuid = removingUuid;
	}
	
	@Override
	void doTelnetWork() throws IOException {
		writeCommandAndRead("del " + removingUuid);

	}

}
