package pl.edu.agh.iptv.servlet.performance;

import pl.edu.agh.iptv.servlet.VBSUtils;
import pl.edu.agh.iptv.servlet.VideoServlet;

/**
 * This thread is launching the iperf client for checking the bandwidth.
 * 
 * @author Wozniak
 * 
 */
public class PerformanceLauncher implements Runnable {

	private VideoServlet servlet;

	public PerformanceLauncher(VideoServlet servlet) {
		this.servlet = servlet;		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		boolean result = VBSUtils.isRunning("iperf.exe");
		/*
		 * If iperf process is already running new won't be started.
		 */
		if (result)
			return;
		IPTVPerformanceServer performance = new IPTVPerformanceServer();
		performance.startServer();

		servlet.log("########################");
		servlet.log("IPERF SERVER WAS STARTED");
		servlet.log("########################");
		
//		while (true) {
//
//			boolean result = VBSUtils.isRunning("iperf.exe");
//			/*
//			 * If iperf process is already running new won't be started.
//			 */
//			if (result)
//				return;
//			IPTVPerformanceServer performance = new IPTVPerformanceServer();
//			performance.startServer();
//
//			servlet.log("########################");
//			servlet.log("IPERF SERVER WAS STARTED");
//			servlet.log("########################");
//
//			try {
//				Thread.sleep(50000);
//				performance.getIperfThread().quit();
//				servlet.log("%%%%%%%%%%%%%%%%%%%%%%%");
//				servlet.log("IPERF SERVER WAS STOPED");
//				servlet.log("%%%%%%%%%%%%%%%%%%%%%%%");
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				servlet.log("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//				servlet.log("EXCEPTION");
//				servlet.log("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");				
//				e.printStackTrace();
//			} finally {
//
//			}
//
//		}

	}

}
