package pl.edu.agh.iptv.view.chat;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ericsson.icp.IPlatform;
import com.ericsson.icp.IProfile;
import com.ericsson.icp.IService;
import com.ericsson.icp.ISession;

public class ChatTab extends JPanel {

	/**
	 * Reference to the icp platform.
	 */
	private IPlatform platform;

	/**
	 * Reference to the icp profile.
	 */
	private IProfile profile;

	/**
	 * Reference to the icp service.
	 */
	private IService service;

	/**
	 * Reference to the icp session.
	 */
	private ISession session;

	Chat chat = null;
	JFrame mainFrame = null;

	public ChatTab(JFrame mainFrame, IProfile profile, IService service,
			ISession session) {
		this.setLayout(new GridLayout(1, 1));
		this.service = service;
		this.profile = profile;
		this.session = session;
		this.mainFrame = mainFrame;
		this.add(getChat().returnMainPanel());
	}

	public Chat getChat() {
		if (chat == null) {
			chat = new Chat(mainFrame, profile, service, session);
		}
		return this.chat;
	}

}
