package pl.edu.agh.iptv.presence.controller;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.MainView;

import com.ericsson.icp.IProfile;

public class PresenceController {

	public PresenceController(IPTVClient iptvClient, IProfile profile, MainView mainView) {		
		new BuddiesController(iptvClient, profile, mainView);
	}

}
