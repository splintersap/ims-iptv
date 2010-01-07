package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.videolan.jvlc.VLCException;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.controllers.helper.VLCHelper;
import pl.edu.agh.iptv.data.Movie;
import pl.edu.agh.iptv.data.MovieDescription;
import pl.edu.agh.iptv.listeners.IperfManagerListener;
import pl.edu.agh.iptv.listeners.PlayListener;
import pl.edu.agh.iptv.view.chat.ChatTab;
import pl.edu.agh.iptv.view.movies.DescriptionPanel;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class MainView {

	private final int menuBarHeight = 100;

	private MyJFrame mainFrame = null; // @jve:decl-index=0:visual-constraint="12,8"
	private JPanel jContentPane = null;
	private JTabbedPane mainTabs = null;
	private JScrollPane statisticsTab = null;
	private JScrollPane paymentsTab = null;
	private MoviesTab moviesTab = null;

	private JPopupMenu playMenu = new JPopupMenu();

	private PlayListener playListener;

	private JLabel bandwidthLabel;
	private JLabel moneyLabel;

	public static ImageIcon playIcon = new ImageIcon("images/play.gif");

	public static ImageIcon pauseIcon = new ImageIcon("images/pause.gif");

	/*
	 * Menu buttons.
	 */
	private static JButton play;
	private JButton stop;
	// private JButton rew;
	// private JButton forward;
	private JButton refresh;
	private JButton record;

	private JButton orderButton;

	private ChatTab chatTab;

	public MainView() {

		// getMainFrame().pack();
		getMainFrame().setVisible(true);
	}

	/**
	 * This method initializes MainFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	public JFrame getMainFrame() {
		if (mainFrame == null) {
			mainFrame = new MyJFrame();
			mainFrame.setSize(new Dimension(704, 445));
			mainFrame.setContentPane(getJContentPane());
			mainFrame.setTitle("Watcher");

			mainFrame.setJMenuBar(getJMenuBar());

			mainTabs.addTab("Chat", chatTab = new ChatTab(mainFrame,
					getMoviesTab()));

			mainFrame.setIconImage(new ImageIcon("images/tv_icon.gif")
					.getImage());

			mainFrame.pack();
			mainFrame.setLocationRelativeTo(null);

		}
		return mainFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			ResizableGridLayout gridLayout = new ResizableGridLayout(2, 1, 0, 5);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout);

			ResizableGridLayout menuLabelGL = new ResizableGridLayout(1, 3, 2,
					0);
			JPanel menuLabelP = new JPanel();
			menuLabelP.setLayout(menuLabelGL);
			menuLabelP.add(getJToolBar());
			menuLabelP.add(getBandwidthLabel());
			menuLabelP.add(getMoneyLabel());

			jContentPane.add(menuLabelP, BorderLayout.NORTH);
			jContentPane.add(getMainTabs(), null);

		}
		return jContentPane;
	}

	/**
	 * This method initializes mainTabs
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	public JTabbedPane getMainTabs() {
		if (mainTabs == null) {
			mainTabs = new JTabbedPane();
			mainTabs.addTab("Movies", getMoviesTab());
			mainTabs.addTab("Statistics", getStatisticsTabBeans());
			mainTabs.addTab("Payments", getPaymentsTab());
		}
		return mainTabs;
	}

	/**
	 * This method initializes statisticsTabBeans
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getStatisticsTabBeans() {
		if (statisticsTab == null) {
			statisticsTab = new JScrollPane();
			statisticsTab.setToolTipText("Movies popularity");
		}
		return statisticsTab;
	}

	/**
	 * This method initializes paymentsTab
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getPaymentsTab() {
		if (paymentsTab == null) {
			paymentsTab = new JScrollPane();
			paymentsTab.setToolTipText("Your bills");
		}
		return paymentsTab;
	}

	/**
	 * This method initializes moviesTab1
	 * 
	 * @return pl.edu.agh.iptv.view.movies.MoviesTab
	 */
	public MoviesTab getMoviesTab() {
		if (this.moviesTab == null) {
			this.moviesTab = new MoviesTab(this);
		}
		return this.moviesTab;
	}

	private JMenuBar getJMenuBar() {

		// Where the GUI is created:
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the first menu.
		menu = new JMenu("Program");
		menu.setMnemonic(KeyEvent.VK_P);
		menu.getAccessibleContext().setAccessibleDescription(
				"Direct program actions");

		menuBar.add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
				ActionEvent.ALT_MASK));

		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}

		});

		menuItem.getAccessibleContext().setAccessibleDescription(
				"Exiting the program");
		menu.add(menuItem);

		// Build second menu in the menu bar.
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menu.getAccessibleContext().setAccessibleDescription("Open help");
		menuBar.add(menu);

		return menuBar;
	}

	public JToolBar getJToolBar() {
		JToolBar toolBar = new JToolBar("Formatting");

		refresh = new JButton(new ImageIcon("images/refresh.gif"));

		stop = new JButton(new ImageIcon("images/stop.gif"));
		stop.setEnabled(false);

		play = new JButton(playIcon);
		play.addMouseListener(mouseListener);
		play.setEnabled(false);

		orderButton = new JButton(new ImageIcon("images/cart.gif"));
		orderButton.setName("ORDER");
		orderButton.setEnabled(false);

		record = new JButton(new ImageIcon("images/record.gif"));
		record.setName("RECORD");
		record.setEnabled(false);

		toolBar.add(refresh);
		toolBar.addSeparator();
		toolBar.add(stop);
		toolBar.add(play);
		toolBar.addSeparator();
		toolBar.add(record);
		toolBar.addSeparator();
		toolBar.add(orderButton);

		return toolBar;
	}

	public JButton getPlayButton() {
		return play;
	}

	public JButton getStopButton() {
		return this.stop;
	}

	public JButton getRefreshButton() {
		return this.refresh;
	}

	public JButton getOrderMoviebButton() {
		return this.orderButton;
	}

	public JButton getRecordMovieButton() {
		return this.record;
	}

	public Dimension getSize() {
		return this.mainFrame.getSize();
	}

	public int getMenuBarHeight() {
		return this.menuBarHeight;
	}

	public ChatTab getChatTab() {
		return this.chatTab;
	}

	public JLabel getBandwidthLabel() {
		if (this.bandwidthLabel == null) {
			bandwidthLabel = new JLabel("Requesting server for bandwidth...");
		}
		return this.bandwidthLabel;
	}
	
	public JLabel getMoneyLabel(){
		if(this.moneyLabel == null){
			moneyLabel = new JLabel("Requesting server for credit info");
		}
		return this.moneyLabel;
	}

	public void setWindowCloseOperation(IperfManagerListener iperfListener) {
		getMainFrame().addWindowListener(iperfListener);
	}

	public void setButtonsEnabelment(final boolean ordered,
			boolean moreToOrder, boolean isBroadcast) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				play.setEnabled(ordered);
			}

		});
		if (moviesTab.getDescriptionPanel() instanceof DescriptionPanel) {
			adjustPlayMenu(((DescriptionPanel) moviesTab.getDescriptionPanel())
					.getMovie());
		}
		stop.setEnabled(ordered);
		orderButton.setEnabled(moreToOrder);
		record.setEnabled(isBroadcast);
	}

	public void adjustPlayMenu(Movie movie) {
		playMenu.removeAll();
		for (MovieDescription desc : movie.getMovieDescriptionList()) {
			playMenu.add(makeMenuItem(desc.getQuality(), desc.isOrdered()));
		}
	}

	private JMenuItem makeMenuItem(String label, boolean ordered) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(playListener);
		item.setEnabled(ordered);
		return item;
	}

	MouseListener mouseListener = new MouseAdapter() {

		public void mousePressed(MouseEvent e) {
			// checkPopup(e);
		}

		public void mouseClicked(MouseEvent e) {
			checkPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			// checkPopup(e);
		}

		private void checkPopup(MouseEvent e) {

			if (!playListener.isPaused()
					&& play.getIcon().equals(MainView.playIcon)
					&& !VLCHelper.isPlayingMovie) {
				playMenu.show(e.getComponent(), play.getX() - 100, play.getY()
						+ play.getHeight());
			} else if (playListener.isPaused()
					&& play.getIcon().equals(MainView.playIcon)
					&& VLCHelper.isPlayingMovie) {
				try {
					VLCHelper.playlist.togglePause();
				} catch (VLCException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				playListener.setPaused(false);
				play.setIcon(MainView.pauseIcon);
			} else if (play.getIcon().equals(MainView.pauseIcon)
					&& !playListener.isPaused()) {
				if (VLCHelper.isPlayingMovie) {
					try {
						VLCHelper.playlist.togglePause();
					} catch (VLCException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					playListener.setPaused(true);
					play.setIcon(MainView.playIcon);
				}
			}

		}
	};

	public void setPlayListner(PlayListener playListner) {
		this.playListener = playListner;
	}

	public PlayListener getPlayListener() {
		return this.playListener;
	}

}
