package pl.edu.agh.iptv.dbmenager.movietab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pl.edu.agh.iptv.dbmenager.main.Application;
import pl.edu.agh.iptv.persistence.Category;
import pl.edu.agh.iptv.persistence.MediaType;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MoviePayment;
import pl.edu.agh.iptv.persistence.Quality;
import pl.edu.agh.iptv.persistence.Setting;
import pl.edu.agh.iptv.telnet.AbstractTelnetWorker;
import pl.edu.agh.iptv.telnet.MulticastTelnetClient;
import pl.edu.agh.iptv.telnet.VodTelnetClient;

public class AddMovieDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -4290000270971157783L;
	final JTextField titleTextFiled;
	final JTextField directorTextFiled;
	final JComboBox categoryComboBox;
	final JComboBox streamingComboBox;
	final JTextField moviePathTextField;
	final JTextArea descriptionTextArea;
	final JButton cancelButton;
	final JButton saveButton;
	private int RTSP_PORT = 5554;

	public final String VOD = "Vod";
	public final String MULTICAST = "Multicast";

	QualitySpinner[] spinners;

	public AddMovieDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		JPanel informationPanel = new JPanel();
		informationPanel.setLayout(new GridLayout(5, 2, 10, 6));
		JLabel titleLabel = new JLabel("     Title");
		titleTextFiled = new JTextField(15);
		informationPanel.add(titleLabel);
		informationPanel.add(titleTextFiled);

		JLabel directorLabel = new JLabel("     Director");
		directorTextFiled = new JTextField(15);
		informationPanel.add(directorLabel);
		informationPanel.add(directorTextFiled);

		JLabel categoryLabel = new JLabel("     Category");
		categoryComboBox = new JComboBox(Category.values());
		informationPanel.add(categoryLabel);
		informationPanel.add(categoryComboBox);

		JPanel moviePathPanel = new JPanel();
		moviePathPanel.setLayout(new BoxLayout(moviePathPanel,
				BoxLayout.LINE_AXIS));
		JLabel moviePathLabel = new JLabel("     URL");
		final JButton browseButton = new JButton("Browse");
		final JFileChooser fc = new JFileChooser();

		JLabel streamingLabel = new JLabel("     Media type");
		streamingComboBox = new JComboBox(new String[] { VOD, MULTICAST });
		streamingComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String mediaType = (String) cb.getSelectedItem();
				if (mediaType.equals(MULTICAST)) {
					for (QualitySpinner spinner : AddMovieDialog.this.spinners) {
						if (!(spinner.getQuality().equals(Quality.HIGH))) {
							spinner.setEnabled(false);
						}
					}
				} else {
					for (QualitySpinner spinner : AddMovieDialog.this.spinners) {
						spinner.setEnabled(true);
					}
				}

			}
		});

		informationPanel.add(streamingLabel);
		informationPanel.add(streamingComboBox);

		browseButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(browseButton);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					moviePathTextField.setText(file.getAbsolutePath());
					System.out.println("Opening: " + file.getName());
				} else {
					System.out.println("Open command cancelled by user.");
				}
			}
		});
		moviePathTextField = new JTextField(20);
		informationPanel.add(moviePathLabel);
		moviePathPanel.add(moviePathTextField);
		moviePathPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		moviePathPanel.add(browseButton);
		informationPanel.add(moviePathPanel);

		add(informationPanel);

		add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(new BorderLayout());
		JLabel descriptionLabel = new JLabel("     Description");
		descriptionTextArea = new JTextArea(5, 20);
		descriptionTextArea.setLineWrap(true);
		descriptionPanel.add(descriptionLabel, BorderLayout.PAGE_START);
		descriptionPanel.add(descriptionTextArea, BorderLayout.CENTER);

		add(descriptionPanel);
		add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel pricesPanel = new JPanel();
		pricesPanel.setLayout(new GridLayout(Quality.values().length, 2));

		spinners = new QualitySpinner[Quality.values().length];

		int i = 0;
		for (Quality quality : Quality.values()) {

			spinners[i] = new QualitySpinner();
			spinners[i].setQuality(quality);
			JLabel qualityLabel = new JLabel("     " + quality.name());
			pricesPanel.add(qualityLabel);
			pricesPanel.add(spinners[i]);
			i++;
		}

		add(pricesPanel);
		add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel buttonPanel = new JPanel();
		saveButton = new JButton("Save");
		cancelButton = new JButton("Cancel");

		cancelButton.addActionListener(this);
		saveButton.addActionListener(this);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		add(buttonPanel);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(cancelButton)) {

		} else if (arg0.getSource().equals(saveButton)) {
			Movie movie = new Movie(titleTextFiled.getText(),
					moviePathTextField.getText());
			movie.setCategory((Category) categoryComboBox.getSelectedItem());
			movie.setDirector(directorTextFiled.getText());
			movie.setDescription(descriptionTextArea.getText());

			String streaming = (String) streamingComboBox.getSelectedItem();

			AbstractTelnetWorker telnet = null;

			if (VOD.equals(streaming)) {
				movie.setMediaType(MediaType.VOD);

				for (QualitySpinner spinner : spinners) {
					movie.addMoviePayment(Double.valueOf(
							spinner.getValue().toString()).longValue(), spinner
							.getQuality());
				}

				String address = ((Setting) Application.getEntityMenager()
						.find(Setting.class, Setting.VLCIP)).getValue();
				// movie.setMovieUrl("rtsp://" + address + ":" + RTSP_PORT + "/"
				// + telnet.getUuid().toString());
				for (MoviePayment moviePayment : movie.getMoviePayments()) {
					moviePayment.setMovieUrl("rtsp://" + address + ":"
							+ RTSP_PORT + "/" + moviePayment.getUuid());
					telnet = new VodTelnetClient(moviePathTextField.getText(),
							moviePayment.getUuid(), moviePayment.getQuality(),
							address);
					AbstractTelnetWorker.doTelnetWork(telnet);
				}

			} else if (MULTICAST.equals(streaming)) {
				// TODO change
				movie.setMediaType(MediaType.MULTICAST);
				// getIpAddress();
				// movie.setMovieUrl("rtp://@239.255.12.42:5004");
				MoviePayment mp = movie.addMoviePayment(Double.valueOf(
						spinners[2].getValue().toString()).longValue(),
						spinners[2].getQuality());

				String multicastIP = getRandomMulticastIp();
				mp.setMovieUrl("rtp://@" + multicastIP + ":5004");

				String broadcastAddress = ((Setting) Application
						.getEntityMenager().find(Setting.class,
								Setting.BROADCASTIP)).getValue();

				telnet = new MulticastTelnetClient(
						moviePathTextField.getText(), multicastIP,
						mp.getUuid(), broadcastAddress);
				AbstractTelnetWorker.doTelnetWork(telnet);
			}
			// movie.setUuid(telnet.getUuid().toString());
			System.out.println("Starting telnet");

			MovieTab.persistMovie(movie);
		}

			this.dispose();

	}

	private String getIpAddress() {
		String address = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			address = addr.getHostAddress();
		} catch (UnknownHostException e) {
		}
		return address;
	}

	public static String getRandomMulticastIp() {
		String multicastIp = null;
		Random random = new Random();
		Integer first = random.nextInt(15) + 224;
		Integer second = random.nextInt(255);
		Integer third = random.nextInt(255);
		Integer fourth = random.nextInt(255);
		multicastIp = first + "." + second + "." + third + "." + fourth;

		return multicastIp;
	}
}
