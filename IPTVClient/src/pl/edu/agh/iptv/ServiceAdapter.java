package pl.edu.agh.iptv;

import com.ericsson.icp.IServiceListener;
import com.ericsson.icp.ISession;
import com.ericsson.icp.util.ErrorReason;

public class ServiceAdapter implements IServiceListener {

	public void processIncomingSession(ISession arg0) {
		// TODO Auto-generated method stub

	}

	public void processMessage(String arg0, String arg1, byte[] arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	public void processMessage(String arg0, String arg1, byte[] arg2, int arg3,
			String arg4) {
		// TODO Auto-generated method stub

	}

	public void processOptions(String arg0, String arg1, String arg2,
			byte[] arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	public void processPublishResult(boolean arg0, String arg1, String arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	public void processRefer(String arg0, String arg1, String arg2,
			String arg3, byte[] arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public void processReferEnded(String arg0) {
		// TODO Auto-generated method stub

	}

	public void processReferNotification(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void processReferNotifyResult(boolean arg0, String arg1, long arg2) {
		// TODO Auto-generated method stub

	}

	public void processReferResult(boolean arg0, String arg1, long arg2) {
		// TODO Auto-generated method stub

	}

	public void processSendMessageResult(boolean arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	public void processSendOptionsResult(boolean arg0, String arg1,
			String arg2, String arg3, byte[] arg4, int arg5, long arg6) {
		// TODO Auto-generated method stub

	}

	public void processSubscribeEnded(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	public void processSubscribeNotification(String arg0, String arg1,
			String arg2, String arg3, byte[] arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public void processSubscribeResult(boolean arg0, String arg1, String arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	public void processUnsubscribeResult(boolean arg0, String arg1,
			String arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	public void processError(ErrorReason arg0) {
		// TODO Auto-generated method stub

	}

}
