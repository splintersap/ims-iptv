package pl.edu.agh.iptv.listeners;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.controllers.MainController;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.components.MenuListItem;
import pl.edu.agh.iptv.view.movies.MoviesTab;

/**
 * This class listens to the selections performed on the movies list. In case of
 * a selection user gets the description of a selected movie.
 * 
 * @author Wozniak
 * 
 */
public class DescriptionListener implements ListSelectionListener {

	/*
	 * Necessary in order to get information about movies.
	 */
	private IPTVClient iptvClient = null;

	private MainController mainController;

	private MoviesTab moviesTab;

	private MainView mainView;

	private MenuListItem lastSelected = null;

	public static ImageIcon loadingIcon = new ImageIcon("images/loading.gif");

	/*
	 * Title of selected movie.
	 */
	private static String selectedMovie = null;

	public static String getSelectedMovie() {
		return selectedMovie;
	}

	public DescriptionListener(IPTVClient iptvClient,
			MainController mainController, MainView mainView) {
		this.iptvClient = iptvClient;
		this.mainController = mainController;
		this.mainView = mainView;
		this.moviesTab = mainView.getMoviesTab();
	}

	public void valueChanged(ListSelectionEvent selection) {

		PlayListener playListener = mainView.getPlayListener();
		if (playListener.getCategory() == MenuListItem.BROADCAST)
			iptvClient.closeBroadcast(playListener.getPlayedMovie(),
					playListener.getPlayedMovieQuality());

		if (selection.getValueIsAdjusting() == true) {
			return;
		}

		JList list = (JList) selection.getSource();
		MenuListItem itemList = (MenuListItem) list.getSelectedValue();
		if (itemList == null) {
			return;
		}
		String item = itemList.getTitle();
		selectedMovie = item;

		this.iptvClient.getMovieInformations(item);
		mainController.stopMovie(iptvClient);
		JPanel loadingPanel = new JPanel(new BorderLayout());
		JLabel loadingIconLabel = new JLabel(loadingIcon);
		loadingIconLabel.setBorder(BorderFactory.createEmptyBorder());
		loadingIconLabel.setForeground(Color.red);
		loadingIconLabel.setBackground(Color.red);

		Dimension size = new Dimension(loadingIcon.getIconWidth(), loadingIcon
				.getIconHeight());
		loadingIconLabel.setPreferredSize(size);
		loadingIconLabel.setMinimumSize(size);
		loadingIconLabel.setMaximumSize(size);
		loadingIconLabel.setSize(size);
		loadingPanel.add(loadingIconLabel, BorderLayout.CENTER);
		// loadingPanel.add(new ImagePanel(loadingIcon), BorderLayout.CENTER);
		moviesTab.setDescriptionPanel(loadingPanel);

		mainView.getPlayListener().setPaused(false);
		VLCHelper.isPlayingMovie = false;

	}

}

class ImagePanel extends JPanel {

	private Image img;

	public ImagePanel(ImageIcon img) {
		this(img.getImage());
	}

	public ImagePanel(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 1, 0, null);
	}

}
