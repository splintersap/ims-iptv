package pl.edu.agh.iptv.view.chat;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.presence.controller.BuddiesController;
import pl.edu.agh.iptv.presence.data.Buddy;

public class AddUserFrame extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BuddiesController controller = null;

	JFrame parent = null;

	private JButton okButton = null;
	private JButton cancelButton = null;
	private JTextArea contactName = null;
	private JTextArea contactUri = null;

	public AddUserFrame(JFrame parent, BuddiesController controller) {
		super(parent);
		this.parent = parent;
		this.controller = controller;
		this.setPreferredSize(new Dimension(230, 200));
		this.setMaximumSize(new Dimension(300, 240));
		this.setMinimumSize(new Dimension(230, 200));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new ResizableGridLayout(5, 1));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new ResizableGridLayout(1, 5));
		JPanel okButtonPanel = new JPanel();
		JPanel cancelButtonPanel = new JPanel();
		okButtonPanel.setPreferredSize(new Dimension(80, 25));
		okButtonPanel.setMaximumSize(new Dimension(80, 25));
		cancelButtonPanel.setPreferredSize(new Dimension(80, 25));
		cancelButtonPanel.setMaximumSize(new Dimension(80, 25));

		okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(80, 25));
		okButton.setMaximumSize(new Dimension(80, 25));
		okButton.setName("OK");
		okButton.addActionListener(this);

		cancelButton = new JButton("CANCEL");
		cancelButton.setPreferredSize(new Dimension(80, 25));
		cancelButton.setMaximumSize(new Dimension(80, 25));
		cancelButton.setName("CANCEL");
		cancelButton.addActionListener(this);

		okButtonPanel.add(okButton);
		cancelButtonPanel.add(cancelButton);

		buttonsPanel.add(new JPanel());
		buttonsPanel.add(okButtonPanel);
		buttonsPanel.add(new JPanel());
		buttonsPanel.add(cancelButtonPanel);
		buttonsPanel.add(new JPanel());

		JScrollPane mainScPane = new JScrollPane(mainPanel);

		mainPanel.add(new JLabel("Conctact name"));
		mainPanel.add(contactName = new JTextArea());
		mainPanel.add(new JLabel("Contact URI"));
		mainPanel.add(contactUri = new JTextArea());
		mainPanel.add(buttonsPanel);

		this.add(mainScPane);

		this.pack();
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (((JButton) e.getSource()).getName().compareTo("OK") == 0) {

			String message = new String();

			if (checkName(contactName.getText()) == 1) {
				message = "You haven't typed the name.";
			} else if (checkName(contactName.getText()) == 2) {
				message = "The given name already exists.";
			} else if (checkURI(contactUri.getText()) == false) {
				message = "Sip URI is incorrect.";
			}

			if (message.length() != 0) {
				final String msgCopy = message;
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						JOptionPane.showMessageDialog(parent, msgCopy,
								"Incorrect input", JOptionPane.ERROR_MESSAGE);
					}

				});
			} else {
				this.controller.addNewBuddy(contactName.getText(), new Buddy(
						contactName.getText(), contactUri.getText()), false);
				this.dispose();
			}
		} else {
			this.dispose();
		}
	}

	public int checkName(String name) {
		if (name.length() == 0) {
			return 1;
		} else if (this.controller.getBuddiesMap().containsKey(
				contactName.getText())) {
			return 2;
		}
		return 0;
	}

	public boolean checkURI(String uri) {
		if (uri != null && uri.length() != 0) {
			return true;
		}
		return false;
	}
}
