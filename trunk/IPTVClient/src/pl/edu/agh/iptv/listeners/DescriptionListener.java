package pl.edu.agh.iptv.listeners;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.ims.commons.CommonMovie;
import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.movies.DescriptionPanel;
import pl.edu.agh.iptv.view.movies.MoviesTab;

/**
 * This class listens to the selections performed on the movies list. In case of
 * a selection user gets the description of a selected movie.
 * 
 * @author Wozniak
 * 
 */
public class DescriptionListener implements ListSelectionListener {

	private IPTVClient iptvClient = null;
	private MoviesTab moviesTab = null;
	private boolean wasSelected = false;

	public DescriptionListener(IPTVClient iptvClient, MoviesTab moviesTab) {
		this.iptvClient = iptvClient;
		this.moviesTab = moviesTab;
	}

	public void valueChanged(ListSelectionEvent selection) {
		// TODO Auto-generated method stub
		if (!wasSelected) {
			JList list = (JList) selection.getSource();
			String item = (String) list.getSelectedValue();
			CommonMovie movie = this.iptvClient.getMoviesController()
					.getMovieByName(item);
			
			DescriptionPanel descriptionPanel = new DescriptionPanel(movie.getTitle(),
					movie.getDirector(), movie.getCategory(), movie
					.getDescription(), movie.getAllUsersRating());
			
			descriptionPanel.getRatingPanel().setIPTVClient(iptvClient);
			
			this.moviesTab.setDescriptionPanel(descriptionPanel);
			
		}
		wasSelected = !wasSelected;
	}
}
