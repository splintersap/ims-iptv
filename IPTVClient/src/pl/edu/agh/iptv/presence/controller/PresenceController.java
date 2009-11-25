package pl.edu.agh.iptv.presence.controller;

import pl.edu.agh.iptv.presence.PresenceNotifier;
import pl.edu.agh.iptv.view.MainView;

import com.ericsson.icp.IProfile;

public class PresenceController {

	private MainView mainView = null;
	private IProfile profile;

	public PresenceController(IProfile profile, MainView mainView) {
		this.mainView = mainView;
		PresenceNotifier presenceNot = new PresenceNotifier(profile);
		new BuddiesController(profile, presenceNot.getPresence(), mainView);
	}

}
