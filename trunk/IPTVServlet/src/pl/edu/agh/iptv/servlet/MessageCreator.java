package pl.edu.agh.iptv.servlet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
import pl.edu.agh.iptv.persistence.OrderedMovie;
import pl.edu.agh.iptv.persistence.Quality;
import pl.edu.agh.iptv.persistence.Setting;
import pl.edu.agh.iptv.persistence.User;

public class MessageCreator {
	EntityManager em;

	UserTransaction utx;

	private static Format formatter = new SimpleDateFormat("dMMMyy-HH:mm");

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

		long price = 0;

		for (MoviePayment moviePayment : movie.getMoviePayments()) {
			OrderedMovie orderedMovie = moviePayment.getOrderByUser(sip);
			// Date date = moviePayment.getOrderByUser(sip).getDate();

			String dateString = null;
			if (orderedMovie != null) {
				dateString = String.valueOf(orderedMovie.getDate().getTime());
				price = moviePayment.getPrice();
			}
			String moviePaymentString = moviePayment.getQuality() + "|"
					+ (moviePayment.getPrice() - price) + "|" + dateString;
			Attribute paymentAtr = SDPFactory.createAttribute("payment",
					moviePaymentString);
			sessionDescription.addAttribute(paymentAtr);
		}
	}

	private void addingMovieComments(SessionDescription sessionDescription,
			Movie movie) throws SDPException {
		for (MovieComment comment : movie.getCommentsList()) {
			String commentString = comment.getUser().getSip() + "|"
					+ comment.getDate().getTime() + "|" + comment.getComment();
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

	public void purchaseMovie(String title, boolean isMulticast, String sip,
			String qualityString, int divider) {
		try {
			utx.begin();
			User user = getUserFromSip(sip);
			Movie movie = getMovieFromTitle(title);
			long payed = 0;
			for (MoviePayment moviePayment : movie.getMoviePayments()) {
				OrderedMovie orderedMovie = user.getOrderedMovie(moviePayment);
				if (orderedMovie != null && moviePayment.getPrice() > payed) {
					payed = moviePayment.getPrice();
				}
			}

			MoviePayment moviePayment = movie.getMoviePayments(Quality
					.valueOf(qualityString));

			long newCredit = user.getCredit()
					- ((moviePayment.getPrice() - payed) / divider);
			if (newCredit >= 0) {
				Quality quality = Quality.valueOf(qualityString);
				for (Quality movieQualities : Quality.values()) {
					if (movie.getMoviePayments(movieQualities) != null) {
						if (quality.compareTo(movieQualities) >= 0) {
							if (!isMulticast)
								user.addOrderedMovie(movie, movieQualities);
						}
					}

				}
				user.setCredit(newCredit);
			}

			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkIfAllCanPay(String[] multicastUsers,
			String qualityString, Movie movie, int divider) throws Exception {

		utx.begin();
		long payed;
		for (int i = 0; i < multicastUsers.length; i++) {
			payed = 0;
			User user = getUserFromSip(multicastUsers[i]);
			for (MoviePayment moviePayment : movie.getMoviePayments()) {
				OrderedMovie orderedMovie = user.getOrderedMovie(moviePayment);
				if (orderedMovie != null && moviePayment.getPrice() > payed) {
					payed = moviePayment.getPrice();
				}
			}

			MoviePayment moviePayment = movie.getMoviePayments(Quality
					.valueOf(qualityString));

			long newCredit = user.getCredit()
					- ((moviePayment.getPrice() - payed) / divider);

			if (newCredit < 0) {
				utx.commit();
				return false;
			}

		}
		utx.commit();
		return true;
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
					OrderedMovie orderedMovie = moviePayment
							.getOrderByUser(sip);
					if (orderedMovie != null) {
						isOrdered = true;
						break;
					}
				}

				Date now = new Date();

				if((movie.getMediaType().equals(MediaType.VOD)) ||
				   (movie.getMediaType().equals(MediaType.BROADCAST)) ||
				   (movie.getMediaType().equals(MediaType.SHARED) && isOrdered) ||
				   (movie.getMediaType().equals(MediaType.RECORDING) && sip.equals(movie.getRecordingUser().getSip()) && now
								.after(movie.getAvailableFrom()))
				)
				
				{
					if (movie.getAvailableFrom() != null) {
						System.err.println("Now = " + now + ", available = "
								+ movie.getAvailableFrom());
						System.err.println("Test after : "
								+ now.after(movie.getAvailableFrom()));
						if(movie.getRecordingUser() == null) {
							System.err.println("Recording user to null");
						} else {
							System.err.println("Recording user to nie null");
						}
					}
					stringBuilder.append(movie.getTitle() + "|" + isOrdered
							+ "|" + movie.getOverallRating() + "|"
							+ movie.getMediaType().name());
					stringBuilder.append("\n");
				}
			}
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public MoviePayment createRecordedMovie(String movieTitle, String sip,
			Date startDate, Date endDate) {

		long dif = (endDate.getTime() - startDate.getTime()) / (1000*60);
		
		String startFormatedDate = formatter.format(startDate);
		//String endFormatedDate = formatter.format(endDate);
		String title = movieTitle + " " + startFormatedDate + " "
				+ dif + "min";
		Movie movie = new Movie();
		movie.setTitle(title);
		// Movie movie = new Movie(title, "C:/Movies/" + uuid + ".mov");
		movie.setMediaType(MediaType.RECORDING);

		MoviePayment moviePayment = movie.addMoviePayment(0, Quality.HIGH);
		movie.setMoviePath("C:/Movies/" + moviePayment.getUuid() + ".mov");
		moviePayment.setMovieUrl("rtsp://" + getVODIpAddress(em) + ":5554/"
				+ moviePayment.getUuid());
		movie.setAvailableFrom(endDate);

		try {
			utx.begin();
			User user = getUserFromSip(sip);
			movie.setRecordingUser(user);
			em.persist(movie);
			em.persist(moviePayment);
			user.addOrderedMovie(movie, Quality.HIGH);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return moviePayment;

	}

	public MoviePayment createSharedMulticast(String title, String[] users,
			Date date, String multicastAddr) {
		Movie sharedMovie = null;
		MoviePayment moviePayment = null;
		try {
			utx.begin();
			Movie movie = getMovieFromTitle(title);

			String sharedTitle = title + " " + formatter.format(date);

			// create new movie
			sharedMovie = new Movie(sharedTitle, movie.getMoviePath());
			sharedMovie.setMediaType(MediaType.SHARED);
			// sharedMovie.addMoviePayment(400, Quality.LOW);
			// sharedMovie.addMoviePayment(600, Quality.MEDIUM);
			sharedMovie.setAvailableFrom(date);
			moviePayment = sharedMovie.addMoviePayment(0, Quality.HIGH);
			moviePayment.setMovieUrl("rtp://@" + multicastAddr + ":5004");

			em.persist(sharedMovie);
			em.persist(moviePayment);

			// sharedMovie.setUuid(uuid);
			// rtp://@239.255.12.42:5004
			// sharedMovie.setMovieUrl("rtp://@" + multicastAddr + ":5004");

			// let users purchase movie
			for (String userSip : users) {
				User user = getUserFromSip(userSip);
				user.addOrderedMovie(sharedMovie, Quality.HIGH);
			}

			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return moviePayment;
	}

	public static String getVODIpAddress(EntityManager em) {
		String address = null;

		if (em.find(Setting.class, "VLCIP") != null) {
			address = ((Setting) em.find(Setting.class, "VLCIP")).getValue();
		} else {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				address = addr.getHostAddress();
			} catch (UnknownHostException e) {
			}
		}

		return address;
	}
	
	public static String getBroadcastIpAddress(EntityManager em) {
		String address = null;

		if (em.find(Setting.class, Setting.BROADCASTIP) != null) {
			address = ((Setting) em.find(Setting.class, Setting.BROADCASTIP)).getValue();
		} else {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				address = addr.getHostAddress();
			} catch (UnknownHostException e) {
			}
		}

		return address;
	}

	public long getCreditForUser(String sip) {
		User user = getUserFromSip(sip);
		return user.getCredit();
	}

}
