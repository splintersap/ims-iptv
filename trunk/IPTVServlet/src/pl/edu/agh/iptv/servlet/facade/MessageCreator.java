package pl.edu.agh.iptv.servlet.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import pl.edu.agh.ims.commons.CommonComment;
import pl.edu.agh.ims.commons.CommonMovie;
import pl.edu.agh.ims.commons.CommonMovieDescription;
import pl.edu.agh.ims.commons.Serializator;
import pl.edu.agh.iptv.servlet.persistence.Movie;
import pl.edu.agh.iptv.servlet.persistence.MovieComment;
import pl.edu.agh.iptv.servlet.persistence.MoviePayment;
import pl.edu.agh.iptv.servlet.persistence.MovieRating;


public class MessageCreator {

	@SuppressWarnings("unchecked")
	public static String getMessage(EntityManager em, UserTransaction utx,
			String sip) {

		String xml = null;
		try {
			utx.begin();

			List<CommonMovie> messageCreatorList = new ArrayList<CommonMovie>();
			Query query = em.createQuery("FROM Movie");
			List<Movie> movieList = query.getResultList();
			Iterator<Movie> moviesIterator = movieList.iterator();
			while (moviesIterator.hasNext()) {
				
				Movie movie = moviesIterator.next();
				CommonMovie commonMovie = createCommonMovieWithRatings(sip, movie);
				messageCreatorList.add(commonMovie); 
			}
			utx.commit();

			xml = Serializator.createXmlFromList(messageCreatorList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xml;
	}


	private static CommonMovie createCommonMovieWithRatings(String sip, Movie movie) {
		List<MovieRating> list = movie.getRating();
		Iterator<MovieRating> it = list.iterator();
		Double allRating = 0.0;
		Integer userRating = null;
		while (it.hasNext()) {
			MovieRating rating = it.next();
			allRating += rating.getRating();
			if (sip.equals(rating.getUser().getSip())) {
				userRating = rating.getRating();
			}
		}

		if (list.size() == 0) {
			allRating = 0.0;
		} else {
			allRating = allRating / list.size();
		}

		CommonMovie mc = createCommonMovie(movie, userRating, allRating, sip);
		return mc;
	}

	private static CommonMovie createCommonMovie(Movie movie,
			Integer userRating, Double allUsersRating, String sip) {
		
		String title = movie.getTitle();
		String category = movie.getCategory().name();
		String description = movie.getDescription();
		String director = movie.getDirector();
		
		CommonMovie commonMovie = new CommonMovie(title, category, description, director,
				userRating, allUsersRating);
		
		Iterator<MovieComment> movieCommentIterator = movie.getCommentsList().iterator();
		while(movieCommentIterator.hasNext()) {
			MovieComment movieComment = movieCommentIterator.next();
			CommonComment commonComment = createCommonComment(movieComment);
			commonMovie.addCommonComment(commonComment);
		}
	
		Iterator<MoviePayment> moviePaymentsIterator = movie.getMoviePayments().iterator();
		while(moviePaymentsIterator.hasNext()) {
			MoviePayment moviePayments = moviePaymentsIterator.next();
			CommonMovieDescription commonMovieDescription = createCommonMovieDescription(moviePayments, sip);
			commonMovie.addCommonMovieDescription(commonMovieDescription);
		}

		
		return commonMovie;
	}

	private static CommonMovieDescription createCommonMovieDescription(
			MoviePayment moviePayments, String sip) {
		Date date  = moviePayments.getOrderByUser(sip);
		CommonMovieDescription cd = new CommonMovieDescription(moviePayments.getQuality().name(), moviePayments.getPirce(), null, false );
		if(date != null) {
			cd.setData(date);
			cd.setOrdered(true);
		}
		return cd;
	}


	private static CommonComment createCommonComment(MovieComment movieComment) {
		
		return new CommonComment(movieComment.getDate(), movieComment.getComment(), movieComment.getUser().getSip());
	}
}
