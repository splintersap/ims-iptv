package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import pl.edu.agh.iptv.view.CommonWatchingView;
import pl.edu.agh.iptv.view.CommonWatchingWaiting;
import pl.edu.agh.iptv.view.chat.Chat;

public class CommonWatchingListener implements ActionListener {

	private Chat chat;
	private CommonWatchingView commonView;
	private JFrame mainFrame;

	public CommonWatchingListener(JFrame mainFrame, Chat chat,
			CommonWatchingView commonView) {
		this.mainFrame = mainFrame;
		this.chat = chat;
		this.commonView = commonView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (((JButton) e.getSource()).getName().compareTo("COMMON_OK") == 0) {
			chat.callGroup(commonView.getUrisToUsers(), commonView
					.getMovieToWatch(), commonView.getStartDate()
					+ commonView.getHour() + ":" + commonView.getMinute());
			commonView.dispose();
			this.chat.setCommonWaiting(new CommonWatchingWaiting(
					this.mainFrame, commonView.getUrisToUsers()));
		} else if (((JButton) e.getSource()).getName().compareTo(
				"COMMON_CANCEL") == 0) {
			commonView.dispose();
		}

	}

}
