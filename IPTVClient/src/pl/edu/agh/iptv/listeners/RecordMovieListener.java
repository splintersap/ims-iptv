package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.RecordMovieView;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class RecordMovieListener implements ActionListener {

	private IPTVClient iptvClient;

	private JFrame parent;

	private MoviesTab moviesTab;

	public RecordMovieListener(IPTVClient iptvClient, JFrame parent,
			MoviesTab moviesTab) {
		this.iptvClient = iptvClient;
		this.parent = parent;
		this.moviesTab = moviesTab;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {

			if (((JButton) e.getSource()).getName().compareTo("RECORD") == 0) {
				//new RecordMovieView(this, parent, (String) moviesTab
				//		.getOrderedMoviesList().getSelectedValue());
			} else if (((JButton) e.getSource()).getName().compareTo("OK") == 0) {

			} else if (((JButton) e.getSource()).getName().compareTo("CANCELa") == 0) {

			}

		}
	}
}
