package pl.edu.agh.iptv.view.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.presence.data.Buddy;
import pl.edu.agh.iptv.view.CommonWatchingView;
import pl.edu.agh.iptv.view.components.ListItem;
import pl.edu.agh.iptv.view.components.MyCellRenderer;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class ContactsPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame mainFrame;
	private MoviesTab moviesTab;

	DefaultListModel listModel = new DefaultListModel();
	private JList contactsList = new JList(listModel);

	private ImageIcon available = new ImageIcon("images/chat/available.gif");
	private ImageIcon unavailable = new ImageIcon("images/chat/unavailable.gif");

	private JToolBar toolBar = null;

	private JButton newContactB = null;
	private JButton removeContactB = null;
	private JButton inviteM = null;

	public ContactsPanel(JFrame mainFrame, MoviesTab moviesTab) {
		this.mainFrame = mainFrame;
		this.moviesTab = moviesTab;
		contactsList.setCellRenderer(new MyCellRenderer());
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

			inviteM = new JButton(new ImageIcon("images/chat/remove_buddy.gif"));
			inviteM.setName("INVITE");
			inviteM.addActionListener(this);
			inviteM.setEnabled(true);

			toolBar.add(newContactB);
			toolBar.add(removeContactB);
			toolBar.add(inviteM);
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

	public void addElement(Buddy buddy, boolean isAvailable) {

		ListItem item = new ListItem(Color.GREEN, buddy.getDisplayName(), buddy
				.getUri(), isAvailable ? available : unavailable, isAvailable);
		listModel.addElement(item);

	}

	public JButton getNewContactB() {
		return newContactB;
	}

	public JButton getRemoveContactB() {
		return removeContactB;
	}

	public void changeStatus(String dispName, boolean isAvailable) {

		for (int i = 0; i < this.listModel.getSize(); i++) {
			ListItem item = (ListItem) this.listModel.getElementAt(i);
			if (item.getValue().compareTo(dispName) == 0) {
				if (isAvailable)
					item.setImage(available);
				else
					item.setImage(unavailable);
				item.setIsAvailable(isAvailable);
				break;
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (contactsList.getSelectedIndex() == -1) {
			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					JOptionPane.showMessageDialog(mainFrame,
							"Select at least one buddy", "Wrong input",
							JOptionPane.ERROR_MESSAGE);
				}

			});
			return;
		} else {
			String unavailable = new String();
			for (Object element : contactsList.getSelectedValues()) {
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
										mainFrame,
										"You selected upsent user: "
												+ unav
												+ "\n You need to select only available users.",
										"Wrong input",
										JOptionPane.ERROR_MESSAGE);
					}

				});
				return;
			} else {
				new CommonWatchingView(mainFrame, moviesTab);
			}
		}

	}
}
