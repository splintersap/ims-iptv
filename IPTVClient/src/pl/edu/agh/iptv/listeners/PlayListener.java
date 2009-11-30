package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.components.MenuListItem;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class PlayListener implements ActionListener {

	private IPTVClient iptvClient;
	private MoviesTab moviesTab;

	public PlayListener(IPTVClient client, MoviesTab moviesTab) {
		this.iptvClient = client;
		this.moviesTab = moviesTab;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		/*JList list = moviesTab.getOrderedMoviesList();
		int index = list.getSelectedIndex();
		Object value = list.getSelectedValue();
		if (value != null) {
			String name = list.getModel().getElementAt(index).toString();
			iptvClient.showChosenMovie(name);			
		}*/
		
		JList list = moviesTab.getAllMoviesList();
		Object value = list.getSelectedValue();
		if (value != null) {
			MenuListItem menuItem = (MenuListItem)value;
			iptvClient.showChosenMovie(menuItem.getTitle());	
		}
	}

}
