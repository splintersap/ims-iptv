package pl.edu.agh.ims.facade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import pl.edu.agh.ims.commons.FacadeMovie;
import pl.edu.agh.ims.persistence.Movie;
import pl.edu.agh.ims.persistence.MovieRating;

import com.thoughtworks.xstream.XStream;

public class MessageCreator {

	public static String getMessage(EntityManager em, UserTransaction utx,
			String sip) {

		String xml = null;
		try {
			utx.begin();

			List<FacadeMovie> messageCreatorList = new ArrayList<FacadeMovie>();
			Query query = em.createQuery("FROM Movie");
			List<Movie> movieList = query.getResultList();
			Iterator<Movie> iterator = movieList.iterator();
			while (iterator.hasNext()) {
				Movie movie = iterator.next();
				List<MovieRating> list = movie.getRating();
				Iterator<MovieRating> it = list.iterator();
				Double allRating = 0.0;
				Integer userRating = null;
				while (it.hasNext()) {
					MovieRating rating = it.next();
					allRating += rating.getRating();
					if (sip.equals("sip:" + rating.getUser().getSip())) {
						userRating = rating.getRating();
					}
				}

				if (list.size() == 0) {
					allRating = null;
				} else {
					allRating = allRating / list.size();
				}

				FacadeMovie mc = createFacadeMovie(movie, userRating, allRating);
				messageCreatorList.add(mc);
			}
			utx.commit();

			XStream xstream = new XStream();
			xml = xstream.toXML(messageCreatorList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return xml;
	}

	private static FacadeMovie createFacadeMovie(Movie movie,
			Integer userRating, Double allUsersRating) {
		String title = movie.getTitle();
		String category = movie.getCategory().name();
		String description = movie.getDescription();
		String director = movie.getDirector();
		
		return new FacadeMovie(title, category, description, director,
				userRating, allUsersRating);
	}
}
