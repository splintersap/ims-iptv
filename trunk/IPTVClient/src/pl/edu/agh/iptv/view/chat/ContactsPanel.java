package pl.edu.agh.iptv.view.chat;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.components.ResizableGridLayout;

public class ContactsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DefaultListModel listModel = new DefaultListModel();
	private JList contactsList = new JList(listModel);

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
			newContactB.setName("New Contact");
			newContactB.setEnabled(true);

			removeContactB = new JButton(new ImageIcon(
					"images/chat/remove_buddy.gif"));
			removeContactB.setName("Remove Contact");
			removeContactB.setEnabled(true);

			toolBar.add(newContactB);
			toolBar.add(removeContactB);
		}
		return toolBar;
	}

	public JList getContactsList() {
		return contactsList;
	}

	public void setContactsList(Object[] contacts) {
		for (int i = 0; i < contacts.length; i++)
			listModel.addElement(contacts[i]);
	}

	public void removeElementAt(int index) {
		this.listModel.remove(index);
	}

	public void addElement(String buddy) {
		this.listModel.addElement(buddy);
	}

	public JButton getNewContactB() {
		return newContactB;
	}

	public JButton getRemoveContactB() {
		return removeContactB;
	}

}
