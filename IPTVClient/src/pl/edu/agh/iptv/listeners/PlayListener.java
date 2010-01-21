package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

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

	private MenuListItem lastSelected;

	private boolean isPaused = false;

	private String playedMovie;
	private String playedQuality;
	private Integer playedCategory;

	JPopupMenu menu = qualityChoice();

	public PlayListener(IPTVClient client, MainView mainView) {
		this.iptvClient = client;
		this.moviesTab = mainView.getMoviesTab();
		this.mainView = mainView;
	}

	public void actionPerformed(ActionEvent e) {

		JMenuItem clickedItem = (JMenuItem) e.getSource();

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

					playedMovie = menuItem.getTitle();
					playedQuality = clickedItem.getLabel();
					playedCategory = menuItem.getCategory();

					iptvClient.showChosenMovie(playedMovie, playedQuality);
				}

				mainView.getPlayButton().setIcon(MainView.pauseIcon);

			} else {
				try {
					VLCHelper.playlist.togglePause();
				} catch (VLCException e1) {
					e1.printStackTrace();
				}
				mainView.getPlayButton().setIcon(MainView.playIcon);
			}
		}
	}

	private JPopupMenu qualityChoice() {

		JPopupMenu popup = new JPopupMenu();

		JMenuItem item;
		popup.add(item = new JMenuItem("Left"));
		item.setHorizontalTextPosition(JMenuItem.RIGHT);
		popup.add(item = new JMenuItem("Center"));
		item.setHorizontalTextPosition(JMenuItem.RIGHT);
		popup.add(item = new JMenuItem("Right"));
		item.setHorizontalTextPosition(JMenuItem.RIGHT);
		popup.add(item = new JMenuItem("Full"));
		item.setHorizontalTextPosition(JMenuItem.RIGHT);
		popup.addSeparator();
		popup.add(item = new JMenuItem("Settings . . ."));

		popup.setLabel("Justification");

		return popup;
	}

	public void playBackAfterPause() {

	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public String getPlayedMovie() {
		return this.playedMovie;
	}

	public String getPlayedMovieQuality() {
		return this.playedQuality;
	}

	public void setPlayedMovie() {

	}

	public Integer getCategory() {
		return VLCHelper.isPlayingMovie ? this.playedCategory : -1;
	}

}
