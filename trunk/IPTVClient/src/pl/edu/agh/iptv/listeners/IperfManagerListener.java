package pl.edu.agh.iptv.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.performance.client.core.IperfThread;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.components.MenuListItem;

public class IperfManagerListener implements WindowListener {

	private IperfThread iperfThread;
	private IPTVClient iptvClient;
	private MainView mainView;

	public IperfManagerListener(IPTVClient iptv, MainView mainView,
			IperfThread iperfThread) {
		this.iptvClient = iptv;
		this.mainView = mainView;
		this.iperfThread = iperfThread;
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		PlayListener playListener = mainView.getPlayListener();
		if (playListener.getCategory() == MenuListItem.BROADCAST)
			iptvClient.closeBroadcast(playListener.getPlayedMovie(),
					playListener.getPlayedMovieQuality());
		iperfThread.quit();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
