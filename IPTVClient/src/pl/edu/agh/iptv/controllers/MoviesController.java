package pl.edu.agh.iptv.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.ims.commons.CommonMovie;

public class MoviesController {

	List<CommonMovie> movies;

	private Map<String, CommonMovie> moviesMap;

	public MoviesController(List<CommonMovie> movies) {

		this.movies = movies;

		moviesMap = new HashMap<String, CommonMovie>();
		for (CommonMovie movie : movies) {
			moviesMap.put(movie.getTitle(), movie);
		}

	}

	/**
	 * Getting the movie by its title.
	 * 
	 * @param movieTitle
	 *            title of the movie
	 * @return representation of the movie
	 */
	public CommonMovie getMovieByName(String movieTitle) {
		return this.moviesMap.get(movieTitle);
	}

	public String[] getTitlesOfBoughtMovies() {
		String[] titles = new String[this.movies.size()];

		int i = 0;
		for (CommonMovie movie : this.movies) {
			titles[i] = movie.getTitle();
			i++;
		}

		return titles;
	}

}
