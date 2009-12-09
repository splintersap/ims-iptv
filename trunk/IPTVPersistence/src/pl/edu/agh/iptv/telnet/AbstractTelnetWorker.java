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

import javax.management.timer.Timer;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

public abstract class AbstractTelnetWorker extends Thread {

	public static String newline = System.getProperty("line.separator");

	private TelnetClient tc = null;
	
	private static final String PASSWORD = "videolan";

	OutputStreamWriter writer;

	InputStream instr;


	protected String uuid;

	public String getUuid() {
		return uuid;
	}

	public AbstractTelnetWorker() {

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

	abstract void doTelnetWork() throws IOException;
	
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

			//readFromStream();
			System.out.println("Startuje z telnetem");

			//read "Password: "
			readFromStream(10);
			
			// writing password
			writeCommand(PASSWORD);
			
			// read command "Welcome, Master
			readFromStream(21);
			
			// firing abstract method 
			doTelnetWork();
			
			//readFromStream();

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

	private String readFromStream() throws IOException {
		byte[] buff = new byte[1024];
		int ret_read = 0;
		do {
			try {
				sleep(Timer.ONE_SECOND/10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ret_read = instr.read(buff);
		} while (ret_read <= 0);
		String value = new String(buff, 0, ret_read);
		System.out.println("int = " + ret_read + ", " + value);
		return value;
	}

	private String readFromStream(final int number) throws IOException {
		byte[] buff = new byte[1024];
		int read_data = number;
		do {
			int ret_read = instr.read(buff);
			read_data -= ret_read;
			System.out.println("Read = " + ret_read + " bytes, w sumie = " + read_data);
		} while (read_data > 0);
		String value = new String(buff, 0, number);
		System.out.println("Read : " + value + ", number " + number);
		//System.out.println("int = " + ret_read + ", " + value);
		return value;
	}
	
	protected void writeCommandAndRead(String command) throws IOException {
		writeCommand(command);
		readFromStream(2);
		System.out.println(command);
	}

	private void writeCommand(String command) throws IOException {
		writer.write(command + newline);
		writer.flush();
	}

}
