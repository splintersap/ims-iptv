package pl.edu.agh.iptv.listeners;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.commons.CommonMovie;
import pl.edu.agh.iptv.view.movies.DescriptionPanel;
import pl.edu.agh.iptv.view.movies.MovieComments;
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

	/*
	 * Necessary in order to update the view with new information.
	 */
	private MoviesTab moviesTab = null;

	/*
	 * Title of selected movie.
	 */
	private static String selectedMovie = null;

	public static String getSelectedMovie() {
		return selectedMovie;
	}

	public DescriptionListener(IPTVClient iptvClient, MoviesTab moviesTab) {
		this.iptvClient = iptvClient;
		this.moviesTab = moviesTab;
	}

	public void valueChanged(ListSelectionEvent selection) {

		if (selection.getValueIsAdjusting() == true) {
			return;
		}

		JList list = (JList) selection.getSource();
		String item = (String) list.getSelectedValue();
		if (item == null) {
			return;
		}
		selectedMovie = item;
		// CommonMovie movie =
		this.iptvClient.getMovieInformations(item, this);

		/*
		 * getMoviesController() .getMovieByName(item);
		 * 
		 * DescriptionPanel descriptionPanel = new DescriptionPanel(movie);
		 * MovieComments movieComments = descriptionPanel.getMovieComments();
		 * movieComments.getCommentButton().addActionListener( new
		 * CommentListener(iptvClient, movieComments));
		 * 
		 * descriptionPanel.getRatingPanel().setIPTVClient(iptvClient);
		 * 
		 * this.moviesTab.setDescriptionPanel(descriptionPanel);
		 */

	}

	public void displayDescription(CommonMovie movie) {

		DescriptionPanel descriptionPanel = new DescriptionPanel(movie);
		MovieComments movieComments = descriptionPanel.getMovieComments();
		movieComments.getCommentButton().addActionListener(
				new CommentListener(iptvClient, movieComments));

		descriptionPanel.getRatingPanel().setIPTVClient(iptvClient);

		this.moviesTab.setDescriptionPanel(descriptionPanel);

	}
}
