package pl.edu.agh.ims.video.client;

import com.ericsson.icp.IPlatformListener;
import com.ericsson.icp.util.ErrorReason;

public class PlatformAdapter implements IPlatformListener {

	public void processApplicationData(String arg0, byte[] arg1, int arg2) {
		System.out.println("processApplicationData");
	}

	public void processIncomingProfile(String arg0) {
		System.out.println("processIncomingProfile");
	}

	public void processPlatformTerminated(ErrorReason arg0) {
		System.out.println("processPlatformTerminated");
	}

	public void processError(ErrorReason arg0) {
		System.out.println("processError");
	}

}
