package pl.edu.agh.iptv.controllers.helper;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import org.videolan.jvlc.JVLC;
import org.videolan.jvlc.MediaDescriptor;
import org.videolan.jvlc.MediaPlayer;
import org.videolan.jvlc.Video;

import pl.edu.agh.iptv.view.movies.MoviesTab;

/**
 * This class is responsible for opening VLC in a 'Watcher' panel.
 * 
 * @author Wozniak
 * 
 */
public class VLCHelper {

	static java.awt.GridBagConstraints gridBagConstraints;
	static JVLC jvlc;
	static Video video;
	static MediaPlayer mp;
	static MediaDescriptor ds;
	static Canvas jvcanvas;
	static JPanel jvcc;

	private MoviesTab moviesTab;

	public VLCHelper(MoviesTab moviesTab) {
		this.moviesTab = moviesTab;
		this.playMovie();
	}

	private void playMovie() {

		jvlc = new JVLC();
		ds = new MediaDescriptor(jvlc, "C:\\Movies\\wesele.avi");
		mp = ds.getMediaPlayer();
		mp.setJVLC(jvlc);
		video = new Video(jvlc);

		// Canvas
		GraphicsEnvironment graphEnv = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice graphDevice = graphEnv.getDefaultScreenDevice();
		GraphicsConfiguration graphicConf = graphDevice
				.getDefaultConfiguration();

		jvcanvas = new java.awt.Canvas(graphicConf);
		jvcanvas.setBackground(Color.black);
		jvcanvas.setSize(800, 600);
		jvcanvas.addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				mp.pause();
			}

			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		jvcanvas.invalidate();
		jvcanvas.setVisible(true);
		jvcanvas.setSize(new Dimension(this.moviesTab.getMoviesDescPane()
				.getWidth() - 20, this.moviesTab.getMoviesDescPane()
				.getHeight() - 20));

		// Panel
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.CENTER;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jvcc = new JPanel(true);
		jvcc.setLayout(new java.awt.GridBagLayout());
		jvcc.setSize(new Dimension(jvcanvas.getWidth(), jvcanvas.getHeight()));
		jvcc.add(jvcanvas, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.CENTER;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;

		this.moviesTab.getMoviesDescPane().add(jvcc, gridBagConstraints);

		jvcanvas.addNotify();
		jvcanvas.requestFocus();
		jvcanvas.createBufferStrategy(2);

		jvlc.setVideoOutput(jvcanvas); // second way

		mp.play();

	}
}
