package pl.edu.agh.iptv.simulators;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.ims.commons.CommonMovie;

public class Simulator {

	List<CommonMovie> movies;

	public Simulator() {
		simulateData();
	}

	private void simulateData() {

		movies = new ArrayList<CommonMovie>();
		movies.add(new CommonMovie("Forest Gump", "comedy",
				"Very interesting movie about stupi guy ;).", "Ernest Wozniak",
				5, 4.5));
		movies
				.add(new CommonMovie(
						"Armagedon",
						"action",
						"Huge metheor threatens the Earth. Will a group of brave spacemans save the world from the disaster ? \n a \n a \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  \n a  ",
						"Michal Wasniowski", 4, 3.5));
		movies.add(new CommonMovie("Borat", "comedy",
				"Borat goes to USA to find out more about this funny country.",
				"Maciej Sajdak", 3, 2.5));

	}

	public List<CommonMovie> getMovies() {
		return this.movies;
	}
}
