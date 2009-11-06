package pl.edu.agh.iptv;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import net.sourceforge.jsdp.Attribute;
import net.sourceforge.jsdp.SDPFactory;
import net.sourceforge.jsdp.SDPParseException;
import net.sourceforge.jsdp.SessionDescription;
import pl.edu.agh.iptv.commons.CommonComment;
import pl.edu.agh.iptv.commons.CommonMovie;
import pl.edu.agh.iptv.controllers.MoviesController;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.listeners.DescriptionListener;
import pl.edu.agh.iptv.view.movies.MoviesTab;

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
			System.out.println("Startujemy sesje");
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
						System.out.println("SDP information");
						String message = new String(aMessage);
						try {
							SessionDescription sdp = SDPFactory
									.parseSessionDescription(message);
							String description = sdp
									.getAttribute("description").getValue();
							String title = sdp.getInformation().getValue();
							String category = sdp.getAttribute("category")
									.getValue();
							String director = sdp.getAttribute("director")
									.getValue();
							String userRating = sdp.getAttribute("userRating")
									.getValue();
							String overallRating = sdp.getAttribute(
									"overallRating").getValue();

							CommonMovie movie = new CommonMovie(title,
									category, description, director, Integer
											.valueOf(userRating), Double
											.valueOf(overallRating));
							Attribute[] commentAtr = sdp
									.getAttributes("comment");
							System.out.println("length = " + commentAtr.length);
							for (Attribute atr : commentAtr) {
								String comment = atr.getValue();
								System.out.println(comment);
								String[] strings = comment.split("\\|");

								if (strings.length == 3) {
									String sip = strings[0];

									Date date = new Date(Long
											.valueOf(strings[1]));

									String com = strings[2];
									CommonComment commonComment = new CommonComment(
											date, com, sip);
									System.out.println(date + ", " + com + ", "
											+ sip);
									movie.addCommonComment(commonComment);

								}
							}

							System.out.println(title + ", " + category + ", "
									+ director + ", " + description);
							System.out.println("Before DP");
							/*
							 * DescriptionPanel descriptionPanel = new
							 * DescriptionPanel( "asdf", "asg", "asdgasdg",
							 * "asdgsadg", 3.0);
							 */
							// System.out.println("After DP");
							// descriptionPanel.getRatingPanel().setIPTVClient(this);
							// moviesTab.setDescriptionPanel(null);
							// System.out.println(description);
						} catch (SDPParseException e) {
							e.printStackTrace();
						}

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
		System.out.println("I am setting user rating to "
				+ String.valueOf(rating));
		String ratingString = String.valueOf(rating);
		try {
			session.sendInformation("rating/" + title, ratingString.getBytes(),
					ratingString.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getMovieInformations(String item,
			DescriptionListener descListener) {

		try {
			session.sendInformation("text/title-info", item.getBytes(), item
					.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUserComment(String text, String movieTitle) {
		// TODO Auto-generated method stub

	}

}
