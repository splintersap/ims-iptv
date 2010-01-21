package pl.edu.agh.iptv.performance.client;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.listeners.IperfManagerListener;
import pl.edu.agh.iptv.view.MainView;

/**
 * This thread is launching the iperf client for checking the bandwidth.
 * 
 * @author Wozniak
 * 
 */
public class PerformanceLauncher implements Runnable {

	private IPTVClient iptvClient;
	private MainView mainView;
	private String ipAddress;

	public PerformanceLauncher(IPTVClient iptv, MainView mainView, String ipAddress) {
		this.iptvClient = iptv;
		this.mainView = mainView;
		this.ipAddress = ipAddress;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		IPTVPerformanceClient performance = new IPTVPerformanceClient(
				ipAddress, mainView.getBandwidthLabel());
		performance.queryServer();

		if (performance.getIperfThread().isAlive()) {
			mainView.setWindowCloseOperation(new IperfManagerListener(iptvClient, mainView,
					performance.getIperfThread()));

		}

	}

}
