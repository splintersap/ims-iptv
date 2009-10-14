package pl.edu.agh.iptv.controllers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.movies.MoviesTab;

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
				mainView = new MainView();
			}
		});		

	}

}
