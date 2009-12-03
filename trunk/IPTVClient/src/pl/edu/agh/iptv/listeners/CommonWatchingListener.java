package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import pl.edu.agh.iptv.view.CommonWatchingView;
import pl.edu.agh.iptv.view.chat.Chat;

public class CommonWatchingListener implements ActionListener {

	private Chat chat;
	private CommonWatchingView commonView;

	public CommonWatchingListener(Chat chat, CommonWatchingView commonView) {
		this.chat = chat;
		this.commonView = commonView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (((JButton) e.getSource()).getName().compareTo("COMMON_OK") == 0) {
			chat.callGroup(commonView.getUris());
		} else if (((JButton) e.getSource()).getName().compareTo(
				"COMMON_CANCEL") == 0) {
			commonView.dispose();
		}

	}

}
