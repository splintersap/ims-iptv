/* **********************************************************************
 * Copyright (c) Ericsson 2007. All Rights Reserved.
 * Reproduction in whole or in part is prohibited without the 
 * written consent of the copyright owner. 
 * 
 * ERICSSON MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY 
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE, OR NON-INFRINGEMENT. ERICSSON SHALL NOT BE LIABLE FOR ANY 
 * DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR 
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. 
 * 
 * **********************************************************************/
package pl.edu.agh.iptv.view.chat;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ericsson.icp.IBase;
import com.ericsson.icp.ICPFactory;
import com.ericsson.icp.IPlatform;
import com.ericsson.icp.IProfile;
import com.ericsson.icp.IService;
import com.ericsson.icp.ISession;
import com.ericsson.icp.media.Direction;
import com.ericsson.icp.media.IPacketMedia;
import com.ericsson.icp.util.ErrorReason;
import com.ericsson.icp.util.IMediaDescription;
import com.ericsson.icp.util.ISessionDescription;
import com.ericsson.icp.util.ITimeDescription;
import com.ericsson.icp.util.MIMEContainer;
import com.ericsson.icp.util.SdpFactory;

public class Chat {
	/**
	 * Mime-type for the file to send
	 */
	private static final String MIME_TYPE = "image/*";

	/**
	 * Default ICP Profile
	 */
	private static final String ICP_PROFILE = "IMSSetting";	

	/**
	 * Text area containing logging information.
	 */
	private CustomTextArea messageArea;

	/**
	 * Field containing URI of the callee
	 */
	private JTextField uriField;

	/**
	 * Field containing message to send.
	 */
	private JTextField messageField;

	/**
	 * When pressed, invites the callee as defined in the field
	 * <code>uriField</code>
	 */
	private JButton inviteButton;

	/**
	 * Ends the current session when pressed.
	 */
	private JButton hangupButton;

	/**
	 * Send message in the field <code>messageField</code>
	 */
	private JButton sendMessageButton;

	/**
	 * Reference to the icp platform.
	 */
	private IPlatform platform;

	/**
	 * Reference to the icp profile.
	 */
	private IProfile profile;

	/**
	 * Reference to the icp service.
	 */
	private IService service;

	/**
	 * Reference to the icp session.
	 */
	private ISession session;

	/**
	 * Reference to the packet media. It is used to send a file.
	 */
	private IPacketMedia packetMedia;

	/**
	 * Button that allows the user to send the file.
	 */
	private JButton sendFileButton;

	/**
	 * This is the main Panel.
	 */
	JPanel mainPanel;

	JFrame mainFrame;

	public Chat(JFrame mainFrame, IProfile profile, IService service,
			ISession session) {
		this.mainFrame = mainFrame;
		openChat(profile, service, session);
	}

	public void openChat(IProfile profile, IService service, ISession session) {
		// this.profile = profile;
		// this.service = service;
		// this.session = session;
		this.run();
	}

	public static void main(String[] args) {
		// Main chatClient = new Main();
		// chatClient.run();
	}

	public JPanel returnMainPanel() {
		return this.mainPanel;
	}

	/**
	 * Main application loop. Will exit when the variable 'done' is set to
	 * <code>true</code>.
	 */
	private void run() {
		mainPanel = createGui();
		initIcp(mainPanel);
		initializeField();
		mainPanel.setVisible(true);
	}

	/**
	 * Set default values for field in the user interface.
	 */
	private void initializeField() {
		try {
			uriField.setText(profile.getIdentity());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Creates the GUI
	 * 
	 * @return the created gui.
	 */
	private JPanel createGui() {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		inviteButton = new JButton("Invite Chat");
		inviteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				call(panel);

			}
		});
		sendMessageButton = new JButton("Send Msg");
		sendMessageButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}

		});
		hangupButton = new JButton("End Chat");
		hangupButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				hangup();
			}
		});
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				messageArea.clear();
			}
		});

		Panel infoPanel = new Panel();
		infoPanel.setLayout(new GridBagLayout());
		Label uriLabel = new Label("Callee URI:");
		uriField = new JTextField();
		messageField = new JTextField();
		sendFileButton = new JButton("Send File");
		final JTextField fileField = new JTextField();
		fileField.setText("");

		infoPanel.add(uriLabel, createConstraints(0, 0, 0));
		infoPanel.add(uriField, createConstraints(1, 0, 1));
		infoPanel.add(sendMessageButton, createConstraints(0, 1, 0));
		infoPanel.add(messageField, createConstraints(1, 1, 1));
		infoPanel.add(sendFileButton, createConstraints(0, 2, 0));
		sendFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logMessage("preparing to send file", null);
					File afile = new File(fileField.getText());
					if (afile.exists() && afile.canRead()) {
						String extension = "unknown";
						int extensionIndex = afile.getName().indexOf(".");
						if (extensionIndex != -1) {
							extension = afile.getName().substring(
									extensionIndex + 1);
						}

						String contentDisposition = "Content-Disposition:attachment;filename="
								+ afile.getName();
						packetMedia.sendFile("image/" + extension, afile
								.getAbsolutePath(), contentDisposition, 0);
						logMessage("file sending started...", null);
					}
				} catch (Exception ex) {
					logMessage("Could not send file", ex);
				}
			}
		});
		infoPanel.add(fileField, createConstraints(1, 2, 1));

		Panel buttonPanel = new Panel();
		GridLayout layout = new GridLayout(1, 0);
		layout.setHgap(5);
		buttonPanel.setLayout(layout);
		buttonPanel.add(inviteButton);
		buttonPanel.add(hangupButton);

		messageArea = new CustomTextArea("Ready!");
		messageArea.setEditable(false);

		panel.add(infoPanel, createConstraints(0, 0, 0));
		panel.add(buttonPanel, createConstraints(0, 1, 0));
		GridBagConstraints c = createConstraints(0, 2, 1);
		c.weighty = 1;
		panel.add(messageArea, c);
		Panel bottomPanel = new Panel();
		bottomPanel.setLayout(new GridLayout());
		bottomPanel.add(clearButton);
		panel.add(bottomPanel, createConstraints(0, 3, 0));
		panel.setSize(new Dimension(200, 300));

		setTalking(false);
		return panel;
	}

	/**
	 * Calls another user
	 * 
	 * @param panel
	 */
	private void call(JPanel panel) {
		try {
			final ISessionDescription localSdp = createLocalSdp();
			session = service.createSession();
			session.addListener(new ChatSessionAdapter(messageArea) {
				public void processSessionStarted(ISessionDescription aSdpBody) {
					super.processSessionStarted(aSdpBody);
					try {
						log("activating packet media");
						ISessionDescription remoteSdp = (ISessionDescription) aSdpBody
								.duplicate();
						packetMedia.setConfiguration(localSdp
								.getMediaDescription(0), remoteSdp
								.getMediaDescription(0));
						packetMedia.activate();
					} catch (Exception e) {
						log("Could not activate packet media", e);
					}
				}
			});

			initPacketMedia(panel);
			IMediaDescription localMediaDescription = SdpFactory
					.createMediaDescription();
			IMediaDescription remoteMediaDescription = SdpFactory
					.createMediaDescription();
			packetMedia.getConfiguration(localMediaDescription,
					remoteMediaDescription);
			localSdp.addMediaDescription(localMediaDescription);

			session.start(uriField.getText(), localSdp, profile.getIdentity(),
					SdpFactory.createMIMEContainer());
			setTalking(true);
		} catch (Exception e) {
			logMessage("Error calling", e);
		}
	}

	private GridBagConstraints createConstraints(int x, int y, float hWeight) {
		GridBagConstraints contraint = new GridBagConstraints();
		contraint.gridx = x;
		contraint.gridy = y;
		contraint.weighty = 0;
		contraint.weightx = hWeight;
		contraint.fill = GridBagConstraints.BOTH;
		return contraint;
	}

	/**
	 * Sends a message in the chat session
	 */
	private void sendMessage() {
		try {
			String message = messageField.getText();
			byte[] messageBytes = message.getBytes("UTF-8");
			session
					.sendMessage("text/plain", messageBytes,
							messageBytes.length);
			logMessage("You: " + message);
		} catch (Exception e) {
			logMessage("Error sending message", e);
		}
	}

	/**
	 * Initializes the ICP platform and service.
	 * 
	 * @param panel
	 */
	private void initIcp(final JPanel panel) {
		try {
			platform = ICPFactory.createPlatform();
			platform.registerClient("ChatClient");
			platform.addListener(new PlatformAdapter(messageArea));

			profile = platform.createProfile(ICP_PROFILE);
			profile.addListener(new ProfileAdapter(messageArea));

			service = profile.createService("+g.chatservice", "chatapp");
			service.addListener(new ServiceAdapter(messageArea) {
				/**
				 * This callback method will be called when a session comes from
				 * the outside and ICP determines that your application needs to
				 * be invoked. In this case we pop a dialog asking if the user
				 * accepts or refuses the invitation.
				 */
				public void processIncomingSession(ISession aSession) {
					session = aSession;

					try {
						aSession
								.addListener(new ChatSessionAdapter(messageArea) {

									/**
									 * Starts the chat session.
									 */
									public void processSessionInvitation(
											String aFrom,
											boolean aProvisionalRequired,
											ISessionDescription aSdpBody,
											MIMEContainer aContainer) {
										super.processSessionInvitation(aFrom,
												aProvisionalRequired, aSdpBody,
												aContainer);
										try {
											log("Got remote SDP!");
											ISessionDescription remoteSdp = (ISessionDescription) aSdpBody
													.duplicate();
											ISessionDescription localSdp = createLocalSdp();
											initPacketMedia(panel);

											IMediaDescription localDesc = SdpFactory
													.createMediaDescription();
											IMediaDescription remoteDesc = remoteSdp
													.getMediaDescription(0);
											packetMedia
													.getConfiguration(
															localDesc,
															SdpFactory
																	.createMediaDescription());
											packetMedia.setConfiguration(
													localDesc, remoteDesc);
											log("activating packet media");
											packetMedia.activate();
											localSdp
													.addMediaDescription(localDesc);

											ConfirmDialog dialog = new ConfirmDialog(
													mainFrame,
													"Accept incoming chat?");

											if (dialog.hasAccepted()) {
												log("Accept invite");
												session
														.acceptInvitation(localSdp);
												setTalking(true);
											} else {
												log("Reject invite");
												session.rejectInvitation();
												setTalking(false);
											}
										} catch (Exception e) {
											log(
													"Could not establish chat session",
													e);
										}
									}

								});

					} catch (Exception e) {
						log("Error handling chat", e);
					}
				}

			});
		} catch (Exception e) {
			logMessage("Could not init ICP", e);
		}
	}

	/**
	 * Logs a message in the gui.
	 * 
	 * @param description
	 */
	private void logMessage(String description) {
		logMessage(description, null);
	}

	/**
	 * Logs an error to the messageArea.
	 * 
	 * @param description
	 *            message to log
	 * @param e
	 *            exception to log
	 */
	private void logMessage(String description, Exception e) {
		StringOutputStream stringos = new StringOutputStream();
		if (e != null) {
			PrintStream stream = new PrintStream(stringos);
			e.printStackTrace(stream);
		}

		messageArea.append(description + " " + stringos.getValue() + "\r\n");
	}

	/**
	 * Ends the current session. Also updates the GUI
	 */
	private void hangup() {
		try {
			session.end();
		} catch (Exception e) {
			logMessage("Cannot end session", e);
		}
		setTalking(false);
	}

	/**
	 * Releases an ICP object if it is not <code>null</code>.
	 * 
	 * @param base
	 */
	private void release(IBase base) {
		try {
			if (base != null) {
				base.release();
			}
		} catch (Exception e) {
			logMessage("Cannot release", e);
		}
	}

	/**
	 * Updates the state of the buttons in the GUI based on wheter the user is
	 * already in a session or not.
	 * 
	 * @param isInSession
	 */
	private void setTalking(boolean isInSession) {
		inviteButton.setEnabled(!isInSession);
		hangupButton.setEnabled(isInSession);
		sendMessageButton.setEnabled(isInSession);
		sendFileButton.setEnabled(isInSession);
	}

	/**
	 * Releases session-related ICP resources and updates the GUI accordingly.
	 */
	private void clean() {
		try {
			if (packetMedia != null) {
				logMessage("Deactiving media");
				packetMedia.deactivate();
			}
			logMessage("Release media");
			release(packetMedia);
			logMessage("Release session");
			release(session);
			packetMedia = null;
			session = null;
			setTalking(false);
		} catch (Exception e) {
			logMessage("Error cleaning", e);
		}

	}

	/**
	 * Initialize the main session SDP
	 */
	private ISessionDescription createLocalSdp() {
		ISessionDescription localSdp = null;
		try {
			localSdp = SdpFactory.createSessionDescription("");
			localSdp.setField(ISessionDescription.FieldType.ProtocolVersion,
					"0");
			localSdp.setField(ISessionDescription.FieldType.Owner,
					"- 1111551202156 1111551202156 IN IP4");
			localSdp.setField(ISessionDescription.FieldType.SessionName,
					"My Chat Application Session");
			ITimeDescription timeDescription = SdpFactory
					.createTimeDescription();
			timeDescription.setSessionActiveTime("0 0");
			localSdp.addTimeDescription(timeDescription);
		} catch (Exception e) {
			logMessage("Could not create the SDP", e);
		}
		return localSdp;
	}

	/**
	 * Initialize the packet media object
	 * 
	 * @param icpController
	 *            The icpController to interact with
	 */
	private void initPacketMedia(final JPanel panel) {
		try {
			logMessage("creating packet media");
			// Create the packet media and add a listener on it
			packetMedia = session.createPacketMedia();
			packetMedia.addListener(new PacketMediaAdapter(messageArea) {
				private File currentReceivedFile = null;

				private FileOutputStream output = null;

				public void processReceivedData(String contentType,
						byte[] data, int length, boolean isCompleted) {
					super.processReceivedData(contentType, data, length,
							isCompleted);

					if (currentReceivedFile != null) {
						try {
							if (output != null) {
								output.write(data);
								output.flush();
								if (isCompleted) {
									output.close();
								}
							}
						} catch (IOException e) {
							log(e.toString());
						}
					} else {
						String message;
						try {
							message = "Remote: " + new String(data, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							message = "Cannot decode message!";
						}
						log("processReceivedData " + message);
					}
				}

				public void processReceivingStarted(String fileName,
						String contentType) {
					super.processReceivingStarted(fileName, contentType);

					try {
						currentReceivedFile = new File(
								"c:\\Media Files\\document\\unfiled\\"
										+ fileName);
						currentReceivedFile.mkdirs();
						if (currentReceivedFile.exists()) {
							currentReceivedFile.delete();
						}
						currentReceivedFile.createNewFile();

						output = new FileOutputStream(currentReceivedFile);
					} catch (IOException e) {
						log("Could not create new file", e);
					}
				}

				public void processSendingProgress(int percentage) {
					super.processSendingProgress(percentage);

					if (percentage == 100 && (currentReceivedFile != null)) {
						ConfirmDialog dialog = new ConfirmDialog(mainFrame,
								"Save received file "
										+ currentReceivedFile.getName()
										+ " in \n"
										+ currentReceivedFile.getParentFile()
												.getAbsolutePath() + "?");

						if (!dialog.hasAccepted()) {
							currentReceivedFile.delete();
						}

						currentReceivedFile = null;
						output = null;
					}
				}

			});
			packetMedia.setDirection(Direction.SendRecv);
			packetMedia.setSupportedMIME(MIME_TYPE);
		} catch (Exception e) {
			logMessage("Error intializing the packet media", e);
		}
	}

	private class CustomTextArea extends JTextArea {
		/**
         * 
         */
		private static final long serialVersionUID = -6364883255675338017L;

		public CustomTextArea(String text) {
			super(text);
		}

		public void clear() {
			super.setText("");
		}
	}

	private class ChatSessionAdapter extends SessionAdapter {
		protected ChatSessionAdapter(JTextArea area) {
			super(area);
		}

		public void processSessionStartFailed(ErrorReason reason,
				long retryAfter) {
			super.processSessionStartFailed(reason, retryAfter);
			try {
				clean();
			} catch (Exception e) {
				log("Could release not packet media", e);
			}
		}

		public void processSessionEnded() {
			super.processSessionEnded();
			try {
				// use following to reproduce crash on Symbian
				// clean();
				setTalking(false);
			} catch (Exception e) {
				log("Could release packet media", e);
			}
		}

		/**
		 * process incoming message
		 */
		public void processSessionMessage(String aContentType, byte[] aMessage,
				int aLength) {
			String message;
			try {
				message = "Remote: " + new String(aMessage, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				message = "Cannot decode message!";
			}
			log("processSessionMessage " + message);
		}
	}
}
