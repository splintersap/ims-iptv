package pl.edu.agh.iptv.dbmenager.telnettab;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.persistence.Query;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.edu.agh.iptv.dbmenager.main.Application;
import pl.edu.agh.iptv.persistence.Setting;
import pl.edu.agh.iptv.telnet.AbstractTelnetWorker;
import pl.edu.agh.iptv.telnet.CheckConnetcionTelnetClient;

public class TelnetTab extends JPanel {

	private Setting vlcipSetting = null;
	
	private Setting liveSetting = null;

	JTextField telnetIpTextField;
	
	JTextField telnetBroadcastIpTextField;

	private static final long serialVersionUID = -376429230198879816L;

	public TelnetTab() {
		this.setLayout(new BorderLayout());
		//this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		Query query = Application.getEntityMenager()
				.createQuery("FROM Setting");

		List<Setting> settingList = query.getResultList();
		String telnetIp = null;
		String liveVideoIp = null;
		for (Setting setting : settingList) {
			if (Setting.VLCIP.equals(setting.getName())) {
				this.vlcipSetting = setting;
				telnetIp = setting.getValue();
			} else if(Setting.BROADCASTIP.equals(setting.getName())) {
				this.liveSetting = setting;
				liveVideoIp = setting.getValue();;
				
			}
		}

		JLabel vodLabel = new JLabel("Video on demand");
		
		telnetIpTextField = new JTextField(telnetIp);

		JButton setButton = new JButton("Set");
		setButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TelnetTab.this.setTelnetIpAddress(vlcipSetting, telnetIpTextField.getText());
			}
		});

		JButton checkButton = new JButton("Check");
		checkButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TelnetTab.this.checkTelnetIpAddress(telnetIpTextField.getText());
			}
		});
		
		JLabel liveVideoLabel = new JLabel("Live video");
		
		telnetBroadcastIpTextField = new JTextField(liveVideoIp);
		
		JButton liveVideoSetButton = new JButton("Set");
		liveVideoSetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TelnetTab.this.setTelnetIpAddress(liveSetting, telnetBroadcastIpTextField.getText());
			}
		});

		JButton liveVideoCheckButton = new JButton("Check");
		liveVideoCheckButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TelnetTab.this.checkTelnetIpAddress(telnetBroadcastIpTextField.getText());
			}
		});

		JPanel northPanel = new JPanel();
	
		northPanel.setLayout(new GridLayout(3, 6, 10, 10));
		
		northPanel.add(new JPanel());
		northPanel.add(new JPanel());
		northPanel.add(new JPanel());
		northPanel.add(new JPanel());
		northPanel.add(new JPanel());
		northPanel.add(new JPanel());
		
		northPanel.add(new JPanel());
		northPanel.add(vodLabel);
		northPanel.add(telnetIpTextField);
		northPanel.add(setButton);
		northPanel.add(checkButton);
		northPanel.add(new JPanel());
		
		northPanel.add(new JPanel());
		northPanel.add(liveVideoLabel);
		northPanel.add(telnetBroadcastIpTextField);
		northPanel.add(liveVideoSetButton);
		northPanel.add(liveVideoCheckButton);
		northPanel.add(new JPanel());
		
		

		add(northPanel, BorderLayout.NORTH);
	}

	protected void checkTelnetIpAddress(String address) {
		AbstractTelnetWorker telnet = new CheckConnetcionTelnetClient(
				address);
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

	protected void setTelnetIpAddress(Setting setting, String value) {
		Application.getEntityMenager().getTransaction().begin();

		Application.getEntityMenager().refresh(setting);
		setting.setValue(value);

		Application.getEntityMenager().getTransaction().commit();
		JOptionPane.showInternalMessageDialog(this.getParent(),
				"Telnet address was set up sucessful");
	}

}
