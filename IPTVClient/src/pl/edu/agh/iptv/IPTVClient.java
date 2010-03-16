package pl.edu.agh.iptv;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import pl.edu.agh.iptv.controllers.MoviesController;
import pl.edu.agh.iptv.controllers.helper.MessageHelper;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.data.Movie;
import pl.edu.agh.iptv.data.MovieDescription;
import pl.edu.agh.iptv.listeners.CommentListener;
import pl.edu.agh.iptv.performance.client.PerformanceLauncher;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.components.MenuListItem;
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
	private static ISession session;

	private MoviesController moviesController;

	private static IProfile profile;

	String[] movies;

	private MoviesTab moviesTab = null;

	private MainView mainView = null;

	private boolean iperfStarted = false;

	private static String infoType = null;

	private static String infoContent = null;

	static boolean isInfoToSend = false;

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
			showError("Could not initialize ICP.", e);
		}
	}

	public void restartSession() {
		addingListener();
		triggerMoviesRequest();
	}

	/**
	 * This function adds a listener which listens to the actions regarding
	 * response of the servlet.
	 */
	private void addingListener() {

		try {
			session = service.createSession();
			session.addListener(new SessionAdapter() {

				@Override
				public void processSessionStartFailed(ErrorReason aError,
						long retryAfter) {
					showError("Could not start session", new Exception(aError
							.getReasonString()));
					// IPTVClient.isInfoToSend = true;

				}

				@Override
				public void processSessionStarted(
						com.ericsson.icp.util.ISessionDescription arg0) {
					System.out.println("Session started");

				};

				@Override
				public void processError(ErrorReason aError) {
					showError("Session Error", new Exception(aError
							.getReasonString()));
				}
				
				@Override
				public void processSessionEnded() {
			
					System.out.println("processSessionEnded");
					IPTVClient.this.actionPerformed(null);
				};
				

				@Override
				public void processSessionInformationFailed(ErrorReason arg0,
						long arg1) {
					System.out.println("processSessionInformationFailed");
					IPTVClient.this.actionPerformed(null);
					IPTVClient.isInfoToSend = true;
				}

				@Override
				public void processSessionInformation(String aContentType,
						byte[] aMessage, int aLength) {

					super.processSessionInformation(aContentType, aMessage,
							aLength);

					System.out.println("Mamy wiadomosc");
					System.out.println("ContentType = " + aContentType);
					System.out.println("Message = " + new String(aMessage));

					if ("text/movie-list".equals(aContentType)) {

						String movieString = new String(aMessage);
						final String[] movieList = movieString.split("\n");

						EventQueue.invokeLater(new Runnable() {

							@Override
							public void run() {
								moviesTab.setListOfMovies(movieList);
							}

						});

						if (IPTVClient.isInfoToSend) {
							System.out.println("Sending INFO : "
									+ IPTVClient.infoType + ", "
									+ IPTVClient.infoContent);
							IPTVClient.this
									.sendMovieInformation(IPTVClient.infoType,
											IPTVClient.infoContent);
							isInfoToSend = false;
						}

						getAccountBalance();

						if (!iperfStarted) {
							iperfStarted = true;
							askForIP();
						}

					} else if ("application/sdp".equals(aContentType)) {

						/*
						 * Displaying received description about selected movie.
						 */
						System.out.println("We got movie informations");
						Runnable sh = new DescriptionThread(new MessageHelper()
								.parseMovieSDPMessage(aMessage), moviesTab,
								client);

						EventQueue.invokeLater(sh);
						/***************************************/

					} else if ("vlc/uri".equals(aContentType)) {
						String vlcCommand = new String(aMessage);
						System.out.println(vlcCommand);
						/*
						 * This is in order to open the stream.
						 */
						new VLCHelper(mainView, vlcCommand, IPTVClient.this);

					} else if ("info/ip_address".equals(aContentType)) {
						if (new String(aMessage).contains("WAIT")) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										Thread.sleep(30000);
										askForIP();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

							});
						} else {
							new Thread(new PerformanceLauncher(IPTVClient.this,
									mainView, new String(aMessage))).start();
						}
					} else if ("application/credit".equals(aContentType)) {
						mainView.getMoneyLabel().setText(
								"Your credit - "
										+ new Double(new String(aMessage))
										/ 100.0 + "zl");
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

	public void showChosenMovie(String movieTitle, String quality) {
		sendMovieInformation("movies/" + movieTitle, quality);
		/*
		 * try { session.sendInformation("movies/choice", movieTitle.getBytes(),
		 * movieTitle.length());
		 * 
		 * } catch (Exception e) { showError("Sending message", e); }
		 */
	}

	/**
	 * Shows an error dialog when something unexpected happens.
	 * 
	 * @param message
	 * @param e
	 */
	private void showError(String message, Exception e) {

		final String msg = message;

		if (mainView != null)

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					JOptionPane
							.showMessageDialog(mainView.getMainFrame(),
									"Error: " + msg, "Error",
									JOptionPane.ERROR_MESSAGE);
				}

			});

		else

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					JOptionPane.showMessageDialog(null, "Error: " + msg,
							"Error", JOptionPane.ERROR_MESSAGE);
				}

			});

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
			showError("Error wile starting session.", e);
			e.printStackTrace();
		}
	}

	private void getAccountBalance() {
		try {
			String text = "Request for account balance";
			session.sendInformation("info/credit", text.getBytes(), text
					.length());
		} catch (Exception e) {
			showError("Error while sending INFO with comment.", e);
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		mainView.setButtonsEnabelment(false, false, false);
		addingListener();
		triggerMoviesRequest();
	}

	public MoviesController getMoviesController() {
		return this.moviesController;
	}

	public void setUserRating(int rating, String title) {

		String ratingString = String.valueOf(rating);
		sendMovieInformation("rating/" + title, ratingString);

	}

	public void getMovieInformations(String item) {
		sendMovieInformation("text/title-info", item);
	}

	public void purchaseMovie(String movieTitle, String quality) {
		sendMovieInformation("purchase/" + movieTitle, quality);
	}

	public void sendMovieInformation(String type, String content) {
		try {
			infoContent = content;
			infoType = type;
			session.sendInformation(type, content.getBytes(), content.length());
		} catch (Exception e) {
			showError("Error while sending INFO", e);
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
			if (VLCHelper.mp != null) {
				VLCHelper.mp.stop();
				VLCHelper.isPlayingMovie = false;
				mainView.getPlayButton().setIcon(MainView.playIcon);
			}
			DescriptionPanel descriptionPanel = new DescriptionPanel(movie);
			MovieComments movieComments = descriptionPanel.getMovieComments();
			movieComments.getCommentButton().addActionListener(
					new CommentListener(client, movieComments));

			descriptionPanel.getRatingPanel().setIPTVClient(client);

			this.moviesTab.setDescriptionPanel(descriptionPanel);

			if (this.moviesTab.getAllMoviesList().getSelectedIndex() == -1) {
				int index = this.moviesTab.findTitleIndex(movie.getTitle());
				if (index != -1) {
					this.moviesTab.getAllMoviesList().setSelectedIndex(index);
				}
				
				MoviesTab.sendMessage = false;
			}

			List<MovieDescription> descList = movie.getMovieDescriptionList();

			if (mainView.getMoviesTab().getAllMoviesList() == null) {
				return;
			}

			if (mainView.getMoviesTab().getAllMoviesList().getSelectedValue() == null) {
				return;
			}

			boolean isMulticastAndOrdered = ((MenuListItem) mainView
					.getMoviesTab().getAllMoviesList().getSelectedValue())
					.getCategory() == MenuListItem.MULTICAST;

			if (descList.size() == 0) {
				mainView.setButtonsEnabelment(false, false, false);
			} else if (descList.get(descList.size() - 1).isOrdered()) {
				mainView.setButtonsEnabelment(true, false,
						isMulticastAndOrdered);
			} else {

				for (MovieDescription movieDesc : descList) {
					if (movieDesc.isOrdered()) {
						mainView.setButtonsEnabelment(true, true,
								isMulticastAndOrdered);
						return;
					}
				}
				mainView.setButtonsEnabelment(false, true, false);
			}
		}
	}

	public void setUserComment(String text, String movieTitle) {
		try {
			session.sendInformation("comment/" + movieTitle, text.getBytes(),
					text.length());
		} catch (Exception e) {
			showError("Error while sending INFO with comment.", e);
			e.printStackTrace();
		}
	}

	public void setCommonWatching(String text, String movieTitle) {
		try {
			session.sendInformation("shared/" + movieTitle, text.getBytes(),
					text.length());

		} catch (Exception e) {
			showError("Error while sending request for common movie watching.",
					e);
			e.printStackTrace();
		}
	}

	public void askForIP() {
		try {
			String text = "Request for server ip address";
			session.sendInformation("info/ip_address", text.getBytes(), text
					.length());

		} catch (Exception e) {
			showError("Error while sending request for server ip address.", e);
			e.printStackTrace();
		}
	}

	public void closeMulticast(String title, String quality) {
		try {

			session.sendInformation("leave/" + title, quality.getBytes(),
					quality.length());

		} catch (Exception e) {
			showError("Error while sending request for server ip address.", e);
			e.printStackTrace();
		}
	}

	public void recordMovie(Date startDate, Date endDate, String movieTitle) {
		String text = startDate.getTime() + "|" + endDate.getTime();
		try {
			session.sendInformation("record/" + movieTitle, text.getBytes(),
					text.length());
		} catch (Exception e) {
			showError("Error while sending INFO with comment.", e);
			e.printStackTrace();
		}
	}

	public static IProfile getProfile() {
		return profile;
	}

	public IService getService() {
		return this.service;
	}

	public static ISession getSession() {
		return session;
	}

	public static String getInfoType() {
		return infoType;
	}

	public static String getInfoContent() {
		return infoContent;
	}

}
