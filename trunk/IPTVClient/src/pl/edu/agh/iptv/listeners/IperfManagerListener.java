package pl.edu.agh.iptv.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import pl.edu.agh.iptv.performance.client.core.IperfThread;

public class IperfManagerListener implements WindowListener{

	private IperfThread iperfThread;
	
	
	public IperfManagerListener(IperfThread iperfThread){
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
