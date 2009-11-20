package pl.edu.agh.iptv.telnet;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

/***
 * This is a simple example of use of TelnetClient. An external option handler
 * (SimpleTelnetOptionHandler) is used. Initial configuration requested by
 * TelnetClient will be: WILL ECHO, WILL SUPPRESS-GA, DO SUPPRESS-GA. VT100
 * terminal type will be subnegotiated.
 * <p>
 * Also, use of the sendAYT(), getLocalOptionState(), getRemoteOptionState() is
 * demonstrated. When connected, type AYT to send an AYT command to the server
 * and see the result. Type OPT to see a report of the state of the first 25
 * options.
 * <p>
 * 
 * @author Bruno D'Avanzo
 ***/
public class TelnetWorker extends Thread {

	public static String newline = System.getProperty("line.separator");

	private static final int VOD = 0;

	private static final int MULTICAST = 1;

	private static final int REMOVE = 2;

	private TelnetClient tc = null;

	OutputStreamWriter writer;

	InputStream instr;

	private int action;

	private String path;

	private String multicastIP = null;

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public static TelnetWorker getVodClient(String path) {
		TelnetWorker telnet = new TelnetWorker();
		telnet.path = path;
		telnet.action = VOD;
		return telnet;
	}

	public static TelnetWorker getMulticastClient(String path, String address) {
		TelnetWorker telnet = new TelnetWorker();
		telnet.path = path;
		telnet.multicastIP = address;
		telnet.action = MULTICAST;
		return telnet;
	}

	public static TelnetWorker removeInstance(String UUID) {
		TelnetWorker telnet = new TelnetWorker();
		telnet.uuid = UUID;
		telnet.action = REMOVE;
		return telnet;
	}

	private TelnetWorker() {

		tc = new TelnetClient();
		uuid = UUID.randomUUID().toString();
		TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler(
				"VT100", false, false, true, false);
		EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true,
				false);
		SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true,
				true, true);

		try {
			tc.addOptionHandler(ttopt);
			tc.addOptionHandler(echoopt);
			tc.addOptionHandler(gaopt);
		} catch (InvalidTelnetOptionException e) {
			System.err.println("Error registering option handlers: "
					+ e.getMessage());
		}

	}

	/***
	 * Reader thread. Reads lines from the TelnetClient and echoes them on the
	 * screen.
	 ***/
	public void run() {

		String remoteip = "127.0.0.1";
		int remoteport = 4212;

		try {
			tc.connect(remoteip, remoteport);
		} catch (Exception e) {
			System.err.println("Error connecting to telnet: " + e.getMessage());
		}

		instr = tc.getInputStream();
		OutputStream outstr = tc.getOutputStream();

		writer = new OutputStreamWriter(outstr);

		try {

			readFromStream();

			writeCommand("videolan");

			switch (action) {
				case VOD:
					createVod(path);
				break;
				case MULTICAST:
					createStream(multicastIP, path);
				break;
				case REMOVE:
					removeStream();
				break;
			}
			
			readFromStream();

		} catch (Exception e) {
			System.err.println("Exception while reading socket:"
					+ e.getMessage());
		}

		try {
			tc.disconnect();
			System.out.println("Disconecting telnet input stream ...");
		} catch (Exception e) {
			System.err.println("Exception while closing telnet:"
					+ e.getMessage());
		}
	}

	private void removeStream() throws IOException {	
		writeCommand("del " + uuid);
	}

	private String readFromStream() throws IOException {
		byte[] buff = new byte[1024];
		int ret_read = 0;
		do {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ret_read = instr.read(buff);
		} while (ret_read <= 0);
		String value = new String(buff, 0, ret_read);
		System.out.println("int = " + ret_read + ", " + value);
		return value;
	}

	private void writeCommand(String command) throws IOException {
		writer.write(command + newline);
		writer.flush();
		System.out.println(command);
	}

	private void createVod(String path) throws IOException {
		// example of creating VOD
		// new MyMusic vod enabled
		// setup MyMusic input
		// C:\Users\michal\Downloads\WyznaniaGejszy.1.LekPL.avi
		writeCommand("new " + uuid + " vod enabled");
		writeCommand("setup " + uuid + " input " + path);

	}

	public void createStream(String multicastIp, String source)
			throws IOException {
		// new channel1 broadcast enabled
		// setup channel1 input rtsp://rmv8.bbc.net.uk/radio4/tue1630.ra?
		// setup channel1 output
		// #rtp{mux=ts,dst=239.255.12.42,sap,name="Channel 1"}
		writeCommand("new " + uuid + " broadcast enabled");
		writeCommand("setup " + uuid + " input " + source);
		writeCommand("setup " + uuid + " output #rtp{mux=ts,dst=" + multicastIp
				+ "}");
		// writeCommand("control " + name + " play");
	}

}
