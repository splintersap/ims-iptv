package pl.edu.agh.iptv;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JOptionPane;

import net.sourceforge.jsdp.Attribute;
import net.sourceforge.jsdp.SDPFactory;
import net.sourceforge.jsdp.SDPParseException;
import net.sourceforge.jsdp.SessionDescription;
import pl.edu.agh.iptv.commons.CommonComment;
import pl.edu.agh.iptv.commons.CommonMovie;
import pl.edu.agh.iptv.commons.CommonMovieDescription;
import pl.edu.agh.iptv.controllers.MoviesController;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.listeners.CommentListener;
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
						System.out.println("SDP information");
						String message = new String(aMessage);
						try {
							SessionDescription sdp = SDPFactory
									.parseSessionDescription(message);
							String description;
							if (sdp.getAttribute("description") != null)
								description = sdp.getAttribute("description")
										.getValue();
							else
								description = "";
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

							Attribute[] paymentAtr = sdp
									.getAttributes("payment");
							for (Attribute atr : paymentAtr) {
								String payment = atr.getValue();
								System.out.println(payment);
								String[] strings = payment.split("\\|");
								if (strings.length == 3) {

									Date date = null;
									boolean isOrdered = false;
									String quality = strings[0];
									// cmd.setQuality(quality);
									Long price = new Long(strings[1]);
									// cmd.setPrice(price);
									if ("null".equals(strings[2])) {
										isOrdered = false;
									} else {
										date = new Date(new Long(strings[2]));
										isOrdered = true;
									}

									CommonMovieDescription movieDescription = new CommonMovieDescription(
											quality, price, date, isOrdered);
									movie
											.addCommonMovieDescription(movieDescription);
								}
							}

							System.out.println(title + ", " + category + ", "
									+ director + ", " + description);
							System.out.println("Before DP");

							/*
							 * Displaying received description about selected
							 * movie.
							 */
							Runnable sh = new DescriptionThread(movie,
									moviesTab, client);

							EventQueue.invokeLater(sh);
							/***************************************/

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
		JOptionPane.showMessageDialog(this.mainView.getMainFrame(), "Error: "
				+ message, "Error", JOptionPane.ERROR_MESSAGE);
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

	public void getMovieInformations(String item) {
		try {
			session.sendInformation("text/title-info", item.getBytes(), item
					.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void purchaseMovie(String movieTitle, String quality) {
		try {
			session.sendInformation("purchase/" + movieTitle, quality
					.getBytes(), quality.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class DescriptionThread implements Runnable {

		MoviesTab moviesTab;
		CommonMovie movie;
		IPTVClient client;

		public DescriptionThread(CommonMovie movie, MoviesTab moviesTab,
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
			for (CommonMovieDescription movieDesc : movie
					.getMovieDescriptionList()) {
				if (movieDesc.isOrdered()) {
					isOrdered = true;
					break;
				}
			}
			mainView.getOrderMoviebButton().setEnabled(!isOrdered);
		}
	}

	public void setUserComment(String text, String movieTitle) {
		try {
			session.sendInformation("comment/" + movieTitle, text.getBytes(),
					text.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
