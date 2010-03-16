package pl.edu.agh.iptv.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.videolan.jvlc.VLCException;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.listeners.DescriptionListener;
import pl.edu.agh.iptv.listeners.OrderMovieListener;
import pl.edu.agh.iptv.listeners.PlayListener;
import pl.edu.agh.iptv.listeners.RecordMovieListener;
import pl.edu.agh.iptv.presence.controller.PresenceController;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.components.MenuListItem;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class MainController {

	MainView mainView = null;
	MoviesTab moviesTab = null;

	public MainController() {

		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					/*
					 * Setting the new look and feel.
					 */
					UIManager
							.setLookAndFeel("org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel");
				} catch (Exception e) {
					System.out
							.println("Substance Raven Graphite failed to initialize");
				}
				/*
				 * Creating the main view.
				 */
				mainView = new MainView();
				moviesTab = mainView.getMoviesTab();

				/*
				 * Creating instance of a class responsible for communication
				 * with servlet.
				 */
				final IPTVClient iptvClient = new IPTVClient(mainView);

				moviesTab.getAllMoviesList().addListSelectionListener(
						new DescriptionListener(iptvClient,
								MainController.this,
								MainController.this.mainView));

				// mainView.getPlayButton().addActionListener(
				// new PlayListener(iptvClient, mainView));
				mainView.setPlayListner(new PlayListener(iptvClient, mainView));

				mainView.getStopButton().addActionListener(
						new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								
								/*
								 * This part of code needs to precede stopMovie(iptvClient) 
								 */
								PlayListener playListener = mainView
								.getPlayListener();
								if (playListener.getCategory() == MenuListItem.MULTICAST)
									iptvClient.closeMulticast(playListener
											.getPlayedMovie(), playListener
											.getPlayedMovieQuality());
								/*
								 * ******************************************************
								 */
								stopMovie(iptvClient);
								
								// mainView.getPlayButton().setEnabled(true);
							}
						});

				mainView.getRefreshButton().addActionListener(iptvClient);

				mainView.getOrderMoviebButton().addActionListener(
						new OrderMovieListener(iptvClient, moviesTab, mainView
								.getMainFrame()));

				mainView.getRecordMovieButton().addActionListener(
						new RecordMovieListener(iptvClient, mainView
								.getMainFrame(), moviesTab));

				/*
				 * Functionality responsible for chat.
				 */
				new PresenceController(iptvClient, IPTVClient.getProfile(),
						mainView);

				// IPTVPerformanceClient performance = new
				// IPTVPerformanceClient(
				// "192.168.1.224", mainView.getBandwidthLabel());
				// performance.queryServer();

				// mainView.setWindowCloseOperation(new IperfManagerListener(
				// performance.getIperfThread()));

			}
		});

	}

	public void stopMovie(final IPTVClient iptvClient) {
		if (VLCHelper.playlist != null) {
			try {
				VLCHelper.playlist.stop();
			} catch (VLCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// VLCHelper.ds.release();
		}

		VLCHelper.isPlayingMovie = false;
		mainView.getPlayButton().setIcon(MainView.playIcon);
		mainView.getPlayListener().setPaused(false);
		MenuListItem item = (MenuListItem) moviesTab.getAllMoviesList()
				.getSelectedValue();
		iptvClient.getMovieInformations(item.getTitle());
	}

}
