package pl.edu.agh.ims;

import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServletRequest;

@javax.servlet.sip.annotation.SipServlet
public class VideoServlet extends SipServlet {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private final String ACK_RECEIVED = "ackReceived";
	/**
	 * Session attribute in which we store the number to guess
	 */

	private final String NUMBER_TO_GUESS = "number";

	/**
	 * Session is accepted - select the number to guess
	 */

	static String moviesDirectoryLocation = "C:\\Documents and Settings\\ania\\Pulpit\\michal\\filmy";

	static final String vlcLocation = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";

	/**
	 * @inheritDoc
	 */
	protected void doMessage(SipServletRequest sipServletRequest)
			throws ServletException, IOException {
		// TODO: Implement this method
		log("inside doMessage method");
		// start streaming
		
		sipServletRequest.createResponse(200);
		
		String content = new String(sipServletRequest.getRawContent());
		runStreamer(sipServletRequest.getInitialRemoteAddr(), content);
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
				message.setContent(getMovies().getBytes(), "movies/list");
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

		SipServletResponse response = sipServletRequest.createResponse(200);
		response.send();
	}

	private static String getMovies() {

		String moviesString = "";
		File moviesDirectory = new File(moviesDirectoryLocation);

		if (moviesDirectory.isDirectory()) {
			String[] moviesNames = moviesDirectory.list();
			StringBuilder moviesStringBuilder = new StringBuilder();
			for (String movie : moviesNames) {
				moviesStringBuilder.append(movie + "\n");
			}
			moviesString = moviesStringBuilder.toString();
		}

		return moviesString;
	}

	private static void runStreamer(String ip, String movie) throws IOException {
		File movieLocation = new File(moviesDirectoryLocation + "\\" + movie);
		String vlcCommandString = "\"" + vlcLocation + "\" \"" + movieLocation
				+ "\" --sout=udp/ts://" + ip + ":1234";
		System.out.println(vlcCommandString);

		Runtime runtime = Runtime.getRuntime();

		runtime.exec(vlcCommandString);

	}

}
