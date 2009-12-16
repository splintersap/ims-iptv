package pl.edu.agh.iptv.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

	private boolean iperfIsAvailable = true;

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
		String[] mimes = contentType.split("/");

		String sip = req.getFrom().getURI().toString();
		String reqContent = new String(req.getRawContent());

		if (mimes.length != 2) {
			log("Wrong MIME type in info message.\n" + req);
			return;
		}

		if ("rating".equals(mimes[0])) {
			updateRating(sip, mimes[1], reqContent);
		} else if ("text/title-info".equals(contentType)) {
			sendSDPInfo(reqContent, req.getSession(), sip);
		} else if ("comment".equals(mimes[0])) {
			updateComment(reqContent, mimes[1], sip);
		} else if ("purchase".equals(mimes[0])) {
			updatePurchase(reqContent, mimes[1], sip);
		} else if ("record".equals(mimes[0])) {
			recordMovie(reqContent, mimes[1], sip);
		} else if ("movies".equals(mimes[0])) {
			sendUrl(req.getSession(), mimes[1], reqContent);
		} else if ("shared".equals(mimes[0])) {
			createSharedMulticast(mimes[1], reqContent);
		} else if ("info/ip_address".equals(contentType)) {
			sendIpAddress(req.getSession());
		} else {
			log("Unrecognized INFO message " + req);
		}
	}

	private synchronized void sendIpAddress(SipSession session)
			throws UnsupportedEncodingException, IOException {
		SipServletRequest info = session.createRequest("INFO");
		if (iperfIsAvailable) {
			info.setContent(this.getIpAddress(), "info/ip_address");
			iperfIsAvailable = false;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(50000);
						iperfIsAvailable = true;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}).start();
		} else {
			info.setContent("WAIT", "info/ip_address");
		}
		info.send();

	}

	private void recordMovie(String content, String movieTitle, String sip)
			throws IOException {
		String[] informationsTab = content.split("\\|");

		if (informationsTab.length != 2) {
			log("Wrong format of recording information ");
			return;
		}

		Long startDateLong = Long.valueOf(informationsTab[0]);
		Date startDate = new Date(startDateLong);
		Long endDateLong = Long.valueOf(informationsTab[1]);
		Date endDate = new Date(endDateLong);

		Movie movie = helper.getMovieFromTitle(movieTitle);
		MoviePayment moviePayment = helper.createRecordedMovie(movieTitle, sip,
				startDate, endDate);

		AbstractTelnetWorker telnet = new RecordingTelnetClient(movie
				.getMoviePath(), startDate, endDate, moviePayment.getUuid());
		AbstractTelnetWorker.doTelnetWork(telnet);

		log("Recording movie " + movieTitle + " from: " + startDate + " to: "
				+ endDate);
	}

	private void createSharedMulticast(String title, String messageContent)
			throws IOException {
		String[] messageInformations = messageContent.split("\n");
		String[] users = messageInformations[0].split("\\|");
		Long dateLong = Long.valueOf(messageInformations[1]);
		Date date = new Date(dateLong);
		Movie movie = helper.getMovieFromTitle(title);

		String multicastAddr = "239.45.12.44";
		MoviePayment moviePayment = helper.createSharedMulticast(title, users,
				date, multicastAddr);
		AbstractTelnetWorker telnet = new SharedMulticastTelnet(movie
				.getMoviePath(), multicastAddr, date, moviePayment.getUuid());
		AbstractTelnetWorker.doTelnetWork(telnet);
		log("Shared multicast " + title);
	}

	private void sendUrl(SipSession session, String title, String quality)
			throws IOException, UnsupportedEncodingException {

		MoviePayment moviePayment = null;
		Movie movie = null;
		try {
			utx.begin();
			movie = helper.getMovieFromTitle(title);
			moviePayment = movie.getMoviePayments(Quality.valueOf(quality));
			utx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		SipServletRequest info = session.createRequest("INFO");
		info.setContent(moviePayment.getMovieUrl(), "vlc/uri");
		info.send();
		log("sending streaming URL of " + movie.getTitle());
	}

	private void updatePurchase(String quality, String title, String sip)
			throws IOException {
		helper.purchaseMovie(title, sip, quality);
		log("Purchase another movie " + title + ", quality = " + quality
				+ ", sip = " + sip);
	}

	private void updateComment(String comment, String title, String sip)
			throws IOException {
		helper.addComment(title, sip, comment);
		log("Adding new comment " + title + ", " + comment + ", " + sip);
	}

	private void sendSDPInfo(String title, SipSession session, String sip)
			throws IOException, UnsupportedEncodingException {

		SessionDescription sessionDescription = helper.createSDPFromMovie(
				title, sip);

		SipServletRequest info = session.createRequest("INFO");
		info.setContent(sessionDescription.toString().getBytes(),
				"application/sdp");
		info.send();

		log("Sending SDP with title = " + title + ", sip = " + sip);
	}

	private void updateRating(String sip, String title, String reqContent)
			throws IOException {
		Integer rating = new Integer(reqContent);
		helper.setRatingToDatabase(title, rating, sip);
		log("Rating updated : title = " + title + ", rating = " + rating
				+ ", sip=" + sip);
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
