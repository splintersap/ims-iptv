package pl.edu.agh.iptv.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.iptv.data.Movie;

public class MoviesController {

	List<Movie> movies;

	private Map<String, Movie> moviesMap;

	public MoviesController(List<Movie> movies) {

		this.movies = movies;

		moviesMap = new HashMap<String, Movie>();
		for (Movie movie : movies) {
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
	public Movie getMovieByName(String movieTitle) {
		return this.moviesMap.get(movieTitle);
	}

	public String[] getTitlesOfBoughtMovies() {
		String[] titles = new String[this.movies.size()];

		int i = 0;
		for (Movie movie : this.movies) {
			titles[i] = movie.getTitle();
			i++;
		}

		return titles;
	}

}
