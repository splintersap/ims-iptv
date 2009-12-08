package pl.edu.agh.iptv.controllers.helper;

import java.util.Date;

import net.sourceforge.jsdp.Attribute;
import net.sourceforge.jsdp.SDPFactory;
import net.sourceforge.jsdp.SDPParseException;
import net.sourceforge.jsdp.SessionDescription;
import pl.edu.agh.iptv.data.Comment;
import pl.edu.agh.iptv.data.Movie;
import pl.edu.agh.iptv.data.MovieDescription;

/**
 * This class takes care of extracting data from received SIP messages.
 * 
 * @author Wozniak
 * 
 */
public class MessageHelper {

	/**
	 * This function having servlet message of type application/sdp creates the
	 * instance of class Movie which holds information about selected movie.
	 * 
	 * @param aMessage
	 *            message from the servlet
	 * @return movie which description is get from received message
	 */
	public Movie parseMovieSDPMessage(byte[] aMessage) {

		Movie movie = null;

		String message = new String(aMessage);
		try {
			SessionDescription sdp = SDPFactory
					.parseSessionDescription(message);
			
			String title = sdp.getInformation().getValue();
			String description = getSDPAttribute(sdp, "description");
			String category = getSDPAttribute(sdp, "category");
			String director = getSDPAttribute(sdp, "director");
			String userRating = getSDPAttribute(sdp, "userRating");
			String overallRating = getSDPAttribute(sdp,"overallRating");

			movie = new Movie(title, category, description, director, Integer
					.valueOf(userRating), Double.valueOf(overallRating));
			getMovieComments(movie, sdp);

			getMoviePayments(movie, sdp);
		} catch (SDPParseException e) {
			e.printStackTrace();
		}

		return movie;

	}

	private void getMoviePayments(Movie movie, SessionDescription sdp) {
		Attribute[] paymentAtr = sdp.getAttributes("payment");
		for (Attribute atr : paymentAtr) {
			String payment = atr.getValue();
			System.out.println(payment);
			String[] strings = payment.split("\\|");
			if (strings.length == 3) {

				Date date = null;
				boolean isOrdered = false;
				String quality = strings[0];
				// cmd.setQuality(quality);
				Long price = new Long(strings[1]);
				// cmd.setPrice(price);
				if ("null".equals(strings[2])) {
					isOrdered = false;
				} else {
					date = new Date(new Long(strings[2]));
					isOrdered = true;
				}

				MovieDescription movieDescription = new MovieDescription(
						quality, price, date, isOrdered);
				movie.addCommonMovieDescription(movieDescription);
			}
		}
	}

	private void getMovieComments(Movie movie, SessionDescription sdp) {
		Attribute[] commentAtr = sdp.getAttributes("comment");
		System.out.println("length = " + commentAtr.length);
		for (Attribute atr : commentAtr) {
			String comment = atr.getValue();
			System.out.println(comment);
			String[] strings = comment.split("\\|");

			if (strings.length == 3) {
				String sip = strings[0];

				Date date = new Date(Long.valueOf(strings[1]));

				String com = strings[2];
				Comment commonComment = new Comment(date, com, sip);
				System.out.println(date + ", " + com + ", " + sip);
				movie.addCommonComment(commonComment);

			}
		}
	}
	
	private String getSDPAttribute(SessionDescription sdp , String attribute) {
		Attribute atr = sdp.getAttribute(attribute);
		if(atr == null) {
			return "";
		}
		return atr.getValue();
	}

}
