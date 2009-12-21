package pl.edu.agh.iptv.view;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import pl.edu.agh.iptv.presence.PresenceNotifier;

public class MyJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PresenceNotifier presenceNotifier;

	public MyJFrame() {
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void setPresenceNotifier(PresenceNotifier notifier) {
		this.presenceNotifier = notifier;
	}

	/**
	 * Release object needed to be release on th icpController before exiting
	 * the application.
	 * 
	 * @see javax.swing.JFrame#processWindowEvent(java.awt.event.WindowEvent)
	 */
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			close();
		}
		super.processWindowEvent(e);
	}

	/**
	 * Close the clientinstance
	 */
	public void close() {
		if (presenceNotifier != null)
			try {
				presenceNotifier.publishPresence(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
