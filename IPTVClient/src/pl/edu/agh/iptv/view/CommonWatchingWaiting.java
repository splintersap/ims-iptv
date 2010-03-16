package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pl.edu.agh.iptv.IPTVClient;

public class CommonWatchingWaiting extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int NO_RESPONSE = 0;
	private static final int AGREED = 1;
	private static final int DENIED = 2;

	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("CANCEL");

	private String movieTitle;
	private long date;

	private DefaultListModel listModel = new DefaultListModel();
	private JList list = new JList(listModel);
	private IPTVClient iptvClient;

	private Map<String, String> urisToUsers;
	private Map<String, Integer> urisToAgreed = new HashMap<String, Integer>();

	public CommonWatchingWaiting(IPTVClient iptvClient, JFrame mainFrame,
			Map<String, String> urisToUsers, String movieTitle, long date) {

		super(mainFrame);
		this.setTitle("Responses");

		this.iptvClient = iptvClient;

		this.urisToUsers = urisToUsers;

		this.movieTitle = movieTitle;

		this.date = date;

		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(200, 200));

		JScrollPane scrollPane = new JScrollPane(panel);

		JScrollPane listPane = new JScrollPane(list);

		JPanel buttonsP = new JPanel(new FlowLayout());
		okButton.setName("OK");
		okButton.addActionListener(this);
		cancelButton.setName("CANCEL");
		cancelButton.addActionListener(this);
		buttonsP.add(okButton);
		buttonsP.add(cancelButton);

		panel.add(listPane, BorderLayout.NORTH);
		panel.add(buttonsP, BorderLayout.SOUTH);

		for (String key : this.urisToUsers.keySet()) {
			listModel.addElement(this.urisToUsers.get(key)
					+ " - DID NOT RESPOND");
			this.urisToAgreed.put(key, NO_RESPONSE);
		}

		this.add(scrollPane);

		this.pack();
		this.setVisible(true);

	}

	public void setAgreement(String uri, boolean response) {

		for (int i = 0; i < listModel.size(); i++) {

			if (this.urisToAgreed.get(uri) == NO_RESPONSE) {
				String comp = (String) listModel.get(i);
				comp = comp.substring(0, comp.indexOf("DID NOT RESPOND"));
				comp = comp.substring(0, comp.length() - 3);

				if (this.urisToUsers.get(uri).compareTo(comp) == 0) {
					if (response) {
						listModel.set(i, new String(this.urisToUsers.get(uri)
								+ " - AGREED"));
						this.urisToAgreed.put(uri, AGREED);
					} else {
						listModel.set(i, new String(this.urisToUsers.get(uri)
								+ " - DENIED"));
						this.urisToAgreed.put(uri, DENIED);
					}
				}
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (((JButton) e.getSource()).getName().compareTo("OK") == 0) {
			String msg = new String();

			for (String key : this.urisToAgreed.keySet()) {
				if (this.urisToAgreed.get(key).compareTo(AGREED) == 0) {
					msg += key + "|";
				}
			}

			try {
				msg += IPTVClient.getProfile().getIdentity();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			msg += "\n" + date;

			iptvClient.setCommonWatching(msg, this.movieTitle);

			this.dispose();

		} else if (((JButton) e.getSource()).getName().compareTo("CANCEL") == 0) {
			this.dispose();
		}
	}

}
