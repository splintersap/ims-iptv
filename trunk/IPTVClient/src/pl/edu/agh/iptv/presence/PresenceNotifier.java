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

public class PresenceNotifier {

	private BuddiesController buddiesC;

	/**
	 * A reference to the ICP profile.
	 */
	private IProfile profile;

	private IPresence presence;

	private ITuple tuple;

	private IBuddy buddy;

	/**
	 * Offline
	 */
	final private int OFFLINE_STATUS = 1;

	/**
	 * Online
	 */
	final private int ONLINE_STATUS = 0;

	public PresenceNotifier(IProfile profile, BuddiesController buddiesC) {
		this.profile = profile;
		this.buddiesC = buddiesC;
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
		publish(ONLINE_STATUS);
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
		// enableButton(subscribeButton, false);
		// enableButton(unSubscribeButton, true);
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
	 * Publishes presence information
	 * 
	 * @param int status, 0=online and 1=offline
	 */
	private void publish(int status) {
		try {
			IPresentity presentity = presence.getPresentity();
			if (status == ONLINE_STATUS) {
				tuple = PGMFactory.createTuple();
				tuple.setBasic(status);
				presentity.addTuple(tuple);
			} else if (status == OFFLINE_STATUS) {
				presentity.removeTuple(tuple);
			}
			presence.updatePresentity(presentity);
		} catch (Exception e) {
			System.out.println("Could not publish presence information");
		}
	}

	public IPresence getPresence() {
		return this.presence;
	}

}
