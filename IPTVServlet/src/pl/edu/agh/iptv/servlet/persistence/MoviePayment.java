package pl.edu.agh.iptv.servlet.persistence;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MoviePayment extends pl.edu.agh.iptv.servlet.persistence.Entity {

	private Movie movie;
	private long pirce;
	private Quality quality;

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

	public MoviePayment() {
	}

	public MoviePayment(Movie movie, long pirce, Quality quality) {
		this.movie = movie;
		this.pirce = pirce;
		this.quality = quality;
	}

}
