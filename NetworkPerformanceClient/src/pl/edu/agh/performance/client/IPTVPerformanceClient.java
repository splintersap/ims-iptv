package pl.edu.agh.performance.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import pl.edu.agh.performance.client.core.IperfThread;
import pl.edu.agh.performance.client.core.PerformanceMeasurment;

public class IPTVPerformanceClient {

	private final int bandwidth = 100;

	private String ipAddress;

	private JLabel bandwidthLabel;

	private PerformanceMeasurment measure;

	public IPTVPerformanceClient(String ipAddress, JLabel bandwidthLabel) {
		this.ipAddress = ipAddress;
		this.bandwidthLabel = bandwidthLabel;
	}

	public static void main(String[] args) {
		new IPTVPerformanceClient("192.168.0.67", new JLabel()).queryServer();
	}

	public void queryServer() {
		String iperfCommand = "iperf";
		String version = "";
		Process process;

		// get version of Iperf
		try {
			process = Runtime.getRuntime().exec(iperfCommand + " -v");
		} catch (Exception ioe) {
			Properties sysprops = System.getProperties();
			String osName = ((String) sysprops.get("os.name")).toLowerCase();

			if (new File("iperf/iperf.exe").exists()
					&& (osName.matches(".*win.*") || osName
							.matches(".*microsoft.*"))) {
				iperfCommand = "iperf/iperf.exe";
				try {
					process = Runtime.getRuntime().exec(iperfCommand + " -v");
				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"<html>"
											+ "Impossible to start the iperf executable located here : <br>"
											+ new File(iperfCommand)
													.getAbsolutePath()
											+ "</html>", "Error",
									JOptionPane.ERROR_MESSAGE);
					System.exit(1);
					return;
				}
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								"<html>Iperf is probably not in your path!<br>Please download it here '<b><font color='blue'><u>http://dast.nlanr.net/Projects/Iperf/</u></font></b>'<br>and put the executable into your <b>PATH</b> environment variable.</html>",
								"Iperf not found", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				return;
			}
		}

		// try to read the Iperf version on the standard output
		BufferedReader input = new BufferedReader(new InputStreamReader(process
				.getInputStream()));
		try {
			String line;
			line = input.readLine();

			while (line != null) {
				version = line;
				line = input.readLine();
			}
		} catch (IOException e) {
			// nothing
		}

		if (version == null || version.trim().equals("")) {
			// try to read the Iperf version on the error output
			input = new BufferedReader(new InputStreamReader(process
					.getErrorStream()));
			try {
				String line;
				line = input.readLine();

				while (line != null) {
					version = line;
					line = input.readLine();
				}
			} catch (IOException e) {
				// nothing
			}
		}

		if (version == null || version.trim().equals("")) {
			version = "iperf version 1.0.0";
			System.err.println("Impossible to get iperf version. Using '"
					+ version + "' as default.");
		}

		measure = new PerformanceMeasurment(this.ipAddress, 5001, bandwidth,
				1470, bandwidthLabel);

	}

	public IperfThread getIperfThread() {
		return this.measure.getIperfThread();
	}
}
