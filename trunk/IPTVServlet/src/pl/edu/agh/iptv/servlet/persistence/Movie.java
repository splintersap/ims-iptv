package pl.edu.agh.iptv.servlet.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Movies")
public class Movie extends pl.edu.agh.iptv.servlet.persistence.Entity {

	@Column(nullable = false, unique = true)
	private String title;

	@Enumerated(EnumType.STRING)
	private Category category;

	private String director;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String moviePath;

	private List<MovieRating> ratingList = new ArrayList<MovieRating>();
	private List<MoviePayments> paymentsList = new ArrayList<MoviePayments>();
	private List<MovieComment> commentsList = new ArrayList<MovieComment>();

	public Movie() {
	}

	public Movie(String title, String moviePath) {
		this.title = title;
		this.moviePath = moviePath;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	@javax.persistence.Column(length=10000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMoviePath() {
		return moviePath;
	}

	public void setMoviePath(String moviePath) {
		this.moviePath = moviePath;
	}

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = MovieRating.class)
	public List<MovieRating> getRating() {
		return ratingList;
	}

	public void setRating(List<MovieRating> ratingList) {
		this.ratingList = ratingList;
	}

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = MoviePayments.class)
	public List<MoviePayments> getMoviePayments() {
		return paymentsList;
	}

	public void setMoviePayments(List<MoviePayments> paymentsList) {
		this.paymentsList = paymentsList;
	}

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = MovieComment.class)
	public List<MovieComment> getCommentsList() {
		return commentsList;
	}

	
	public void setCommentsList(List<MovieComment> commentsList) {
		this.commentsList = commentsList;
	}
}
