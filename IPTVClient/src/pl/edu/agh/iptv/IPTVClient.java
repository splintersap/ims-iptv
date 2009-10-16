package pl.edu.agh.iptv;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import pl.edu.agh.iptv.controllers.helper.VLCHelper;
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

	IProfile profile;

	String[] movies = new String[1];

	static final String vlcLocation = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";

	private MoviesTab moviesTab = null;

	public IPTVClient(MoviesTab moviesTab) {
		try {

			IPlatform platform = ICPFactory.createPlatform();
			platform.registerClient("VideoClient");
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
					// A MESSAGE was received, process it
					super
							.processSessionMessage(aContentType, aMessage,
									aLength);
					String message = new String(aMessage);
					movies = message.split("\n");
					moviesTab.setListOfMovies(movies);

				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showChosenMovie(String movieTitle) {
		System.out
				.println("Inside showChosenMovie, movieTitle = " + movieTitle);
		try {
			session.sendMessage("movies/choice", movieTitle.getBytes(),
					movieTitle.length());

			openStream();
		} catch (Exception e) {
			showError("Sending message", e);
		}
	}

	private void openStream() throws IOException {

		
//		 String vlcCommandString = "\"" + vlcLocation +
//		 "\" -I dummy udp://@:1234"; System.out.println(vlcCommandString);
//		  
//		 Runtime runtime = Runtime.getRuntime();
//		  
//		 runtime.exec(vlcCommandString);
		 
		new VLCHelper(this.moviesTab);
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

	public void triggerMoviesRequest() {
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

}
