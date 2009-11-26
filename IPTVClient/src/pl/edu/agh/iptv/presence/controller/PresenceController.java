package pl.edu.agh.iptv.presence.controller;

import pl.edu.agh.iptv.view.MainView;

import com.ericsson.icp.IProfile;

public class PresenceController {

	public PresenceController(IProfile profile, MainView mainView) {		
		new BuddiesController(profile, mainView);
	}

}
