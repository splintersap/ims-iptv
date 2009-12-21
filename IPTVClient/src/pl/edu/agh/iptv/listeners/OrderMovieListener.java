package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.OrderMovieView;
import pl.edu.agh.iptv.view.components.MenuListItem;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class OrderMovieListener implements ActionListener {

	private MoviesTab moviesTab;
	private OrderMovieView view;
	private JFrame parent;
	private IPTVClient iptvClient;

	public OrderMovieListener(IPTVClient iptvClient, MoviesTab moviesTab,
			JFrame parent) {
		this.moviesTab = moviesTab;
		this.iptvClient = iptvClient;
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() instanceof JButton) {
			if (((JButton) e.getSource()).getName() == "OK") {
				if (view.getSelectedQuality() != null) {
					iptvClient.purchaseMovie(view.getSelectedMovieTitle(), view
							.getSelectedQuality());
					iptvClient.actionPerformed(null);
					view.dispose();
				}
			} else if (((JButton) e.getSource()).getName() == "CANCEL") {
				view.dispose();
			} else if (((JButton) e.getSource()).getName() == "ORDER") {
				MenuListItem menuItem = (MenuListItem) moviesTab
						.getAllMoviesList().getSelectedValue();
				view = new OrderMovieView(menuItem.getTitle(), this, parent,
						this.moviesTab);

			}
		}
	}

}
