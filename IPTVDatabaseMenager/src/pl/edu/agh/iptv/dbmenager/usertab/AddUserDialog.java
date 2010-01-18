package pl.edu.agh.iptv.dbmenager.usertab;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import pl.edu.agh.iptv.persistence.User;

public class AddUserDialog extends JDialog {

	private static final long serialVersionUID = -648904792275225490L;
	private JTextField sipTextField;
	private JSpinner quantitySpinner;
	UsersTab usersTab;
	
	AddUserDialog(JFrame frame, UsersTab usersTab) {
		super(frame);
		this.usersTab = usersTab;
		setLayout(new GridLayout(3, 2, 10, 6));

		JLabel sipLabel = new JLabel("Sip");
		sipTextField = new JTextField(25);

		JLabel credit = new JLabel("Credit");
		SpinnerModel model =
	        new SpinnerNumberModel(0, //initial value
	                               0, //min
	                               Integer.MAX_VALUE, //max
	                               1);                //step

		quantitySpinner = new JSpinner(model);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					User user = new User(sipTextField.getText());
					user.setCredit((Integer)quantitySpinner.getValue());
					AddUserDialog.this.usersTab.persistUser(user);
					AddUserDialog.this.dispose();
			}

		});
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AddUserDialog.this.dispose();
			}
		});

		add(sipLabel);
		add(sipTextField);
		add(credit);
		add(quantitySpinner);
		add(saveButton);
		add(closeButton);

		pack();
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

}
