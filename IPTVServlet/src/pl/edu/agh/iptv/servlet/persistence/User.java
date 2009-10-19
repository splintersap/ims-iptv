package pl.edu.agh.iptv.servlet.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User extends pl.edu.agh.iptv.servlet.persistence.Entity {
	@Column(nullable = false, unique = true)
	private String sip;
	private long credit;
	private List<MovieRating> ratingList = new ArrayList<MovieRating>();
	private List<OrderedMovies> orderedMoviesList = new ArrayList<OrderedMovies>();

	public User() {
		
	}
	
	public User(String sip) {
		this.sip = sip;
	}
	
	public String getSip() {
		return sip;
	}

	public void setSip(String sip) {
		this.sip = sip;
	}

	public long getCredit() {
		return credit;
	}

	public void setCredit(long credit) {
		this.credit = credit;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = MovieRating.class)
	public List<MovieRating> getRatingList() {
		return ratingList;
	}

	public void setRatingList(List<MovieRating> ratingList) {
		this.ratingList = ratingList;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = OrderedMovies.class)
	public List<OrderedMovies> getOrderedMoviesList() {
		return orderedMoviesList;
	}

	public void setOrderedMoviesList(List<OrderedMovies> orderedMoviesList) {
		this.orderedMoviesList = orderedMoviesList;
	}
}
