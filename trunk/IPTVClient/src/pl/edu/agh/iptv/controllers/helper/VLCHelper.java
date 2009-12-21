package pl.edu.agh.iptv.controllers.helper;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import org.videolan.jvlc.JVLC;
import org.videolan.jvlc.MediaDescriptor;
import org.videolan.jvlc.MediaPlayer;
import org.videolan.jvlc.Playlist;
import org.videolan.jvlc.VLCException;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.movies.MoviesTab;

/**
 * This class is responsible for opening VLC in a 'Watcher' panel.
 * 
 * @author Wozniak
 * 
 */
public class VLCHelper implements ComponentListener {

	static java.awt.GridBagConstraints gridBagConstraints;
	public static JVLC jvlc;
	// static Video video;
	public static MediaPlayer mp;
	static MediaDescriptor ds;
	static Canvas jvcanvas;
	static JPanel jvcc;

	private MainView mainView;
	private MoviesTab moviesTab;
	private String playMovieCommand;
	public static Playlist playlist;

	public static boolean isPlayingMovie = false;

	public VLCHelper(MainView mainView, String playMovieCommand,
			IPTVClient client) {
		this.mainView = mainView;
		this.mainView.getMainFrame().addComponentListener(this);
		this.moviesTab = mainView.getMoviesTab();
		this.playMovieCommand = playMovieCommand;
		this.playMovie();
	}

	private void playMovie() {

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String[] params = new String[] { "-vvv",
						"--plugin-path=C:\\Program Files\\VideoLAN\\VLC\\plugins" };

				jvlc = new JVLC(params);
				playlist = new Playlist(jvlc);

				// Canvas
				GraphicsEnvironment graphEnv = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				GraphicsDevice graphDevice = graphEnv.getDefaultScreenDevice();
				GraphicsConfiguration graphicConf = graphDevice
						.getDefaultConfiguration();

				jvcanvas = new java.awt.Canvas(graphicConf);
				jvcanvas.setBackground(Color.black);
				jvcanvas.setSize(800, 600);
				jvcanvas.invalidate();
				jvcanvas.setVisible(true);
				jvcanvas.setSize(new Dimension(moviesTab.getMoviesDescPane()
						.getWidth() - 20, moviesTab.getMoviesDescPane()
						.getHeight() - 20));

				// Panel
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridwidth = java.awt.GridBagConstraints.CENTER;
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				jvcc = new JPanel(true);
				jvcc.setLayout(new java.awt.GridBagLayout());
				jvcc.setSize(new Dimension(jvcanvas.getWidth(), jvcanvas
						.getHeight()));
				jvcc.add(jvcanvas, gridBagConstraints);

				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridwidth = java.awt.GridBagConstraints.CENTER;
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;

				moviesTab.getMoviesDescPane().getViewport().add(jvcc,
						gridBagConstraints);

				jvcanvas.addNotify();
				jvcanvas.requestFocus();
				jvcanvas.createBufferStrategy(2);

				jvlc.setVideoOutput(jvcanvas); // second way

				isPlayingMovie = true;
				try {
					playlist.add(playMovieCommand, "");
					playlist.play();
				} catch (VLCException e1) {
					e1.printStackTrace();
				}

			}

		});
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		this.jvcanvas.setSize(new Dimension(moviesTab.getMoviesDescPane()
				.getWidth() - 20,
				moviesTab.getMoviesDescPane().getHeight() - 20));
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}
