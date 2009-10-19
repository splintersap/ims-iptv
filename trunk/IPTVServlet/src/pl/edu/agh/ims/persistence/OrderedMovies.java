package pl.edu.agh.ims.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderedMovies extends pl.edu.agh.ims.persistence.Entity {

	private User user;
	private OrderedMovies orderedMovies;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = OrderedMovies.class)
	@JoinColumn(nullable = false)
	public OrderedMovies getOrderedMovies() {
		return orderedMovies;
	}

	public void setOrderedMovies(OrderedMovies orderedMovies) {
		this.orderedMovies = orderedMovies;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	private Date date;

}
