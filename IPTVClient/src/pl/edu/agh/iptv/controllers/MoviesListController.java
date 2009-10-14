package pl.edu.agh.iptv.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class MoviesListController{

	private MoviesTab moviesTab = null;
	private IPTVClient client = null;

	public MoviesListController(MoviesTab moviesTab, IPTVClient client) {
		this.moviesTab = moviesTab;
		this.client = client;
	}

}
