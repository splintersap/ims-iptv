package pl.edu.agh.iptv.performance.client;

import pl.edu.agh.iptv.IPTVClient;

public class PerformanceRequester implements Runnable {

	private IPTVClient iptvClient;

	public PerformanceRequester(IPTVClient iptvClient) {
		this.iptvClient = iptvClient;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			iptvClient.askForIP();
		}
	}

}
