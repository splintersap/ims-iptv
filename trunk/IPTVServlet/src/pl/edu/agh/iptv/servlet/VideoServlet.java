package pl.edu.agh.iptv.servlet;

import java.io.IOException;
import java.util.Iterator;
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

import pl.edu.agh.ims.commons.CommonMovie;
import pl.edu.agh.ims.commons.Serializator;
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

	private final String NUMBER_TO_GUESS = "number";

	static final String vlcLocation = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";

	String sip;

	/**
	 * @inheritDoc
	 */
	protected void doMessage(SipServletRequest sipServletRequest)
			throws ServletException, IOException {
		log("inside doMessage method");

		// start streaming

		sipServletRequest.createResponse(200).send();

		String content = new String(sipServletRequest.getRawContent());
		String moviePath = getMoviePath(content);
		log("Movie path = " + moviePath);
		// String sip = sipServletRequest.getRequestURI().toString();
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
			log("wysylam wiadomosc");
			session.setAttribute(ACK_RECEIVED, true);
			// yields a value between 0 (inclusive) and 11 (exclusive) so [0,
			// 10]
			session.setAttribute(NUMBER_TO_GUESS, 8);

			SipServletRequest message = session.createRequest("MESSAGE");

			try {
				// message.setContent(getMovies().getBytes(), "movies/list");
				message.setContent(getMovies(), "text/movie-list");

			} catch (Exception e) {
				log(e.getMessage());
			}
			message.send();
			log("wiadomosc wyslana");
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

		log("inside doInvite method");

		RandomDatabaseData.fillDatabase(em, utx);

		try {

			log("SIP = " + sipServletRequest.getFrom().getURI());
			String xml = MessageCreator.getMessage(em, utx, sipServletRequest
					.getFrom().getURI().toString());
			log("XML: " + xml);
			List<CommonMovie> newList = Serializator.createListFromXml(xml);
			log("Lista" + newList.toString());
			Iterator<CommonMovie> it = newList.iterator();
			while (it.hasNext()) {
				CommonMovie commonMovie = it.next();
				log(commonMovie.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SipServletResponse response = sipServletRequest.createResponse(200);
		//response.
		response.send();
	}

	@SuppressWarnings("unchecked")
	private String getMovies() {

		StringBuilder moviesStringBuilder = new StringBuilder();

		Query query = em.createQuery("FROM Movie");
		List<Movie> movieList = query.getResultList();
		Iterator<Movie> movieIterator = movieList.iterator();
		while (movieIterator.hasNext()) {
			Movie movie = movieIterator.next();
			moviesStringBuilder.append(movie.getTitle() + "\n");
		}

		log("Filmy: " + moviesStringBuilder.toString());

		return moviesStringBuilder.toString();
	}

	private void runStreamer(String ip, String movieLocation)
			throws IOException {

		String vlcCommandString = "\"" + vlcLocation + "\" \"" + movieLocation
				+ "\" --sout=udp/ts://" + ip + ":1234";

		log(vlcCommandString);

		Runtime runtime = Runtime.getRuntime();

		runtime.exec(vlcCommandString);
	}
}
