package pl.edu.agh.iptv.servlet.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderedMovie extends pl.edu.agh.iptv.servlet.persistence.Entity {

	private User user;
	private MoviePayment moviePayment;
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MoviePayment.class)
	@JoinColumn(nullable = false)
	public MoviePayment getMoviePayment() {
		return moviePayment;
	}
	
	public void setMoviePayment(MoviePayment moviePayment) {
		this.moviePayment = moviePayment;
	}

	public OrderedMovie() {
	}
	
	public OrderedMovie(User user, MoviePayment moviePayment) {
		this.user = user;
		this.moviePayment = moviePayment;
		this.date = new Date();
	}

	public void setOrderedMovies(MoviePayment moviePayment) {
		this.moviePayment = moviePayment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
