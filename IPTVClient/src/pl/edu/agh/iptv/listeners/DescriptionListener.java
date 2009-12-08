package pl.edu.agh.iptv.listeners;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.controllers.MainController;
import pl.edu.agh.iptv.view.components.MenuListItem;

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
	
	private MainController mainController;

	/*
	 * Title of selected movie.
	 */
	private static String selectedMovie = null;

	public static String getSelectedMovie() {
		return selectedMovie;
	}

	public DescriptionListener(IPTVClient iptvClient, MainController mainController) {
		this.iptvClient = iptvClient;
		this.mainController = mainController;
	}

	public void valueChanged(ListSelectionEvent selection) {

		if (selection.getValueIsAdjusting() == true ) {
			return;
		}

		JList list = (JList) selection.getSource();
		MenuListItem itemList = (MenuListItem) list.getSelectedValue();
		if (itemList  == null) {
			return;
		}
		String item = itemList.getTitle();
		selectedMovie = item;

		this.iptvClient.getMovieInformations(item);
		mainController.stopMovie(iptvClient);

	}

}
