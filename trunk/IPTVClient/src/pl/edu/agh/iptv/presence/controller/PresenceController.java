package pl.edu.agh.iptv.presence.controller;

import pl.edu.agh.iptv.view.MainView;

public class PresenceController {

	private MainView mainView = null;

	public PresenceController(MainView mainView) {
		this.mainView = mainView;
		new BuddiesController(mainView);
	}

}
