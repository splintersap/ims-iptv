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

@Entity
public class MoviePayment extends pl.edu.agh.iptv.persistence.Entity {

	private Movie movie;
	private long pirce;
	private Quality quality;
	@Column(nullable = false)
	private String url;
	private String uuid;
	private List<OrderedMovie> orderedMovieList = new ArrayList<OrderedMovie>();

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Movie.class)
	@JoinColumn(nullable = false)
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public long getPirce() {
		return pirce;
	}

	public void setPirce(long pirce) {
		this.pirce = pirce;
	}

	@Enumerated(EnumType.STRING)
	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}
	
	
	@OneToMany(mappedBy = "moviePayment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = OrderedMovie.class)
	public List<OrderedMovie> getOrderedMovieList() {
		return orderedMovieList;
	}

	public void setOrderedMovieList(List<OrderedMovie> orderedMovieList) {
		this.orderedMovieList = orderedMovieList;
	}

	public OrderedMovie getOrderByUser(String sip) {
		Iterator<OrderedMovie> iterator = orderedMovieList.iterator();
		while(iterator.hasNext()) {
			OrderedMovie orderedMovie = iterator.next();
			if(orderedMovie.getUser().getSip().equals(sip)) {
				return orderedMovie;
			}
		}
		
		return null;
	}
	
	public MoviePayment() {
	}

	public MoviePayment(Movie movie, long pirce, Quality quality) {
		this.movie = movie;
		this.pirce = pirce;
		this.quality = quality;
		this.uuid = UUID.randomUUID().toString();
	}
	
	public String getMovieUrl() {
		return url;
	}

	public void setMovieUrl(String moviePath) {
		this.url = moviePath;
	}
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public String toString() {
		return getQuality().name() + " : " +getPirce();
	}

}
