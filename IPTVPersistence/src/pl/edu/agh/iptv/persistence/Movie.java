package pl.edu.agh.iptv.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Entity;

@Entity
@Table(name = "Movies")
public class Movie extends pl.edu.agh.iptv.persistence.Entity {

	@Column(nullable = false, unique = true)
	private String title;

	@Enumerated(EnumType.STRING)
	private Category category;

	private String director;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String url;
	
	private String uuid;

	private List<MovieRating> ratingList = new ArrayList<MovieRating>();
	private List<MoviePayment> paymentsList = new ArrayList<MoviePayment>();
	private List<MovieComment> commentsList = new ArrayList<MovieComment>();

	public Movie() {
	}

	public Movie(String title, String moviePath) {
		this.title = title;
		this.url = moviePath;
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

	public String getMovieUrl() {
		return url;
	}

	public void setMovieUrl(String moviePath) {
		this.url = moviePath;
	}

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = MovieRating.class)
	public List<MovieRating> getRating() {
		return ratingList;
	}

	public void setRating(List<MovieRating> ratingList) {
		this.ratingList = ratingList;
	}

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = MoviePayment.class)
	public List<MoviePayment> getMoviePayments() {
		return paymentsList;
	}

	public void setMoviePayments(List<MoviePayment> paymentsList) {
		this.paymentsList = paymentsList;
	}

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = MovieComment.class)
	public List<MovieComment> getCommentsList() {
		return commentsList;
	}

	
	public void setCommentsList(List<MovieComment> commentsList) {
		this.commentsList = commentsList;
	}
	
	public void addMoviePayment(long price, Quality quality) {
		MoviePayment mp = new MoviePayment(this, price, quality);
		paymentsList.add(mp);
	}
	
	public void addMovieComment(String comment, User user) {
		MovieComment mc = new MovieComment(comment, user, this);
		commentsList.add(mc);
	}
	
	public void addMovieRating(User user, int rating) {
		MovieRating mr = new MovieRating(user, this, rating);
		ratingList.add(mr);
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
	@Transient
	public MoviePayment getMoviePayments(Quality quality) {
		
		Iterator<MoviePayment> iterator = paymentsList.iterator();
		while(iterator.hasNext()) {
			MoviePayment payment = iterator.next();
			if(payment.getQuality().equals(quality)) {
				return payment;
			}
		}
		
		return null;
	}
	
	@Transient
	public Double getOverallRating()
	{
		double overallRating = 0.0;

		for (MovieRating rating : getRating()) {
			overallRating += rating.getRating();
		}

		// In case dividing by 0
		if (getRating().size() != 0) {
			overallRating /= getRating().size();
		}

		return overallRating;
	}
}
