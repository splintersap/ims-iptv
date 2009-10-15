package pl.edu.agh.iptv.controllers;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.IPTVClient;

public class MoviesListController implements ListSelectionListener {

	private IPTVClient iptvClient = null;
	private boolean wasSelected = false;

	public MoviesListController(IPTVClient iptvClient) {
		this.iptvClient = iptvClient;
	}

	public void valueChanged(ListSelectionEvent selection) {
		// TODO Auto-generated method stub
		if (!wasSelected) {
			JList list = (JList) selection.getSource();
			String item = (String) list.getSelectedValue();
			this.iptvClient.showChosenMovie(item);
		}
		wasSelected = !wasSelected;
	}

}
