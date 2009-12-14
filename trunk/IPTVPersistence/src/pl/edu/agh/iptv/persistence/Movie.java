package pl.edu.agh.iptv.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Movies")
public class Movie extends pl.edu.agh.iptv.persistence.Entity {

	@Column(nullable = false, unique = true)
	private String title;

	@Enumerated(EnumType.STRING)
	private Category category;

	@Enumerated(EnumType.STRING)
	private MediaType mediaType;

	private String director;

	@Column(nullable = false)
	private String description;

	private String moviePath;
	
	private User recordingUser;
	
	private Date availableFrom;

	private List<MovieRating> ratingList = new ArrayList<MovieRating>();
	private List<MoviePayment> paymentsList = new ArrayList<MoviePayment>();
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

	@javax.persistence.Column(length = 10000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public MoviePayment addMoviePayment(long price, Quality quality) {
		MoviePayment mp = new MoviePayment(this, price, quality);
		paymentsList.add(mp);
		return mp;
	}

	public void addMovieComment(String comment, User user) {
		MovieComment mc = new MovieComment(comment, user, this);
		commentsList.add(mc);
	}

	public void addMovieRating(User user, int rating) {
		MovieRating mr = new MovieRating(user, this, rating);
		ratingList.add(mr);
	}


	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(nullable = true)
	public User getRecordingUser() {
		return recordingUser;
	}

	public void setRecordingUser(User recordingUser) {
		this.recordingUser = recordingUser;
	}

	public String getMoviePath() {
		return moviePath;
	}

	public void setMoviePath(String moviePath) {
		this.moviePath = moviePath;
	}

	@Transient
	public MoviePayment getMoviePayments(Quality quality) {

		Iterator<MoviePayment> iterator = paymentsList.iterator();
		while (iterator.hasNext()) {
			MoviePayment payment = iterator.next();
			if (payment.getQuality().equals(quality)) {
				return payment;
			}
		}

		return null;
	}

	@Transient
	public Double getOverallRating() {
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

	public Date getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(Date availableFrom) {
		this.availableFrom = availableFrom;
	}
	
	
}
