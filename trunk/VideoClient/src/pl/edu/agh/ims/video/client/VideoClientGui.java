package pl.edu.agh.ims.video.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class VideoClientGui extends JPanel {

	private static final long serialVersionUID = 3146874614664014160L;

	static JButton choseMovieButton;

	static JComboBox moviesComboBox;

	VideoClient client = new VideoClient();

	public static void setMoviesList(String[] moviesStrings) {
		choseMovieButton.setEnabled(true);
		for (String movie : moviesStrings) {
			moviesComboBox.addItem(movie);
		}
		moviesComboBox.setEnabled(true);
	}

	public VideoClientGui() {
		super(new BorderLayout());

		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				client.getMovieList();

			}

		});

		choseMovieButton = new JButton("GO");
		choseMovieButton.setEnabled(false);
		choseMovieButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String movie = (String) moviesComboBox.getSelectedItem();
				client.showChosenMovie(movie);

			}
		});

		add(startButton, BorderLayout.PAGE_START);

		JButton endSessionButton = new JButton("END SESION");
		endSessionButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					client.session.end();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}});
		add(endSessionButton, BorderLayout.LINE_END);
		moviesComboBox = new JComboBox();

		moviesComboBox.setPreferredSize(new Dimension(200, 40));
		moviesComboBox.setEnabled(false);

		add(moviesComboBox, BorderLayout.CENTER);
		add(choseMovieButton, BorderLayout.PAGE_END);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Video Streaming");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new VideoClientGui();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}