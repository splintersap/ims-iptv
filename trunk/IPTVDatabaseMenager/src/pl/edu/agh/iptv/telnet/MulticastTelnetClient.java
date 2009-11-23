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
		writeCommand("new " + uuid + " broadcast enabled");
		writeCommand("setup " + uuid + " input " + source);
		writeCommand("setup " + uuid + " output #rtp{mux=ts,dst=" + multicastIp
				+ "}");
	}

	
}
