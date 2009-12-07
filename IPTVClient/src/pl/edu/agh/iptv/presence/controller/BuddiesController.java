package pl.edu.agh.iptv.presence.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.listeners.CommonWatchingListener;
import pl.edu.agh.iptv.presence.PresenceNotifier;
import pl.edu.agh.iptv.presence.data.Buddy;
import pl.edu.agh.iptv.view.CommonWatchingView;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.MyJFrame;
import pl.edu.agh.iptv.view.chat.AddUserFrame;
import pl.edu.agh.iptv.view.chat.ContactsPanel;
import pl.edu.agh.iptv.view.components.ListItem;

import com.ericsson.icp.IProfile;
import com.ericsson.icp.services.PGM.IPresence;
import com.ericsson.icp.services.PGM.IRLSGroup;
import com.ericsson.icp.services.PGM.IRLSManager;
import com.ericsson.icp.services.PGM.PGMFactory;
import com.ericsson.icp.util.IBuddy;
import com.ericsson.icp.util.IIterator;

public class BuddiesController implements ActionListener, ListSelectionListener {

	private IPTVClient iptvClient = null;

	private Map<String, Buddy> buddies = null;

	private Map<String, String> buddyUriToName = null;

	private PresenceNotifier presenceNot;

	private MainView mainView = null;
	private ContactsPanel contactsPanel = null;

	private IRLSGroup mainGroup = null;

	/**
	 * The ICP group list manager
	 */
	private IRLSManager groupListManagement;

	IPresence presence;

	public BuddiesController(IPTVClient iptvClient, IProfile profile,
			MainView mainView) {
		this.iptvClient = iptvClient;
		presenceNot = new PresenceNotifier(profile, this);
		((MyJFrame) mainView.getMainFrame()).setPresenceNotifier(presenceNot);
		this.presence = presenceNot.getPresence();
		this.mainView = mainView;
		this.buddies = new HashMap<String, Buddy>();
		this.buddyUriToName = new HashMap<String, String>();

		this.contactsPanel = this.mainView.getChatTab().getContactsPanel();

		try {
			groupListManagement = PGMFactory.createRLSManager(profile);
			loadGroups();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			showErrorMsg("Problem loading buddies list");
			e.printStackTrace();
		}

		this.contactsPanel.getNewContactB().addActionListener(this);
		this.contactsPanel.getRemoveContactB().addActionListener(this);
		this.contactsPanel.getContactsList().addListSelectionListener(this);
		this.contactsPanel.getInviteMB().addActionListener(this);

		this.mainView.getChatTab().getChat().setBuddyUriToName(
				this.buddyUriToName);

	}

	public void addBuddy(String buddyName, Buddy buddy, boolean isAvailable) {
		this.buddies.put(buddyName, buddy);
		this.buddyUriToName.put(buddy.getUri(), buddyName);
		this.contactsPanel.addElement(buddy, isAvailable);
		// Subscription to the presence notification.
		this.presenceNot.subscribe(buddy.getUri());

	}

	public void addNewBuddy(String buddyName, Buddy buddy, boolean isAvailable) {
		this.buddies.put(buddyName, buddy);
		this.buddyUriToName.put(buddy.getUri(), buddyName);
		this.contactsPanel.addElement(buddy, isAvailable);
		// Subscription to the presence notification.
		this.presenceNot.subscribe(buddy.getUri());

		/*
		 * This is copied part.
		 */
		IRLSGroup icpGroup = mainGroup;
		try {
			IBuddy newBuddy = PGMFactory.createBuddy(buddy.getUri());
			newBuddy.setDisplayName(buddy.getDisplayName());
			icpGroup.addMember(newBuddy);
			groupListManagement.modifyGroup(icpGroup);
		} catch (Exception e) {
			showErrorMsg("Problem with adding new buddy");
		}
	}

	public void refreshContactsList() {

	}

	public void removeBuddy(Buddy buddy) {
		this.buddies.remove(buddy.getDisplayName());
		this.buddyUriToName.remove(buddy.getUri());

		if (mainGroup != null) {
			IBuddy buddyToRemove = findBuddy(mainGroup, buddy.getUri());
			if (buddyToRemove != null) {
				try {
					mainGroup.removeMember(buddyToRemove);
					groupListManagement.modifyGroup(mainGroup);
				} catch (Exception e) {
					showErrorMsg("Problem removing buddy "
							+ buddy.getDisplayName());
				}
			}
		}

	}

	public void removeBuddies(Object[] buddiesToRemove, int[] selectedInd) {
		for (int i = 0; i < buddiesToRemove.length; i++) {
			Buddy buddy = this.buddies.get(((ListItem) buddiesToRemove[i])
					.getValue());
			removeBuddy(buddy);
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
								displayString += ((ListItem) selectedC[i])
										.getValue()
										+ "\n";
							}

							if (JOptionPane.showConfirmDialog(mainView
									.getMainFrame(), displayString) == JOptionPane.YES_OPTION) {
								removeBuddies(selectedC, selectedInd);
							}
						}

					});
				}
			} else if (((JButton) e.getSource()).getName().compareTo("INVITE") == 0) {
				if (this.contactsPanel.getContactsList().getSelectedIndex() == -1) {
					EventQueue.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							JOptionPane.showMessageDialog(
									BuddiesController.this.mainView
											.getMainFrame(),
									"Select at least one buddy", "Wrong input",
									JOptionPane.ERROR_MESSAGE);
						}

					});
					return;
				} else {
					String unavailable = new String();
					for (Object element : contactsPanel.getContactsList()
							.getSelectedValues()) {
						if (!((ListItem) element).isAvailable()) {
							unavailable += (((ListItem) element).getValue());
						}
					}
					final String unav = unavailable;
					if (unavailable.length() > 0) {
						EventQueue.invokeLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								JOptionPane
										.showMessageDialog(
												BuddiesController.this.mainView
														.getMainFrame(),
												"You selected upsent user: "
														+ unav
														+ "\n You need to select only available users.",
												"Wrong input",
												JOptionPane.ERROR_MESSAGE);
							}

						});
						return;
					} else {

						Map<String, String> urisToUsers = new HashMap<String, String>();
						List<String> uris = new ArrayList<String>();

						Object[] elements = contactsPanel.getContactsList()
								.getSelectedValues();

						String selected = new String();

						for (int i = 0; i < elements.length; i++) {

							uris.add(this.buddies.get(
									(((ListItem) elements[i]).getValue()))
									.getUri());

							urisToUsers.put(this.buddies.get(
									(((ListItem) elements[i]).getValue()))
									.getUri(), (((ListItem) elements[i])
									.getValue()));

							if (i < elements.length - 1) {
								selected += (((ListItem) elements[i])
										.getValue())
										+ ", ";
							} else {
								selected += (((ListItem) elements[i])
										.getValue());
							}
						}

						CommonWatchingView commonWatching = new CommonWatchingView(
								this.mainView.getMainFrame(), this.mainView
										.getMoviesTab(), urisToUsers, selected);

						CommonWatchingListener commonListener = new CommonWatchingListener(
								iptvClient, this.mainView.getMainFrame(),
								this.mainView.getChatTab().getChat(),
								commonWatching);

						commonWatching.getOkButton().addActionListener(
								commonListener);
						commonWatching.getCancelButton().addActionListener(
								commonListener);
					}
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
							((ListItem) ((JList) selection.getSource())
									.getSelectedValue()).getValue()).getUri());
		else
			this.mainView.getChatTab().getChat().setURI("");
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
				mainGroup = icpGroup;
				while (buddyList.hasNext()) {
					buddyList.next();
					IBuddy icpBuddy = (IBuddy) buddyList.getElement();
					Buddy buddy = new Buddy(icpBuddy.getDisplayName(), icpBuddy
							.getUri());

					// Subscription to the presence notification.
					// this.presenceNot.subscribe(icpBuddy.getUri());

					this.addBuddy(icpBuddy.getDisplayName(), buddy, false);
				}
			}
		}
	}

	public void changeStatus(String uri, boolean isAvailable) {
		this.contactsPanel.changeStatus(this.buddyUriToName.get(uri),
				isAvailable);
		this.mainView.getMainFrame().repaint();
	}

	private IBuddy findBuddy(IRLSGroup group, String contactUri) {
		IBuddy icpBuddy = null;
		try {
			IIterator itr = group.getMembers();
			while (itr.hasNext() && (icpBuddy == null)) {
				itr.next();
				icpBuddy = (IBuddy) itr.getElement();
				if (!icpBuddy.getUri().equals(contactUri)) {
					icpBuddy = null;
				}
			}
		} catch (Exception e) {
			showErrorMsg("Error searching for buddy");
		}
		return icpBuddy;
	}
}
