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

import com.ericsson.icp.IProfile;
import com.ericsson.icp.services.PGM.IPresence;
import com.ericsson.icp.services.PGM.IRLSGroup;
import com.ericsson.icp.services.PGM.IRLSManager;
import com.ericsson.icp.services.PGM.PGMFactory;
import com.ericsson.icp.util.IBuddy;
import com.ericsson.icp.util.IIterator;
import com.ericsson.icp.util.IUri;

public class BuddiesController implements ActionListener, ListSelectionListener {

	private Map<String, Buddy> buddies = null;

	private MainView mainView = null;
	private ContactsPanel contactsPanel = null;

	/**
	 * The ICP group list manager
	 */
	private IRLSManager groupListManagement;

	IPresence presence;

	public BuddiesController(IProfile profile, IPresence presence,
			MainView mainView) {
		this.presence = presence;
		this.mainView = mainView;
		this.buddies = new HashMap<String, Buddy>();

		this.contactsPanel = this.mainView.getChatTab().getContactsPanel();
		
		try {
			groupListManagement = PGMFactory.createRLSManager(profile);
			loadGroups();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			showErrorMsg("Problem loading buddies list");
			e.printStackTrace();
		}
		
//		this.contactsPanel.setContactsList(this.buddies.keySet().toArray());
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

		return this.buddies;
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

	/**
	 * Define the black list
	 * 
	 * @throws Exception
	 */
	private void loadBlackList() throws Exception {
		IIterator itr = presence.getBlockedWatcherList();
		while (itr.hasNext()) {
			itr.next();
			IUri icpBuddyUri = (IUri) itr.getElement();
			IBuddy buddy = groupListManagement
					.searchBuddy(icpBuddyUri.getUri());
			// The black listed buddy may not be in our contact list
			this.addBuddy(buddy.getDisplayName(), new Buddy(buddy
					.getDisplayName(), buddy.getUri()));

		}
	}

	private void showErrorMsg(String message) {

		final String msg = message;

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(mainView.getMainFrame(),
						"Error: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
			}

		});
	}

	/**
	 * Get the groups and the buddies
	 * 
	 * @throws Exception
	 */
	private void loadGroups() throws Exception {
		IIterator groupList = groupListManagement.getGroupIterator();
		while (groupList.hasNext()) {
			groupList.next();
			IRLSGroup icpGroup = (IRLSGroup) groupList.getElement();

			if (!StringUtil.isEmpty(icpGroup.getDisplayName())) {

				IIterator buddyList = icpGroup.getMembers();
				while (buddyList.hasNext()) {
					buddyList.next();
					IBuddy icpBuddy = (IBuddy) buddyList.getElement();
					Buddy buddy = new Buddy(icpBuddy.getDisplayName(), icpBuddy
							.getUri());
					this.addBuddy(icpBuddy.getDisplayName(), buddy);
				}
			}
		}
	}

}
