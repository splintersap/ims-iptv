package pl.edu.agh.iptv.dbmenager.telnettab;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.persistence.Query;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.edu.agh.iptv.dbmenager.main.Application;
import pl.edu.agh.iptv.persistence.Setting;
import pl.edu.agh.iptv.telnet.AbstractTelnetWorker;
import pl.edu.agh.iptv.telnet.CheckConnetcionTelnetClient;

public class TelnetTab extends JPanel {

	private Setting vlcipSetting = null;

	JTextField telnetIpTextField;

	private static final long serialVersionUID = -376429230198879816L;

	public TelnetTab() {
		Query query = Application.getEntityMenager()
				.createQuery("FROM Setting");

		List<Setting> settingList = query.getResultList();
		String telnetIp = null;
		for (Setting setting : settingList) {
			if (Setting.VLCIP.equals(setting.getName())) {
				this.vlcipSetting = setting;
				telnetIp = setting.getValue();
				break;
			}
		}

		telnetIpTextField = new JTextField(telnetIp);

		JButton setButton = new JButton("Set");
		setButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TelnetTab.this.setTelnetIpAddress();
			}
		});

		JButton checkButton = new JButton("Check");
		checkButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TelnetTab.this.checkTelnetIpAddress();
			}
		});

		add(telnetIpTextField);
		add(setButton);
		add(checkButton);

	}

	protected void checkTelnetIpAddress() {
		AbstractTelnetWorker telnet = new CheckConnetcionTelnetClient(
				telnetIpTextField.getText());
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		AbstractTelnetWorker.doTelnetWork(telnet);
		setCursor(Cursor.getDefaultCursor());

		if (telnet.isTelnetWorking()) {
			JOptionPane.showInternalMessageDialog(this.getParent(),
					"Telnet is working");
		} else {
			JOptionPane
			.showMessageDialog(this.getParent(),
					"Telnet is not working", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void setTelnetIpAddress() {
		Application.getEntityMenager().getTransaction().begin();

		Application.getEntityMenager().refresh(vlcipSetting);
		vlcipSetting.setValue(telnetIpTextField.getText());

		Application.getEntityMenager().getTransaction().commit();
		JOptionPane.showInternalMessageDialog(this.getParent(),
				"Telnet address was set up sucessful");
	}

}
