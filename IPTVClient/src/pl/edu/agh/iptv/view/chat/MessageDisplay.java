package pl.edu.agh.iptv.view.chat;

import com.ericsson.icp.util.ISessionDescription;

public abstract class MessageDisplay implements Runnable {

	private int percentage;

	private ISessionDescription sessionDescription;

	public MessageDisplay() {

	}

	public MessageDisplay(int percentage) {
		this.percentage = percentage;
	}

	public MessageDisplay(ISessionDescription sessionDescription) {
		this.sessionDescription = sessionDescription;
	}

	public int getPercentage() {
		return this.percentage;
	}

	public ISessionDescription getSessionDescription() {
		return this.sessionDescription;
	}

}
