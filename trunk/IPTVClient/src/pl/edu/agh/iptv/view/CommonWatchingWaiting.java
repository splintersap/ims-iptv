package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

public class CommonWatchingWaiting extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("CANCEL");
		
	private DefaultListModel listModel = new DefaultListModel();
	private JList list = new JList(listModel);

	private Map<String, String> urisToUsers;

	public CommonWatchingWaiting(JFrame mainFrame,
			Map<String, String> urisToUsers) {

		super(mainFrame);

		this.urisToUsers = urisToUsers;

		JPanel panel = new JPanel(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(panel);

		JPanel buttonsP = new JPanel(new FlowLayout());
		okButton.setEnabled(false);
		okButton.setName("OK");
		cancelButton.setName("CANCEL");
		buttonsP.add(okButton);
		buttonsP.add(cancelButton);

		panel.add(buttonsP, BorderLayout.SOUTH);
		
		for(String key : this.urisToUsers.keySet()){
			listModel.addElement(this.urisToUsers.get(key) + " - DIDN'T CONFIRM");
		}

		this.add(scrollPane);
		
		this.pack();
		this.setVisible(true);

	}

	public void setAgreement(String uri, boolean response){
	
		for(int i = 0; i < listModel.size(); i++){
			if(uri.compareTo((String)listModel.get(i)) == 0){
				if(response)
					listModel.set(i, new String(this.urisToUsers.get(uri) + " - AGREED"));
				else
					listModel.set(i, new String(this.urisToUsers.get(uri) + " - DENYED"));
			}
		}
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(((JButton)e.getSource()).getName().compareTo("OK") == 0){
			
		}else if(((JButton)e.getSource()).getName().compareTo("OK") == 0){
			this.dispose();
		}
	}
	
	

}
