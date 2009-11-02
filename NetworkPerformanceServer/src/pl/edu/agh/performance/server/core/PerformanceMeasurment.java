package pl.edu.agh.performance.server.core;


public class PerformanceMeasurment {

	/*
	 * Represents options for iperf exe command.
	 */
	private String options = "bin/iperf.exe -s -u -P 0 -i 1 -p 5001 -f k";

	private IperfThread iperf;

	public PerformanceMeasurment(String iperfCommand) {
		startListening();
	}

	private void startListening() {

		iperf = new IperfThread(true, options);
		iperf.start();

	}

}
