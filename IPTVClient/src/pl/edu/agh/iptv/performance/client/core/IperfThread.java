/**
 * - 02/2008: Class created by Nicolas Richasse
 * 
 * Changelog:
 * 	- class created
 * 	- iperf line parsing fixed and improved
 * 
 * To do:
 * 	- ...
 *
 * Old notes:
 *	- The ParseLine results variable still ends up with a blank 0th string which may or may not ever matter (DC)
 */

package pl.edu.agh.iptv.performance.client.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JLabel;

public class IperfThread extends Thread {
	private String command;
	private Process process;
	private Vector<JperfStreamResult> finalResults;

	private BufferedReader input;
	private BufferedReader errors;

	private double finalBandwidth;

	private boolean isServerMode;

	private JLabel bandwidthLabel;

	public IperfThread(boolean isServerMode, String command,
			JLabel bandwidthLabel) {
		this.isServerMode = isServerMode;
		this.command = command;
		this.bandwidthLabel = bandwidthLabel;
		this.finalResults = new Vector<JperfStreamResult>();
	}

	public void run() {
		try {

			process = Runtime.getRuntime().exec(command);

			// read in the output from Iperf
			input = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			errors = new BufferedReader(new InputStreamReader(process
					.getErrorStream()));

			String input_line = null;
			while ((input_line = input.readLine()) != null) {
				parseLine(input_line);
			}

			String error_line = null;
			while ((error_line = errors.readLine()) != null) {
				System.out.println(error_line);
			}
			this.bandwidthLabel.setText("Your bandwidth: " + finalBandwidth
					+ "Mbits/sec");
		} catch (Exception e) {
			// don't do anything?
			System.out.println("\nIperf thread stopped [CAUSE="
					+ e.getMessage() + "]");
		} finally {
			quit();
		}

	}

	public synchronized void quit() {
		if (process != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {
			}

			if (!isServerMode) {
				if (input != null) {
					try {
						input.close();
					} catch (Exception e) {
						// nothing
					} finally {
						input = null;
					}
				}

				if (errors != null) {
					try {
						errors.close();
					} catch (Exception e) {
						// nothing
					} finally {
						errors = null;
					}
				}

				try {
					process.getInputStream().close();
				} catch (Exception e) {
					// nothing
				}

				try {
					process.getOutputStream().close();
				} catch (Exception e) {
					// nothing
				}

				try {
					process.getErrorStream().close();
				} catch (Exception e) {
					// nothing
				}
			}

			process.destroy();

			try {
				process.waitFor();
			} catch (Exception ie) {
				// nothing
			}

			process = null;
		}
	}

	public void parseLine(String line) {
		// only want the actual output lines
		if (line.matches("\\[[ \\d]+\\]\\s*[\\d]+.*")) {
			Pattern p = Pattern.compile("[-\\[\\]\\s]+");
			// ok now break up the line into id#, interval, amount transfered,
			// format
			// transferred, bandwidth, and format of bandwidth
			String[] results = p.split(line);

			// get the ID # for the stream
			Integer temp = new Integer(results[1].trim());
			int id = temp.intValue();

			boolean found = false;
			JperfStreamResult streamResult = new JperfStreamResult(id);
			for (int i = 0; i < finalResults.size(); ++i) {
				if ((finalResults.elementAt(i)).getID() == id) {
					streamResult = finalResults.elementAt(i);
					found = true;
					break;
				}
			}

			if (!found) {
				finalResults.add(streamResult);
			}
			// this is TCP or Client UDP
			if (results.length == 9) {
				Double start = new Double(results[2].trim());
				Double end = new Double(results[3].trim());
				Double bw = new Double(results[7].trim());

				Measurement M = new Measurement(start.doubleValue(), end
						.doubleValue(), bw.doubleValue(), results[8]);
				streamResult.addBW(M);
			} else if (results.length == 14) {
				Double start = new Double(results[2].trim());
				Double end = new Double(results[3].trim());
				Double bw = new Double(results[7].trim());

				Measurement B = new Measurement(start.doubleValue(), end
						.doubleValue(), bw.doubleValue(), results[7]);
				streamResult.addBW(B);

				Double jitter = new Double(results[9].trim());
				Measurement J = new Measurement(start.doubleValue(), end
						.doubleValue(), jitter.doubleValue(), results[10]);
				streamResult.addJitter(J);
				finalBandwidth = B.getValue();
			}
		}
	}
}
