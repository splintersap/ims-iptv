package pl.edu.agh.iptv.servlet.performance.core;

public class PerformanceMeasurment {

	/*
	 * Represents options for iperf exe command.
	 */
	private String options;// = "bin/iperf.exe -s -u -P 0 -i 1 -p 5001 -f k";

	private IperfThread iperf;

	public PerformanceMeasurment(int port, int frameSize) {
		options = new String("C:\\Iperf\\iperf.exe -s -u -P 0 -i 1 -p " + port
				+ " -l " + frameSize);
		startListening();
	}

	private void startListening() {

		iperf = new IperfThread(true, options);
		iperf.start();

	}

	public IperfThread getIperfThread() {
		return iperf;
	}

}
