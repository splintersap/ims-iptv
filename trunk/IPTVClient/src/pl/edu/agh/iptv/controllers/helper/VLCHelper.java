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

import javax.management.timer.Timer;
import javax.swing.JPanel;

import org.videolan.jvlc.JVLC;
import org.videolan.jvlc.MediaDescriptor;
import org.videolan.jvlc.MediaPlayer;
import org.videolan.jvlc.Video;
import org.videolan.jvlc.event.MediaPlayerListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.components.MenuListItem;
import pl.edu.agh.iptv.view.movies.MoviesTab;

/**
 * This class is responsible for opening VLC in a 'Watcher' panel.
 * 
 * @author Wozniak
 * 
 */
public class VLCHelper implements ComponentListener {

	static java.awt.GridBagConstraints gridBagConstraints;
	static JVLC jvlc;
	static Video video;
	public static MediaPlayer mp;
	static MediaDescriptor ds;
	static Canvas jvcanvas;
	static JPanel jvcc;

	private MainView mainView;
	private MoviesTab moviesTab;
	private String playMovieCommand;
	private IPTVClient client;

	public static boolean isPlayingMovie = false;

	public VLCHelper(MainView mainView, String playMovieCommand, IPTVClient client) {
		this.mainView = mainView;
		this.mainView.getMainFrame().addComponentListener(this);
		this.moviesTab = mainView.getMoviesTab();
		this.client = client;
		this.playMovieCommand = playMovieCommand;
		this.playMovie();
	}

	private void playMovie() {

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String[] params = new String[] { "-v" , "--plugin-path=C:\\Program Files\\VideoLAN\\VLC\\plugins"};
				jvlc = new JVLC(params);
				ds = new MediaDescriptor(jvlc, playMovieCommand);
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
						// System.out.println("Mamy resize !!");
						// mp.stop();
					}

					public void componentShown(ComponentEvent arg0) {
						// TODO Auto-generated method stub

					}

				});
				jvcanvas.invalidate();
				jvcanvas.setVisible(true);
				jvcanvas.setSize(new Dimension(moviesTab.getMoviesDescPane()
						.getWidth() - 20, moviesTab.getMoviesDescPane()
						.getHeight() - 20));
				// jvcanvas.setSize(new Dimension(100, 100));

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
				mp.play();
mp.addListener(new MediaPlayerListener(){

					@Override
					public void endReached(MediaPlayer arg0) {
						isPlayingMovie = false;
						MainView.getPlayButton().setIcon(MainView.playIcon);
						MenuListItem item = (MenuListItem)moviesTab.getAllMoviesList().getSelectedValue();
						System.out.println("Film sie skonczyl");
						System.out.println("Tytul = " + item.getTitle());
						try {
							Thread.sleep(Timer.ONE_SECOND);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						client.getMovieInformations(item.getTitle());
					}

					@Override
					public void errorOccurred(MediaPlayer arg0) {
						System.out.println("Error occurred");
					}

					@Override
					public void paused(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void playing(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void positionChanged(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void stopped(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void timeChanged(MediaPlayer arg0, long arg1) {
						// TODO Auto-generated method stub
						
					}});
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
