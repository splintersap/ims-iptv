package pl.edu.agh.iptv.controllers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.listeners.DescriptionListener;
import pl.edu.agh.iptv.listeners.IperfManagerListener;
import pl.edu.agh.iptv.listeners.PlayListener;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.movies.MoviesTab;
import pl.edu.agh.performance.client.IPTVPerformanceClient;

public class MainController {

	MainView mainView = null;
	MoviesTab moviesTab = null;

	public MainController() {

		JFrame.setDefaultLookAndFeelDecorated(true);
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
				IPTVClient iptvClient = new IPTVClient(mainView.getMoviesTab());

				/*
				 * Adding listener to the list with movies.
				 */
				moviesTab.getMoviesList().addListSelectionListener(
						new DescriptionListener(iptvClient, moviesTab));

				mainView.getPlayButton().addActionListener(
						new PlayListener(iptvClient, moviesTab));

				mainView.getRefreshButton().addActionListener(iptvClient);

				IPTVPerformanceClient performance = new IPTVPerformanceClient(
						"192.168.0.67", mainView.getBandwidthLabel());
				performance.queryServer();

				mainView.setWindowCloseOperation(new IperfManagerListener(
						performance.getIperfThread()));

			}
		});

	}

}
