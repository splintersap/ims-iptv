package pl.edu.agh.iptv.simulators;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.iptv.data.Movie;

public class Simulator {

	List<Movie> movies;

	public Simulator() {
		simulateData();
	}

	private void simulateData() {

		movies = new ArrayList<Movie>();
		movies.add(new Movie("Forest Gump", "comedy",
				"Very interesting movie about stupi guy ;).", "Ernest Wozniak",
				5, 4.5));
		movies
				.add(new Movie(
						"Armagedon",
						"action",
						"Huge metheor threatens the Earth. Will a group of brave spacemans save the world from the disaster ? \n a \n a \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  ",
						"Michal Wasniowski", 4, 3.5));
		movies.add(new Movie("Borat", "comedy",
				"Borat goes to USA to find out more about this funny country.",
				"Maciej Sajdak", 3, 2.5));

	}

	public List<Movie> getMovies() {
		return this.movies;
	}
}
