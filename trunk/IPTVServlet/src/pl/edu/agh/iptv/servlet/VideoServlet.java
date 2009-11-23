package pl.edu.agh.iptv.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.management.timer.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.transaction.UserTransaction;

import net.sourceforge.jsdp.SessionDescription;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MovieRating;
import pl.edu.agh.iptv.persistence.User;
import pl.edu.agh.iptv.servlet.facade.MessageCreator;
import pl.edu.agh.iptv.telnet.AbstractTelnetWorker;
import pl.edu.agh.iptv.telnet.RecordingTelnetClient;

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
		String movieTitle = new String(sipServletRequest.getRawContent());
		String moviePath = getMoviePath(movieTitle);

		SipSession session = sipServletRequest.getSession(true);
		SipServletRequest info = session.createRequest("INFO");
		info.setContent(moviePath, "vlc/uri");
		info.send();

		// start streaming
		// runStreamer(sipServletRequest.getInitialRemoteAddr(), moviePath);
	}

	@SuppressWarnings("unchecked")
	private String getMoviePath(String content) {

		Query query = em.createQuery("FROM Movie WHERE title = '" + content
				+ "'");
		List<Movie> movieList = query.getResultList();
		Movie movie = movieList.get(0);
		return movie.getMovieUrl();
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

			log("Updating rating " + user.getSip() + ", rating " + rating);

			boolean wasModified = false;
			for (MovieRating mRating : movie.getRating()) {
				if (sip.equals(mRating.getUser().getSip())) {
					mRating.setRating(rating);
					wasModified = true;
					break;
				}
			}

			if (!wasModified) {
				log("Adding new movie");
				movie.addMovieRating(user, rating);
			}

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
				log("Creating SDP with title = " + title + ", sip = "
						+ req.getFrom().getURI().toString());
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

		} else if (mimeTab.length == 2 && "purchase".equals(mimeTab[0])) {
			String title = mimeTab[1];
			String quality = new String(req.getRawContent());
			String sip = req.getFrom().getURI().toString();
			log("Purchase another movie " + title + ", quality = " + quality
					+ ", sip = " + sip);

			try {
				utx.begin();
				MessageCreator.purchaseMovie(title, sip, quality, em);

				utx.commit();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (mimeTab.length == 2 && "record".equals(mimeTab[0])) {
			String informations = mimeTab[1];
			Date now = new Date();
			Date startDate = new Date(now.getTime() + Timer.ONE_MINUTE);
			Date endDate = new Date(now.getTime() + 3L * Timer.ONE_MINUTE);
			AbstractTelnetWorker telnet = new RecordingTelnetClient(
					"mms://stream.onet.pl/media.wsx?/live/aljazeera", startDate, endDate);
			telnet.start();
			try {
				telnet.join();
			} catch (InterruptedException e) {
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
		SipSession session = sipServletRequest.getSession(true);
		// Ensure we do not process the ACK twice for the dialog
		Object ackAlreadyProcessed = session.getAttribute(ACK_RECEIVED);
		log(sipServletRequest.getInitialRemoteAddr());
		if (ackAlreadyProcessed == null) {
			log("sending movie list");
			session.setAttribute(ACK_RECEIVED, true);
			String sip = sipServletRequest.getFrom().getURI().toString();
			SipServletRequest info = session.createRequest("INFO");

			// SipServletRequest message = session.createRequest("MESSAGE");

			try {
				utx.begin();
				String movieList = MessageCreator.createMovieList(sip, em);
				utx.commit();

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
