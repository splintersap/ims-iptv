package pl.edu.agh.iptv.view.chat;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import pl.edu.agh.iptv.components.ResizableGridLayout;

public class ContactsPanel extends JPanel {

	private JList contactsList = null;

	private JToolBar toolBar = null;

	private JButton newContactB = null;
	private JButton removeContactB = null;

	public ContactsPanel() {
		this.setLayout(new ResizableGridLayout(2, 1));
		this.add(getJToolBar());

		JScrollPane contactsPane = new JScrollPane(getContactsList());
		contactsPane.setPreferredSize(new Dimension(150, 250));

		this.add(contactsPane);
	}

	private JToolBar getJToolBar() {
		if (toolBar == null) {

			toolBar = new JToolBar("Chat");
			
			toolBar.setSize(new Dimension(100, 25));

			newContactB = new JButton(
					new ImageIcon("images/chat/add_buddy.gif"));
			newContactB.setEnabled(true);

			removeContactB = new JButton(new ImageIcon(
					"images/chat/remove_buddy.gif"));
			removeContactB.setEnabled(true);

			toolBar.add(newContactB);
			toolBar.add(removeContactB);
		}
		return toolBar;
	}

	private JList getContactsList() {
		if (contactsList == null) {
			contactsList = new JList();

			String[] contacts = new String[] { "Ala", "Marcin", "Ewelina" };						

			contactsList.setListData(contacts);
		}

		return contactsList;
	}

}
