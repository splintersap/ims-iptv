package pl.edu.agh.performance.client.core;

public class PerformanceMeasurment {

	/*
	 * Represents options for iperf exe command.
	 */
	private String options;// =
							// "bin/iperf.exe -c 192.168.131.65 -u -P 1 -i 1 -p 5001 -f k -b 1M -t 10 -T 1";

	private IperfThread iperf;

	public PerformanceMeasurment(String serverAddress, int port, int bandwidth,
			int frameSize) {
		options = new String("bin/iperf.exe -c " + serverAddress
				+ " -u -i 1 -p " + port + " -b " + bandwidth
				+ "M -t 10 -T 255 -l " + frameSize + " -r");
		performQuery();
	}

	private void performQuery() {

		iperf = new IperfThread(true, options);
		iperf.start();

	}

}
