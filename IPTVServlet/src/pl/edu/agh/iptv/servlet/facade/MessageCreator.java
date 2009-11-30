package pl.edu.agh.iptv.servlet.facade;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import net.sourceforge.jsdp.Attribute;
import net.sourceforge.jsdp.Information;
import net.sourceforge.jsdp.Origin;
import net.sourceforge.jsdp.SDPException;
import net.sourceforge.jsdp.SDPFactory;
import net.sourceforge.jsdp.SessionDescription;
import pl.edu.agh.iptv.persistence.MediaType;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MovieComment;
import pl.edu.agh.iptv.persistence.MoviePayment;
import pl.edu.agh.iptv.persistence.MovieRating;
import pl.edu.agh.iptv.persistence.Quality;
import pl.edu.agh.iptv.persistence.User;

public class MessageCreator {
	EntityManager em;

	UserTransaction utx;

	public MessageCreator(EntityManager em, UserTransaction utx) {
		this.em = em;
		this.utx = utx;
	}

	@SuppressWarnings("unchecked")
	public Movie getMovieFromTitle(String title) {
		Query query = em
				.createQuery("FROM Movie WHERE title = '" + title + "'");
		List<Movie> movieList = query.getResultList();
		Movie movie = movieList.get(0);
		return movie;
	}

	public SessionDescription createSDPFromMovie(String movieTitle, String sip) {
		SessionDescription sessionDescription = null;
		try {
			utx.begin();
			Movie movie = getMovieFromTitle(movieTitle);

			sessionDescription = SDPFactory.createSessionDescription();
			sessionDescription.setVersion(SDPFactory.createVersion());
			Origin orgin = SDPFactory.createOrigin();
			sessionDescription.setOrigin(orgin);

			if (movie.getTitle() != null && (!movie.getTitle().isEmpty())) {
				Information info = SDPFactory.createInformation(movie
						.getTitle());
				sessionDescription.setInformation(info);
			}

			if (movie.getDescription() != null
					&& (!movie.getDescription().isEmpty())) {
				System.err.println("Description : " + movie.getDescription());
				Attribute description = SDPFactory.createAttribute(
						"description", movie.getDescription());
				sessionDescription.addAttribute(description);
			}

			if (movie.getCategory() != null) {
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

			addingMovieComments(sessionDescription, movie);
			addingMoviePayments(sip, sessionDescription, movie);
			
			utx.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sessionDescription;

	}

	private void addingMoviePayments(String sip,
			SessionDescription sessionDescription, Movie movie)
			throws SDPException {
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
	}

	private void addingMovieComments(SessionDescription sessionDescription,
			Movie movie) throws SDPException {
		for (MovieComment comment : movie.getCommentsList()) {
			String commentString = comment.getUser().getSip() + "|"
					+ comment.getDate().getTime() + "|"
					+ comment.getComment();
			Attribute commentAtr = SDPFactory.createAttribute("comment",
					commentString);
			sessionDescription.addAttribute(commentAtr);
		}
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

	public void addComment(String title, String sip, String comment) {
		try {
			utx.begin();
			Movie movie = getMovieFromTitle(title);
			User user = getUserFromSip(sip);
			movie.addMovieComment(comment, user);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private User getUserFromSip(String sip) {
		Query query = em.createQuery("FROM User WHERE sip = '" + sip + "'");
		List<User> userList = query.getResultList();
		User user = userList.get(0);
		return user;
	}

	public void purchaseMovie(String title, String sip, String quality) {
		try {
			utx.begin();
			User user = getUserFromSip(sip);
			Movie movie = getMovieFromTitle(title);
			user.addOrderedMovie(movie, Quality.valueOf(quality));
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRatingToDatabase(String title, Integer rating, String sip) {
		try {
			utx.begin();
			Movie movie = getMovieFromTitle(title);
			User user = getUserFromSip(sip);
			
			boolean wasModified = false;
			for (MovieRating mRating : movie.getRating()) {
				if (sip.equals(mRating.getUser().getSip())) {
					mRating.setRating(rating);
					wasModified = true;
					break;
				}
			}

			if (!wasModified) {
				movie.addMovieRating(user, rating);
			}

			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public String createMovieList(String sip) {

		StringBuilder stringBuilder = new StringBuilder("");
		try {
			utx.begin();
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
						+ movie.getOverallRating()+"|"+movie.getMediaType().name());
				stringBuilder.append("\n");
			}
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public void createRecordedMovie(String uuid, String movieTitle,
			String sip) {
		String title = "Recorded : " + movieTitle;
		Movie movie = new Movie(title, "C:/Movies/"+ uuid + ".avi");
		movie.setMediaType(MediaType.RECORDING);
		movie.setMovieUrl("rtsp://127.0.0.1:5554/" + uuid);
		movie.setUuid(uuid);
		User user = getUserFromSip(sip);
		movie.setRecordingUser(user);

		try {
			utx.begin();
			em.persist(movie);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
