package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.OrderMovieView;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class OrderMovieListener implements ActionListener {
	
	private MoviesTab moviesTab;
	private IPTVClient iptvClient;

	public OrderMovieListener(IPTVClient iptvClient, MoviesTab moviesTab) {
		this.moviesTab = moviesTab;
		this.iptvClient = iptvClient;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		new OrderMovieView((String) moviesTab.getAllMoviesList()
				.getSelectedValue());
	}

}
