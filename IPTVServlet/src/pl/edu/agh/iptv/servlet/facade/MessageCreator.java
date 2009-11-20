package pl.edu.agh.iptv.servlet.facade;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.sip.SipServlet;

import net.sourceforge.jsdp.Attribute;
import net.sourceforge.jsdp.Information;
import net.sourceforge.jsdp.Origin;
import net.sourceforge.jsdp.SDPException;
import net.sourceforge.jsdp.SDPFactory;
import net.sourceforge.jsdp.SessionDescription;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MovieComment;
import pl.edu.agh.iptv.persistence.MoviePayment;
import pl.edu.agh.iptv.persistence.MovieRating;
import pl.edu.agh.iptv.persistence.Quality;
import pl.edu.agh.iptv.persistence.User;

public class MessageCreator {

	@SuppressWarnings("unchecked")
	private static Movie getMovieFromTitle(String title, EntityManager em) {
		Query query = em
				.createQuery("FROM Movie WHERE title = '" + title + "'");
		List<Movie> movieList = query.getResultList();
		Movie movie = movieList.get(0);
		return movie;
	}

	public static SessionDescription createSDPFromMovie(String movieTitle,
			String sip, EntityManager em) {
		SessionDescription sessionDescription = null;
		Movie movie = getMovieFromTitle(movieTitle, em);

		try {
			sessionDescription = SDPFactory.createSessionDescription();
			sessionDescription.setVersion(SDPFactory.createVersion());
			Origin orgin = SDPFactory.createOrigin();
			sessionDescription.setOrigin(orgin);

			if (movie.getTitle() != null && (!movie.getTitle().isEmpty())) {
				Information info = SDPFactory.createInformation(movie
						.getTitle());
				sessionDescription.setInformation(info);
			}

			if (movie.getDescription() != null && (!movie.getDescription().isEmpty())) {
				System.err.println("Description : " + movie.getDescription());
				Attribute description = SDPFactory.createAttribute(
						"description", movie.getDescription());
				sessionDescription.addAttribute(description);
			}

			if (movie.getCategory() != null ) {
				Attribute movieCategory = SDPFactory.createAttribute(
						"category", movie.getCategory().name());
				sessionDescription.addAttribute(movieCategory);
			}

			if (movie.getDirector() != null && (!movie.getDirector().isEmpty())) {
				Attribute director = SDPFactory.createAttribute("director",
						movie.getDirector());
				sessionDescription.addAttribute(director);
			}

			Attribute userRating = SDPFactory.createAttribute("userRating",
					String.valueOf(getUserRating(movie, sip)));
			sessionDescription.addAttribute(userRating);

			Attribute overallRating = SDPFactory.createAttribute(
					"overallRating", String.valueOf(movie.getOverallRating()));
			sessionDescription.addAttribute(overallRating);

			for (MovieComment comment : movie.getCommentsList()) {
				String commentString = comment.getUser().getSip() + "|"
						+ comment.getDate().getTime() + "|"
						+ comment.getComment();
				Attribute commentAtr = SDPFactory.createAttribute("comment",
						commentString);
				sessionDescription.addAttribute(commentAtr);
			}

			for (MoviePayment moviePayment : movie.getMoviePayments()) {
				Date date = moviePayment.getOrderByUser(sip);
				String dateString = null;
				if (date != null) {
					dateString = String.valueOf(date.getTime());
				}
				String moviePaymentString = moviePayment.getQuality() + "|"
						+ moviePayment.getPirce() + "|" + dateString;
				Attribute paymentAtr = SDPFactory.createAttribute("payment",
						moviePaymentString);
				sessionDescription.addAttribute(paymentAtr);

			}

		} catch (SDPException e) {
			e.printStackTrace();
		}

		return sessionDescription;

	}


	private static int getUserRating(Movie movie, String sip) {
		int userRating = 0;

		for (MovieRating rating : movie.getRating()) {
			if (sip.equals(rating.getUser().getSip())) {
				userRating = rating.getRating();
				break;
			}
		}

		return userRating;
	}

	public static void addComment(String title, String sip, String comment,
			EntityManager em) {
		Movie movie = getMovieFromTitle(title, em);
		User user = getUserFromSip(sip, em);
		movie.addMovieComment(comment, user);

	}

	private static User getUserFromSip(String sip, EntityManager em) {
		Query query = em.createQuery("FROM User WHERE sip = '" + sip + "'");
		List<User> userList = query.getResultList();
		User user = userList.get(0);
		return user;
	}

	public static void purchaseMovie(String title, String sip, String quality,
			EntityManager em) {
		User user = getUserFromSip(sip, em);
		Movie movie = getMovieFromTitle(title, em);
		user.addOrderedMovie(movie, Quality.valueOf(quality));
	}

	public static String createMovieList(String sip, EntityManager em) {
		StringBuilder stringBuilder = new StringBuilder("");
		Query query = em.createQuery("FROM Movie");
		List<Movie> movieList = query.getResultList();
		for (Movie movie : movieList) {
			boolean isOrdered = false;
			for (MoviePayment moviePayment : movie.getMoviePayments()) {
				Date date = moviePayment.getOrderByUser(sip);
				if (date != null) {
					isOrdered = true;
					break;
				}
			}
			stringBuilder.append(movie.getTitle() + "|" + isOrdered + "|"
					+ movie.getOverallRating());
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

}
