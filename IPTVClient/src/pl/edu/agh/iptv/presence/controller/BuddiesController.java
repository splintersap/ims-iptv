package pl.edu.agh.iptv.presence.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.presence.data.Buddy;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.chat.AddUserFrame;
import pl.edu.agh.iptv.view.chat.ContactsPanel;

public class BuddiesController implements ActionListener, ListSelectionListener {

	private Map<String, Buddy> buddies = null;

	private MainView mainView = null;
	private ContactsPanel contactsPanel = null;

	public BuddiesController(MainView mainView) {
		this.mainView = mainView;
		this.buddies = getBuddies();

		this.contactsPanel = this.mainView.getChatTab().getContactsPanel();
		this.contactsPanel.setContactsList(this.buddies.keySet().toArray());
		this.contactsPanel.getNewContactB().addActionListener(this);
		this.contactsPanel.getRemoveContactB().addActionListener(this);
		this.contactsPanel.getContactsList().addListSelectionListener(this);
	}

	public void addBuddy(String buddyName, Buddy buddy) {
		this.buddies.put(buddyName, buddy);
		this.contactsPanel.addElement(buddyName);
	}

	public void removeBuddy(String buddyName) {
		this.buddies.remove(buddyName);
	}

	private Map<String, Buddy> getBuddies() {
		Map<String, Buddy> result = new HashMap<String, Buddy>();

		result.put("Alicja", new Buddy("Alicja", "sip:alice@ericsson.com"));
		result.put("Bob", new Buddy("Bob", "sip:bob@ericsson.com"));
		result.put("Coco", new Buddy("Coco", "sip:coco@ericsson.com"));

		return result;
	}

	public void removeBuddies(Object[] buddiesToRemove, int[] selectedInd) {
		for (int i = 0; i < buddiesToRemove.length; i++) {
			this.buddies.remove(buddiesToRemove[i]);
			this.contactsPanel.removeElementAt(selectedInd[i]);
		}
		this.contactsPanel.getContactsList().repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {
			if (((JButton) e.getSource()).getName().compareTo("New Contact") == 0) {

				new AddUserFrame(this.mainView.getMainFrame(), this);

			} else if (((JButton) e.getSource()).getName().compareTo(
					"Remove Contact") == 0) {
				final JList contacts = contactsPanel.getContactsList();

				int index = contacts.getSelectedIndex();
				if (index != -1) {
					final Object[] selectedC = contacts.getSelectedValues();
					final int[] selectedInd = contacts.getSelectedIndices();
					EventQueue.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String displayString = new String(
									"Are you sure you want to remove: ");
							for (int i = 0; i < contacts.getSelectedValues().length; i++) {
								displayString += selectedC[i] + "\n";
							}

							if (JOptionPane.showConfirmDialog(mainView
									.getMainFrame(), displayString) == JOptionPane.YES_OPTION) {
								removeBuddies(selectedC, selectedInd);
							}
						}

					});
				}
			}
		}
	}

	public Map<String, Buddy> getBuddiesMap() {
		return this.buddies;
	}

	@Override
	public void valueChanged(ListSelectionEvent selection) {
		// TODO Auto-generated method stub
		if (selection.getValueIsAdjusting() == true) {
			return;
		}
		if (((JList) selection.getSource()).getSelectedValue() != null)
			this.mainView.getChatTab().getChat().setURI(
					this.buddies.get(
							((JList) selection.getSource()).getSelectedValue()
									.toString()).getUri());
		else
			this.mainView.getChatTab().getChat().setURI("");
	}

}
