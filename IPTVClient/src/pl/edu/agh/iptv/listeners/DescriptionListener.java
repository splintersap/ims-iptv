package pl.edu.agh.iptv.listeners;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.IPTVClient;

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
	 * Title of selected movie.
	 */
	private static String selectedMovie = null;

	public static String getSelectedMovie() {
		return selectedMovie;
	}

	public DescriptionListener(IPTVClient iptvClient) {
		this.iptvClient = iptvClient;
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

		this.iptvClient.getMovieInformations(item);

	}

}
