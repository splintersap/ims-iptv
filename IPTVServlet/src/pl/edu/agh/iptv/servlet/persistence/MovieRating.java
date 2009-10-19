package pl.edu.agh.iptv.servlet.persistence;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MovieRating extends pl.edu.agh.iptv.servlet.persistence.Entity {
	private User user;
	private Movie movie;
	private int rating;

	public MovieRating() {
		
	}
	
	public MovieRating(User user, Movie movie, int rating) {
		this.user = user;
		this.movie = movie;
		this.rating = rating;
	}



	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Movie.class)
	@JoinColumn(nullable = false)
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
