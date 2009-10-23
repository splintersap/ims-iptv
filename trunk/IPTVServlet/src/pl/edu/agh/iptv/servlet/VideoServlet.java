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
import javax.transaction.UserTransaction;

import pl.edu.agh.iptv.servlet.facade.MessageCreator;
import pl.edu.agh.iptv.servlet.persistence.Movie;

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

			SipServletRequest message = session.createRequest("MESSAGE");

			try {
				String xml = MessageCreator.getMessage(em, utx, sipServletRequest
						.getFrom().getURI().toString());
				message.setContent(xml, "text/movie-list");

			} catch (Exception e) {
				log(e.getMessage());
			}
			message.send();
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
