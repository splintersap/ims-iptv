package pl.edu.agh.ims.video.client;

import com.ericsson.icp.IProfileListener;
import com.ericsson.icp.IProfile.State;
import com.ericsson.icp.util.ErrorReason;

public class ProfileAdapter implements IProfileListener {

	public void processEvent(String arg0, String arg1, ErrorReason arg2) {
		System.out.println("processEvent");

	}

	public void processStateChanged(State arg0) {
		System.out.println("processStateChanged");
	}

	public void processError(ErrorReason arg0) {
		System.out.println("processError");
	}

}
