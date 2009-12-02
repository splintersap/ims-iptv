package pl.edu.agh.iptv.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.listeners.DescriptionListener;
import pl.edu.agh.iptv.listeners.OrderMovieListener;
import pl.edu.agh.iptv.listeners.PlayListener;
import pl.edu.agh.iptv.listeners.RecordMovieListener;
import pl.edu.agh.iptv.performance.client.PerformanceLauncher;
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

				/*
				 * Adding listener to the list with movies.
				 */
				// moviesTab.getOrderedMoviesList().addListSelectionListener(
				// new DescriptionListener(iptvClient));
				moviesTab.getAllMoviesList().addListSelectionListener(
						new DescriptionListener(iptvClient));

				MainView.getPlayButton().addActionListener(
						new PlayListener(iptvClient, moviesTab));

				mainView.getStopButton().addActionListener(
						new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								
								/*if(VLCHelper.video != null) {
									VLCHelper.video.destroyVideo(VLCHelper.mp);
								}*/
								
								if(VLCHelper.mp != null) {
									VLCHelper.mp.stop();
									//VLCHelper.jvlc.release();
									//VLCHelper.ds.release();
								}
								
								/*if (VLCHelper.mp != null) {
									VLCHelper.mp.stop();
									
								}
								
								if(VLCHelper.ds != null) {
									VLCHelper.ds.release();
								}*/
								
								
								
								VLCHelper.isPlayingMovie = false;
								MainView.getPlayButton().setIcon(MainView.playIcon);
								MenuListItem item = (MenuListItem)moviesTab.getAllMoviesList().getSelectedValue();
								iptvClient.getMovieInformations(item.getTitle());
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
				new PresenceController(IPTVClient.getProfile(), mainView);

				// IPTVPerformanceClient performance = new
				// IPTVPerformanceClient(
				// "192.168.1.224", mainView.getBandwidthLabel());
				// performance.queryServer();

				// mainView.setWindowCloseOperation(new IperfManagerListener(
				// performance.getIperfThread()));

				new Thread(new PerformanceLauncher(mainView, "192.168.1.224"))
						.start();

			}
		});

	}

}
