package pl.edu.agh.iptv.servlet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.transaction.UserTransaction;

import net.sourceforge.jsdp.SessionDescription;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MoviePayment;
import pl.edu.agh.iptv.persistence.Quality;
import pl.edu.agh.iptv.servlet.facade.MessageCreator;
import pl.edu.agh.iptv.servlet.performance.PerformanceLauncher;
import pl.edu.agh.iptv.telnet.AbstractTelnetWorker;
import pl.edu.agh.iptv.telnet.RecordingTelnetClient;
import pl.edu.agh.iptv.telnet.SharedMulticastTelnet;

public class VideoServlet extends SipServlet {

	@PersistenceContext(unitName = "PU")
	protected EntityManager em;

	@Resource
	private UserTransaction utx;

	private static final long serialVersionUID = -1055741883702121144L;

	private final String ACK_RECEIVED = "ackReceived";

	MessageCreator helper;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		new Thread(new PerformanceLauncher(this)).start();
		helper = new MessageCreator(em, utx);
		RandomDatabaseData.fillDatabase(em, utx);
		log("writing data to telnet");
		RandomDatabaseData.addDBMoviesToTelnet(em, utx);
	}

	/**
	 * @inheritDoc
	 */
	protected void doMessage(SipServletRequest sipServletRequest)
			throws ServletException, IOException {
		log("WARNING : Message " + sipServletRequest);
	}

	@Override
	protected void doInfo(SipServletRequest req) throws ServletException,
			IOException {
		req.createResponse(200).send();

		String contentType = req.getContentType();
		String[] mimeTab = contentType.split("/");

		if (mimeTab.length != 2) {
			log("Wrong MIME type in info message.\n" + req);
			return;
		}

		if ("rating".equals(mimeTab[0])) {
			String title = mimeTab[1];
			Integer rating = new Integer(new String(req.getRawContent()));
			helper.setRatingToDatabase(title, rating, req.getFrom().getURI()
					.toString());
			log("Rating updated : title = " + title + ", rating = " + rating
					+ ", sip=" + req.getFrom().getURI().toString());
		} else if ("text/title-info".equals(contentType)) {
			String title = new String(req.getRawContent());

			SessionDescription sessionDescription = helper.createSDPFromMovie(
					title, req.getFrom().getURI().toString());

			SipServletRequest info = req.getSession().createRequest("INFO");
			info.setContent(sessionDescription.toString().getBytes(),
					"application/sdp");
			info.send();

			log("Sending SDP with title = " + title + ", sip = "
					+ req.getFrom().getURI().toString());
		} else if ("comment".equals(mimeTab[0])) {
			String title = mimeTab[1];
			String comment = new String(req.getRawContent());
			String sip = req.getFrom().getURI().toString();
			helper.addComment(title, sip, comment);
			log("Adding new comment " + title + ", " + comment + ", " + sip);

		} else if ("purchase".equals(mimeTab[0])) {
			String title = mimeTab[1];
			String quality = new String(req.getRawContent());
			String sip = req.getFrom().getURI().toString();
			helper.purchaseMovie(title, sip, quality);
			log("Purchase another movie " + title + ", quality = " + quality
					+ ", sip = " + sip);

		} else if ("record".equals(mimeTab[0])) {
			//TODO recording with MoviePayments
			/*String content = new String(req.getRawContent());
			String[] informationsTab = content.split("\\|");
			if (informationsTab.length == 2) {
				Long startDateLong = Long.valueOf(informationsTab[0]);
				Date startDate = new Date(startDateLong);
				Long endDateLong = Long.valueOf(informationsTab[1]);
				Date endDate = new Date(endDateLong);
				String movieTitle = mimeTab[1];
				Movie movie = helper.getMovieFromTitle(movieTitle);
				AbstractTelnetWorker telnet = new RecordingTelnetClient(movie
						.getMoviePath(), startDate, endDate, movie.getUuid());
				telnet.start();
				helper.createRecordedMovie(telnet.getUuid(), movieTitle, req
						.getFrom().getURI().toString(), startDate, endDate);
				try {
					telnet.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				log("Recording movie " + movieTitle + " from: " + startDate
						+ " to: " + endDate);
			
			}*/
		} else if ("movies".equals(mimeTab[0])) {
			//TODO chceck if movie is bought by user
			String title = mimeTab[1];
			String quality = new String(req.getRawContent());
			
			MoviePayment moviePayment = null;
			Movie movie = null;
			try {
			utx.begin();
			movie = helper.getMovieFromTitle(title);
			moviePayment = movie.getMoviePayments(Quality.valueOf(quality));
			utx.commit();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			SipSession session = req.getSession(true);
			SipServletRequest info = session.createRequest("INFO");
			info.setContent(moviePayment.getMovieUrl(), "vlc/uri");
			info.send();
			log("sending streaming URL of " + movie.getTitle());
		} else if ("shared".equals(mimeTab[0])) {
			//TODO shared with MoviePayments
			/*String title = mimeTab[1];
			String messageContent = new String(req.getRawContent());
			String[] messageInformations = messageContent.split("\n");
			String[] users = messageInformations[0].split("\\|");
			Long dateLong = Long.valueOf(messageInformations[1]);
			Date date = new Date(dateLong);
			Movie movie = helper.getMovieFromTitle(title);
			String multicastAddr = "239.45.12.44";
			helper.createSharedMulticast(title, users, date, multicastAddr);
			AbstractTelnetWorker telnet = new SharedMulticastTelnet(movie
					.getMoviePath(), multicastAddr, date, movie.getUuid());
			telnet.start();
			try {
				telnet.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log("Shared multicast " + title);*/
		} else if ("info/ip_address".equals(contentType)) {
			SipSession session = req.getSession();
			SipServletRequest info = session.createRequest("INFO");
			info.setContent(this.getIpAddress(), "info/ip_address");
			info.send();
			log("sending ip address ##############################");
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
		if (ackAlreadyProcessed == null) {
			session.setAttribute(ACK_RECEIVED, true);
			String sip = sipServletRequest.getFrom().getURI().toString();
			SipServletRequest info = session.createRequest("INFO");
			String movieList = helper.createMovieList(sip);
			info.setContent(movieList, "text/movie-list");
			info.send();
			log("Movie list with " + movieList.replaceAll("\n", ", ")
					+ " movies");
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

		SipServletResponse response = sipServletRequest.createResponse(200);
		response.send();
	}

	private String getIpAddress() {
		String address = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			address = addr.getHostAddress();
		} catch (UnknownHostException e) {
		}
		return address;
	}

}
