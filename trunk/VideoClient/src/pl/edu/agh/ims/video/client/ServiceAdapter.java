package pl.edu.agh.ims.video.client;

import com.ericsson.icp.IServiceListener;
import com.ericsson.icp.ISession;
import com.ericsson.icp.util.ErrorReason;

public class ServiceAdapter implements IServiceListener {

	public void processIncomingSession(ISession arg0) {
		System.out.println("processIncomingSession");

	}

	public void processMessage(String arg0, String arg1, byte[] arg2, int arg3) {
		System.out.println("processMessage");

	}

	public void processMessage(String arg0, String arg1, byte[] arg2, int arg3,
			String arg4) {
		System.out.println("processMessage2");

	}

	public void processOptions(String arg0, String arg1, String arg2,
			byte[] arg3, int arg4) {
		System.out.println("processOptions");

	}

	public void processPublishResult(boolean arg0, String arg1, String arg2,
			long arg3) {
		System.out.println("processPublishResult");
	}

	public void processRefer(String arg0, String arg1, String arg2,
			String arg3, byte[] arg4, int arg5) {
		System.out.println("processRefer");
	}

	public void processReferEnded(String arg0) {
		System.out.println("processReferEnded");

	}

	public void processReferNotification(String arg0, int arg1) {
		System.out.println("processReferNotification");

	}

	public void processReferNotifyResult(boolean arg0, String arg1, long arg2) {
		System.out.println("processReferNotifyResult");

	}

	public void processReferResult(boolean arg0, String arg1, long arg2) {
		System.out.println("processReferResult");
	}

	public void processSendMessageResult(boolean arg0, long arg1) {
		System.out.println("processSendMessageResult");
	}

	public void processSendOptionsResult(boolean arg0, String arg1,
			String arg2, String arg3, byte[] arg4, int arg5, long arg6) {
		System.out.println("processSendOptionsResult");
	}

	public void processSubscribeEnded(String arg0, String arg1, String arg2) {
		System.out.println("processSubscribeEnded");

	}

	public void processSubscribeNotification(String arg0, String arg1,
			String arg2, String arg3, byte[] arg4, int arg5) {
		System.out.println("processSubscribeNotification");
	}

	public void processSubscribeResult(boolean arg0, String arg1, String arg2,
			long arg3) {
		System.out.println("processSubscribeResult");
	}

	public void processUnsubscribeResult(boolean arg0, String arg1,
			String arg2, long arg3) {
		System.out.println("processUnsubscribeResult");

	}

	public void processError(ErrorReason arg0) {
		System.out.println("processError");
	}

}
