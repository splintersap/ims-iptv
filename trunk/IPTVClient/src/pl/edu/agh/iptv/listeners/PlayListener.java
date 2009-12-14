package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;

import org.videolan.jvlc.VLCException;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.components.MenuListItem;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class PlayListener implements ActionListener {

	private IPTVClient iptvClient;
	private MoviesTab moviesTab;
	private MainView mainView;
	private boolean isPlayButton = true;

	public PlayListener(IPTVClient client, MainView mainView) {
		this.iptvClient = client;
		this.moviesTab = mainView.getMoviesTab();
		this.mainView = mainView;
	}

	public void actionPerformed(ActionEvent e) {

		JButton button = (JButton) e.getSource();

		JList list = moviesTab.getAllMoviesList();
		Object value = list.getSelectedValue();
		mainView.getStopButton().setEnabled(true);
		if (value != null) {

			if (mainView.getPlayButton().getIcon().equals(MainView.playIcon)) {
				if (VLCHelper.isPlayingMovie) {
					try {
						VLCHelper.playlist.togglePause();						
					} catch (VLCException e1) {
						e1.printStackTrace();
					}
				} else {
					MenuListItem menuItem = (MenuListItem) value;
					iptvClient.showChosenMovie(menuItem.getTitle());
				}

				button.setIcon(MainView.pauseIcon);
				isPlayButton = false;

			} else {

				try {
					VLCHelper.playlist.togglePause();
				} catch (VLCException e1) {
					e1.printStackTrace();
				}

				isPlayButton = true;
				button.setIcon(MainView.playIcon);
			}
		}
	}

}
