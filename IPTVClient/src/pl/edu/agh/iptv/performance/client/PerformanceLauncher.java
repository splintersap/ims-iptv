package pl.edu.agh.iptv.performance.client;

import pl.edu.agh.iptv.listeners.IperfManagerListener;
import pl.edu.agh.iptv.view.MainView;

/**
 * This thread is launching the iperf client for checking the bandwidth.
 * 
 * @author Wozniak
 * 
 */
public class PerformanceLauncher implements Runnable {

	private MainView mainView;
	private String ipAddress;

	public PerformanceLauncher(MainView mainView, String ipAddress) {
		this.mainView = mainView;
		this.ipAddress = ipAddress;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			IPTVPerformanceClient performance = new IPTVPerformanceClient(
					ipAddress, mainView.getBandwidthLabel());
			performance.queryServer();

			mainView.setWindowCloseOperation(new IperfManagerListener(
					performance.getIperfThread()));
			try {
				performance.getIperfThread().join();
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
