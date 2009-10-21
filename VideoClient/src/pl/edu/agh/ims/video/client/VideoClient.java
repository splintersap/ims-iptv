package pl.edu.agh.ims.video.client;

import java.io.IOException;

import com.ericsson.icp.ICPFactory;
import com.ericsson.icp.IPlatform;
import com.ericsson.icp.IProfile;
import com.ericsson.icp.IService;
import com.ericsson.icp.ISession;
import com.ericsson.icp.util.ErrorReason;
import com.ericsson.icp.util.SdpFactory;




public class VideoClient {
	
	 /**
     * Reference to ICP service. This is used to manipulate paging-mode messages.
     */
    private IService service;

    /**
     * The current game IMS session.
     */
    public ISession session;

    IProfile profile;
    
	static final String vlcLocation = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";
	
	public VideoClient() {
		try {
			IPlatform platform = ICPFactory.createPlatform();
			platform.registerClient("GuessClient");
			platform.addListener(new PlatformAdapter());
			profile = platform.createProfile("IMSSetting");
			profile.addListener(new ProfileAdapter());

			service = profile.createService(
					"+g.videoclient.ericsson.com", "");
			service.addListener(new ServiceAdapter());
			
		} catch (Exception e) {
			showError("Could not initialize ICP", e);
		}
	}


	public void getMovieList() {
		System.out.println("Inside getMovieList");
		try {
			session = service.createSession();
			session.addListener(
				new SessionAdapter()
			    {
			        public void processSessionStartFailed(ErrorReason aError, long retryAfter)
			        {
			            showError("Could not start session", new Exception(aError.getReasonString()));
			        }

			        public void processError(ErrorReason aError)
			        {
			            showError("Session Error", new Exception(aError.getReasonString()));
			        }

			        public void processSessionMessage(String aContentType, byte[] aMessage, int aLength)
			        {
			            // A MESSAGE was received, process it
			            super.processSessionMessage(aContentType, aMessage, aLength);
			            String message = new String(aMessage);
			            String[] movies = message.split("\n");
			            
			            VideoClientGui.setMoviesList(movies);

			        }
			    }

			);
			session.start("sip:video@ericsson.com", null, profile.getIdentity(), SdpFactory.createMIMEContainer());
		} catch (Exception e) {
			showError("Session Creation", e);
		}
		
	}
	
	public void showChosenMovie(String movieTitle) {
		System.out.println("Inside showChosenMovie, movieTitle = " + movieTitle);
		try {
			session.sendMessage("movies/choice", movieTitle.getBytes(), movieTitle.length());
			
			openStream();
		} catch (Exception e) {
			showError("Sending message", e);
		}
	}
	
	
	
	private static void openStream() throws IOException {
		
		
		
		String vlcCommandString = "\"" + vlcLocation + "\" -I dummy udp://@:1234";
		System.out.println(vlcCommandString);

		Runtime runtime = Runtime.getRuntime();
		
		
		runtime.exec(vlcCommandString);

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
		/*String exceptionMessage = e.getMessage();
		if (exceptionMessage.length() == 0) {
			exceptionMessage = e.toString();
		}
		
		MessageDialog errorDialog = new MessageDialog(this, "ICP Error",
				message + "\r\n\r\n: " + exceptionMessage);
		errorDialog.setVisible(true);*/
	}

}
