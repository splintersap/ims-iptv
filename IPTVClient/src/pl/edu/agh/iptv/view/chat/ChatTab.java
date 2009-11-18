package pl.edu.agh.iptv.view.chat;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChatTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chat chat = null;
	JFrame mainFrame = null;

	public ChatTab(JFrame mainFrame) {
		this.setLayout(new GridLayout(1, 1));
		this.mainFrame = mainFrame;
		this.add(getChat().returnMainPanel());
	}

	public Chat getChat() {
		if (chat == null) {
			chat = new Chat(mainFrame);
		}
		return this.chat;
	}

}
