package pl.edu.agh.iptv.presence;

import pl.edu.agh.iptv.presence.controller.BuddiesController;

import com.ericsson.icp.IProfile;
import com.ericsson.icp.services.PGM.IPresence;
import com.ericsson.icp.services.PGM.IPresenceListener;
import com.ericsson.icp.services.PGM.PGMFactory;
import com.ericsson.icp.util.ErrorReason;
import com.ericsson.icp.util.IBuddy;
import com.ericsson.icp.util.IIterator;
import com.ericsson.icp.util.IPresentity;
import com.ericsson.icp.util.ITuple;
import com.ericsson.icp.util.IWatcher;
import com.ericsson.icp.util.ITuple.Basic;

public class PresenceNotifier {

	private BuddiesController buddiesC;

	/**
	 * A reference to the ICP profile.
	 */
	private IProfile profile;

	private IPresence presence;

	private IBuddy buddy;

	private String identity = null;

	public PresenceNotifier(IProfile profile, BuddiesController buddiesC) {
		this.profile = profile;
		this.buddiesC = buddiesC;
		try {
			identity = profile.getIdentity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initializePresence();
	}

	public void initializePresence() {
		try {
			presence = PGMFactory.createPresence(profile);
			presence.addListener(new PresenceClientAdapter());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// publish(ONLINE_STATUS);
		try {
			publishPresence(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Subscribe to a the user defined in the field <code>uriField</code>. The
	 * sip of the field is validated.
	 */
	public void subscribe(String uri) {
		if (uri.compareTo("") == 0 || !uri.startsWith("sip:")) {
			System.out
					.println("please enter a valid uri. Ex: sip:alice@ericsson.com");
			return;
		}
		try {
			buddy = PGMFactory.createBuddy(uri);
			presence.subscribeToPresentity(buddy, 3600);
		} catch (Exception e) {
			System.out.println("Error calling Subscribe");
		}
	}

	/**
	 * The listener handling presence event.
	 * 
	 * @author qmatber
	 */
	private class PresenceClientAdapter implements IPresenceListener {
		protected PresenceClientAdapter() {
		}

		public void processWatcherInfo(String aRemote,
				IIterator aWatcherIterator) {
			System.out.println("process watcher info for " + aRemote);
			try {
				while (aWatcherIterator.hasNext()) {
					aWatcherIterator.next();
					IWatcher watcher = (IWatcher) aWatcherIterator.getElement();
					System.out.println("watcher is " + watcher.getUri());
					System.out.println("status is " + watcher.getStatus());
				}
			} catch (Exception e) {
				System.out.println("Error while processWatcherInfo()");
			}

		}

		public void processSubscribeNotification(String aRemote,
				IPresentity aPresentity) {
			System.out.println("SubscribeNotification is received");
			try {
				IIterator itr = aPresentity.getTupleIterator();
				if (itr.hasNext()) {
					itr.next();
					ITuple tuple = (ITuple) itr.getElement();
					if (tuple.getBasic() == 0) {
						System.out.println(aRemote + " is online");
						buddiesC.changeStatus(aRemote, true);
					} else if (tuple.getBasic() == 1) {
						System.out.println(aRemote + " is offline");
						buddiesC.changeStatus(aRemote, false);
					}
				}
			} catch (Exception e) {
				System.out.println("Error while processing notification");
			}
		}

		public void processUpdatePresentityResult(boolean aSuccess) {
			System.out.println("processUpdatePresentityResult");
		}

		public void processSubscribeResult(boolean aStatus, String aRemote,
				String aEvent) {
			if (!aStatus) {
				System.out
						.println("user is not registered. Please try to subscribe again!");
			}
		}

		public void processUnsubscribeResult(boolean aStatus, String aRemote,
				String aEvent) {
			if (aStatus) {
				System.out.println("Subscription ended");
			}
		}

		public void processError(ErrorReason aError) {
			System.out.println("processError");
		}

		@Override
		public void processAllowWatcherResult(boolean arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void processBlockWatcherResult(boolean arg0) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * Publish the presence of the ICP user
	 */
	public void publishPresence(boolean present) throws Exception {
		// Create our own presentity
		IPresentity presentity = presence.getPresentity();

		ITuple tuple = null;
		IIterator itr = presentity.getTupleIterator();
		boolean hasTuple = itr.hasNext();
		if (hasTuple) {
			itr.next();
			tuple = (ITuple) itr.getElement();
		} else {
			tuple = PGMFactory.createTuple();
			tuple.setContact("High", identity);
		}
		// Update the tuple
		int basic = present ? Basic.Open : Basic.Close;
		tuple.setBasic(basic);

		if (hasTuple) {
			// Add the tuple to the presentity
			presentity.modifyTuple(tuple);
		} else {
			presentity.addTuple(tuple);
		}

		// Update the presentity on the presence server
		presence.updatePresentity(presentity);
	}

	public IPresence getPresence() {
		return this.presence;
	}

}
