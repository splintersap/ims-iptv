package pl.edu.agh.iptv;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.iptv.controllers.MoviesController;
import pl.edu.agh.iptv.controllers.helper.MessageHelper;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.data.Movie;
import pl.edu.agh.iptv.data.MovieDescription;
import pl.edu.agh.iptv.listeners.CommentListener;
import pl.edu.agh.iptv.presence.PresenceNotifier;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.movies.DescriptionPanel;
import pl.edu.agh.iptv.view.movies.MovieComments;
import pl.edu.agh.iptv.view.movies.MoviesTab;

import com.ericsson.icp.ICPFactory;
import com.ericsson.icp.IPlatform;
import com.ericsson.icp.IProfile;
import com.ericsson.icp.IService;
import com.ericsson.icp.ISession;
import com.ericsson.icp.util.ErrorReason;
import com.ericsson.icp.util.SdpFactory;

/**
 * This is very important class responsible for communication with the
 * application server. This class establishes session with the server and
 * request server for needed information - movie description, etc.
 * 
 * @author Wozniak
 * 
 */
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

	private MainView mainView = null;

	/*
	 * This is in order to pass the reference of IPTVClient class to the
	 * aggregated class SessionAdapter responsible for querying the server.
	 */
	private IPTVClient client;

	public IPTVClient(MainView mainView) {
		try {
			this.client = this;
			IPlatform platform = ICPFactory.createPlatform();
			platform.registerClient("IPTVClient");
			platform.addListener(new PlatformAdapter());
			profile = platform.createProfile("IMSSetting");
			profile.addListener(new ProfileAdapter());

			service = profile.createService("+g.videoclient.ericsson.com", "");
			service.addListener(new ServiceAdapter());

			this.mainView = mainView;
			this.moviesTab = mainView.getMoviesTab();

			//new PresenceNotifier(profile);

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

				@Override
				public void processSessionInformation(String aContentType,
						byte[] aMessage, int aLength) {

					System.out.println("Tutaj");

					super.processSessionInformation(aContentType, aMessage,
							aLength);

					System.out.println("Mamy wiadomosc");
					System.out.println("ContentType = " + aContentType);
					System.out.println("Message = " + new String(aMessage));

					if ("text/movie-list".equals(aContentType)) {

						String movieString = new String(aMessage);
						String[] movieList = movieString.split("\n");
						moviesTab.setListOfMovies(movieList);
					} else if ("application/sdp".equals(aContentType)) {

						/*
						 * Displaying received description about selected movie.
						 */
						Runnable sh = new DescriptionThread(new MessageHelper()
								.parseMovieSDPMessage(aMessage), moviesTab,
								client);

						EventQueue.invokeLater(sh);
						/***************************************/
					} else if("vlc/uri".equals(aContentType)) {
						String vlcCommand = new String(aMessage);
						System.out.println(vlcCommand);
					} else {
						System.out.println("Unrecognized message");
					}
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


		// if (mainView != null && mainView.getMainFrame() != null)
		// JOptionPane.showMessageDialog(this.mainView.getMainFrame(),
		// "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
		// else
		// JOptionPane.showMessageDialog(null, "Error: " + message, "Error",
		// JOptionPane.ERROR_MESSAGE);


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
			showError("Error wile starting session", e);
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
		System.out.println("I am setting user rating to "
				+ String.valueOf(rating));
		String ratingString = String.valueOf(rating);
		try {
			session.sendInformation("rating/" + title, ratingString.getBytes(),
					ratingString.length());
		} catch (Exception e) {
			showError("Error while sending INFO about user rating", e);
			e.printStackTrace();

		}
	}

	public void getMovieInformations(String item) {
		try {
			session.sendInformation("text/title-info", item.getBytes(), item
					.length());
		} catch (Exception e) {
			showError("Error while sending INFO about movie movie", e);
			e.printStackTrace();
		}
	}

	public void purchaseMovie(String movieTitle, String quality) {
		try {
			session.sendInformation("purchase/" + movieTitle, quality
					.getBytes(), quality.length());
		} catch (Exception e) {
			showError("Error while sending INFO about purchaseing movie", e);
			e.printStackTrace();
		}
	}

	private class DescriptionThread implements Runnable {

		MoviesTab moviesTab;
		Movie movie;
		IPTVClient client;

		public DescriptionThread(Movie movie, MoviesTab moviesTab,
				IPTVClient client) {
			this.movie = movie;
			this.moviesTab = moviesTab;
			this.client = client;
		}

		public void run() {
			DescriptionPanel descriptionPanel = new DescriptionPanel(movie);
			MovieComments movieComments = descriptionPanel.getMovieComments();
			movieComments.getCommentButton().addActionListener(
					new CommentListener(client, movieComments));

			descriptionPanel.getRatingPanel().setIPTVClient(client);

			this.moviesTab.setDescriptionPanel(descriptionPanel);

			boolean isOrdered = false;
			for (MovieDescription movieDesc : movie.getMovieDescriptionList()) {
				if (movieDesc.isOrdered()) {
					isOrdered = true;
					break;
				}
			}
			mainView.setButtonsEnabelment(isOrdered);
		}
	}

	public void setUserComment(String text, String movieTitle) {
		try {
			session.sendInformation("comment/" + movieTitle, text.getBytes(),
					text.length());
		} catch (Exception e) {
			showError("Error while sending INFO with comment", e);
			e.printStackTrace();
		}
	}

	public IProfile getProfile() {
		return this.profile;
	}

	public IService getService() {
		return this.service;
	}

	public ISession getSession() {
		return this.session;
	}

}
