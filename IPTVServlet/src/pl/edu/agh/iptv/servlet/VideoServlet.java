package pl.edu.agh.iptv.servlet;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import net.sourceforge.jsdp.Attribute;
import net.sourceforge.jsdp.Information;
import net.sourceforge.jsdp.Origin;
import net.sourceforge.jsdp.SDPException;
import net.sourceforge.jsdp.SDPFactory;
import net.sourceforge.jsdp.SessionDescription;
import pl.edu.agh.iptv.servlet.facade.MessageCreator;
import pl.edu.agh.iptv.servlet.persistence.Movie;
import pl.edu.agh.iptv.servlet.persistence.User;

@javax.servlet.sip.annotation.SipServlet
public class VideoServlet extends SipServlet {

	@PersistenceContext(unitName = "PU")
	protected EntityManager em;

	@Resource
	private UserTransaction utx;

	private static final long serialVersionUID = -1055741883702121144L;

	private final String ACK_RECEIVED = "ackReceived";

	static final String vlcLocation = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";

	/**
	 * @inheritDoc
	 */
	protected void doMessage(SipServletRequest sipServletRequest)
			throws ServletException, IOException {
		log("inside doMessage method");
		sipServletRequest.createResponse(200).send();
		String content = new String(sipServletRequest.getRawContent());
		String moviePath = getMoviePath(content);
		// start streaming
		runStreamer(sipServletRequest.getInitialRemoteAddr(), moviePath);
	}

	@SuppressWarnings("unchecked")
	private String getMoviePath(String content) {

		Query query = em.createQuery("FROM Movie WHERE title = '" + content
				+ "'");
		List<Movie> movieList = query.getResultList();
		Movie movie = movieList.get(0);
		return movie.getMoviePath();
	}

	private void setRatingToDatabase(String title, Integer rating, String sip) {
		try {
			utx.begin();
			Query query = em.createQuery("FROM Movie WHERE title = '" + title
					+ "'");
			List<Movie> movieList = query.getResultList();
			Movie movie = movieList.get(0);
			log("Movie = " + movie);

			Query userQuery = em.createQuery("FROM User WHERE sip = '" + sip
					+ "'");
			List<User> userList = userQuery.getResultList();
			User user = userList.get(0);
			log("User = " + user);

			log("Updating rating");
			movie.addMovieRating(user, rating);

			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doInfo(SipServletRequest req) throws ServletException,
			IOException {

		req.createResponse(200).send();

		String contentType = req.getContentType();
		String[] mimeTab = contentType.split("/");

		if (mimeTab.length == 2 && "rating".equals(mimeTab[0])) {
			String title = mimeTab[1];
			Integer rating = new Integer(new String(req.getRawContent()));
			setRatingToDatabase(title, rating, req.getFrom().getURI()
					.toString());
			log("Rating updated : title = " + title + ", rating = " + rating
					+ ", sip=" + req.getFrom().getURI().toString());
		} else if (mimeTab.length == 2 && "text/title-info".equals(contentType)) {
			log("Request for description " + mimeTab[1]);
			String title = new String(req.getRawContent());
			log("Title = " + title + ", sip = "
					+ req.getFrom().getURI().toString());
			try {
				utx.begin();
				log("Creating SDP with title = " + title + ", sip = " + req.getFrom().getURI()
								.toString());
				SessionDescription sessionDescription = MessageCreator
						.createSDPFromMovie(title, req.getFrom().getURI()
								.toString(), em);

				SipServletRequest info = req.getSession().createRequest("INFO");
				System.out.println(sessionDescription);
				info.setContent(sessionDescription.toString().getBytes(),
						"application/sdp");
				info.send();
				utx.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (mimeTab.length == 2 && "comment".equals(mimeTab[0])) {
			String title = mimeTab[1];
			String comment = new String(req.getRawContent());
			String sip = req.getFrom().getURI().toString();
			log("Adding new comment " + title + ", " + comment + ", " + sip);
			try {
				utx.begin();
				MessageCreator.addComment(title, sip, comment, em);
				utx.commit();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			

		} else {
			log("Unrecognized INFO message " + req);
		}
	}

	/**
	 * @inheritDoc
	 */
	protected void doAck(SipServletRequest sipServletRequest)
			throws ServletException, IOException {

		log("inside doAck method");
		createSDPFromMovie(null);
		SipSession session = sipServletRequest.getSession(true);
		// Ensure we do not process the ACK twice for the dialog
		Object ackAlreadyProcessed = session.getAttribute(ACK_RECEIVED);
		log(sipServletRequest.getInitialRemoteAddr());
		if (ackAlreadyProcessed == null) {
			log("sending movie list");
			session.setAttribute(ACK_RECEIVED, true);

			SipServletRequest info = session.createRequest("INFO");

			// SipServletRequest message = session.createRequest("MESSAGE");

			try {
				String movieList = createMovieList();

				info.setContent(movieList, "text/movie-list");
				// String xml = MessageCreator.getMessage(em, utx,
				// sipServletRequest.getFrom().getURI().toString());
				// message.setContent(xml, "text/movie-list");

			} catch (Exception e) {
				log(e.getMessage());
			}
			info.send();
		}
	}

	private String createMovieList() {
		StringBuilder stringBuilder = new StringBuilder("");
		Query query = em.createQuery("FROM Movie");
		List<Movie> movieList = query.getResultList();
		for (Movie movie : movieList) {
			stringBuilder.append(movie.getTitle());
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	private SessionDescription createSDPFromMovie(Movie movie) {
		SessionDescription sessionDescription = null;
		try {
			sessionDescription = SDPFactory.createSessionDescription();
		} catch (SDPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			sessionDescription.setVersion(SDPFactory.createVersion());
			Origin orgin = SDPFactory.createOrigin();
			sessionDescription.setOrigin(orgin);

		} catch (SDPException e) {
			e.printStackTrace();
		}

		if (movie != null) {
			try {
				Information info = SDPFactory.createInformation(movie
						.getTitle());
				sessionDescription.setInformation(info);
				Attribute description = SDPFactory.createAttribute(
						"description", movie.getDescription());
				sessionDescription.addAttribute(description);
				Attribute movieCategory = SDPFactory.createAttribute(
						"category", movie.getCategory().name());
				sessionDescription.addAttribute(movieCategory);
				Attribute director = SDPFactory.createAttribute("director",
						movie.getDirector());
				sessionDescription.addAttribute(director);
				Attribute userRating = SDPFactory.createAttribute("userRating",
						String.valueOf(3));
				sessionDescription.addAttribute(userRating);
				Attribute overallRating = SDPFactory.createAttribute(
						"overallRating", String.valueOf(2));
				sessionDescription.addAttribute(overallRating);

			} catch (SDPException e) {

				e.printStackTrace();
			}

		}

		log(sessionDescription.toString());

		return sessionDescription;

	}

	/**
	 * @inheritDoc
	 */
	protected void doBye(SipServletRequest sipServletRequest)
			throws ServletException, IOException {
		log("inside doBye method");
		sipServletRequest.getSession().invalidate();
	}

	/**
	 * @inheritDoc
	 */
	protected void doInvite(SipServletRequest sipServletRequest)
			throws ServletException, IOException {

		log("inside doInvite method - sending response 200 to start session");
		RandomDatabaseData.fillDatabase(em, utx);
		SipServletResponse response = sipServletRequest.createResponse(200);
		response.send();
	}

	private void runStreamer(String ip, String movieLocation)
			throws IOException {

		String vlcCommandString = "\"" + vlcLocation + "\" \"" + movieLocation
				+ "\" --sout=udp/ts://" + ip + ":1234";

		log("Streamer runs with command : " + vlcCommandString);

		Runtime runtime = Runtime.getRuntime();

		runtime.exec(vlcCommandString);
	}
}
