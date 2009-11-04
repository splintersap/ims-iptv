package pl.edu.agh.iptv;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.ims.commons.Serializator;
import pl.edu.agh.iptv.controllers.MoviesController;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.view.movies.MoviesTab;
import pl.edu.agh.performance.client.IPTVPerformanceClient;

import com.ericsson.icp.ICPFactory;
import com.ericsson.icp.IPlatform;
import com.ericsson.icp.IProfile;
import com.ericsson.icp.IService;
import com.ericsson.icp.ISession;
import com.ericsson.icp.util.ErrorReason;
import com.ericsson.icp.util.SdpFactory;

public class IPTVClient implements ActionListener {

	/**
	 * Reference to ICP service. This is used to manipulate paging-mode
	 * messages.
	 */
	private IService service;

	/**
	 * The current game IMS session.
	 */
	private ISession session;

	private MoviesController moviesController;

	IProfile profile;

	String[] movies;

	private MoviesTab moviesTab = null;

	public IPTVClient(MoviesTab moviesTab) {
		try {

			IPlatform platform = ICPFactory.createPlatform();
			platform.registerClient("IPTVClient");
			platform.addListener(new PlatformAdapter());
			profile = platform.createProfile("IMSSetting");
			profile.addListener(new ProfileAdapter());

			service = profile.createService("+g.videoclient.ericsson.com", "");
			service.addListener(new ServiceAdapter());

			this.moviesTab = moviesTab;

			addingListener();
			triggerMoviesRequest();

		} catch (Exception e) {
			showError("Could not initialize ICP", e);
		}
	}

	/**
	 * This function adds a listener which listens to the actions regarding
	 * response of the servlet.
	 */
	private void addingListener() {

		try {
			session = service.createSession();

			session.addListener(new SessionAdapter() {

				public void processSessionStartFailed(ErrorReason aError,
						long retryAfter) {
					showError("Could not start session", new Exception(aError
							.getReasonString()));
				}

				public void processError(ErrorReason aError) {
					showError("Session Error", new Exception(aError
							.getReasonString()));
				}

				public void processSessionMessage(String aContentType,
						byte[] aMessage, int aLength) {
					// A MESSAGE was received, process it.
					super
							.processSessionMessage(aContentType, aMessage,
									aLength);

					Serializator serializator = new Serializator();
					moviesController = new MoviesController(serializator
							.createListFromXml(new String(aMessage)));

					moviesTab.setListOfMovies(moviesController
							.getTitlesOfBoughtMovies());

				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This function triggers request to the server in order to start streaming
	 * the media - which in this case is the chosen movie whose title is passed
	 * as a function parameter.
	 * 
	 * @param movieTitle
	 *            title of the movie user wants to watch
	 */
	public void showChosenMovie(String movieTitle) {
		try {
			session.sendMessage("movies/choice", movieTitle.getBytes(),
					movieTitle.length());

			/*
			 * This is in order to open the stream.
			 */
			new VLCHelper(this.moviesTab);

		} catch (Exception e) {
			showError("Sending message", e);
		}
	}

	/**
	 * Shows an error dialog when something unexpected happens.
	 * 
	 * @param message
	 * @param e
	 */
	private void showError(String message, Exception e) {
		System.out.println("Error : " + message);
		e.printStackTrace();
	}

	/**
	 * This function triggers request for starting the session.
	 */
	private void triggerMoviesRequest() {
		try {
			session.start("sip:video@ericsson.com", null,
					profile.getIdentity(), SdpFactory.createMIMEContainer());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		addingListener();
		triggerMoviesRequest();
	}

	public MoviesController getMoviesController() {
		return this.moviesController;
	}

	public void setUserRating(int rating, String title) {
		System.out.println("I am setting user rating to " + String.valueOf(rating));
		String ratingString = String.valueOf(rating);
		try {
			session.sendInformation("rating/" + title, ratingString.getBytes(), ratingString.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
