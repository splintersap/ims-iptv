package pl.edu.agh.iptv.telnet;

import java.io.IOException;

public class MulticastTelnetClient extends AbstractTelnetWorker {

	private String source;
	
	private String multicastIp;
	
	public MulticastTelnetClient(String source, String multicastIp)
	{
		super();
		this.source = source;
		this.multicastIp = multicastIp;
	}
	
	@Override
	void doTelnetWork() throws IOException {
		writeCommandAndRead("new " + uuid + " broadcast enabled");
		writeCommandAndRead("setup " + uuid + " input " + source);
		writeCommandAndRead("setup " + uuid + " output #rtp{mux=ts,dst=" + multicastIp
				+ "}");
		writeCommandAndRead("control "+ uuid + " play");
	}

	
}
