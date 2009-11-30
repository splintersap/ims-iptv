package pl.edu.agh.iptv.view.chat;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.edu.agh.iptv.view.movies.MoviesTab;

public class ChatTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chat chat = null;
	JFrame mainFrame = null;
	ContactsPanel contactsPanel = null;

	public ChatTab(JFrame mainFrame, MoviesTab moviesTab) {
		this.setLayout(new GridLayout(1, 2));
		this.mainFrame = mainFrame;
		this.add(getChat().returnMainPanel());
		this.contactsPanel = new ContactsPanel(mainFrame, moviesTab);
		this.add(contactsPanel);
	}

	public Chat getChat() {
		if (chat == null) {
			chat = new Chat(mainFrame);
		}
		return this.chat;
	}
	
	public ContactsPanel getContactsPanel(){
		return this.contactsPanel;
	}

}
