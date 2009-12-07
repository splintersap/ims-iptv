package pl.edu.agh.iptv.telnet;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SharedMulticastTelnet extends AbstractTelnetWorker {

	private String source;
	private String multicastIp;
	private Date date;

	public SharedMulticastTelnet(String source, String multicastIp, Date date) {
		super();
		this.source = source;
		this.multicastIp = multicastIp;
		this.date = date;
	}

	@Override
	void doTelnetWork() throws IOException {
		// new d broadcast enabled
		// setup d input C:\Movies\2012.mov
		// setup d output #rtp{mux=ts,dst=239.255.12.42,sap,name="2012"}
		//
		// new my_sched schedule enabled
		// setup my_sched date 2009/12/4-11:59:00
		// setup my_sched append control d play
		// setup my_sched append del my_sched

		Format formatter = new SimpleDateFormat("yyyy/M/d-HH:mm:ss");
		String formatedDate = formatter.format(date);
		
		writeCommand("new " + uuid + " broadcast enabled");
		writeCommand("setup " + uuid + " input " + source);
		writeCommand("setup " + uuid + " output #rtp{mux=ts,dst=" + multicastIp	+ "}");
		
		String recorederUuid = UUID.randomUUID().toString();
		
		writeCommand("new " + recorederUuid + " schedule enabled");
		writeCommand("setup " + recorederUuid + " date " + formatedDate);
		writeCommand("setup " + recorederUuid + " append control " + uuid + " play");
		writeCommand("setup " + recorederUuid + " append control del " + recorederUuid);
	}

}
