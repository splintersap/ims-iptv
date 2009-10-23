package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class MainView {

	private final int menuBarHeight = 100;

	private JFrame mainFrame = null; // @jve:decl-index=0:visual-constraint="12,8"
	private JPanel jContentPane = null;
	private JTabbedPane mainTabs = null;
	private JScrollPane statisticsTab = null;
	private JScrollPane paymentsTab = null;
	private MoviesTab moviesTab = null;	

	/*
	 * Menu buttons.
	 */
	private JButton play;
	private JButton refresh;

	public MainView() {

		// getMainFrame().pack();
		getMainFrame().setVisible(true);
	}

	/**
	 * This method initializes MainFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getMainFrame() {
		if (mainFrame == null) {
			mainFrame = new JFrame();
			mainFrame.setSize(new Dimension(704, 445));
			mainFrame.setContentPane(getJContentPane());
			mainFrame.setTitle("Watcher");

			mainFrame.setJMenuBar(getJMenuBar());

			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.pack();
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

			jContentPane.add(getJToolBar(), BorderLayout.NORTH);
			jContentPane.add(getMainTabs(), null);

		}
		return jContentPane;
	}

	/**
	 * This method initializes mainTabs
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getMainTabs() {
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

		JButton stop = new JButton(new ImageIcon("images/stop.gif"));

		JButton rew = new JButton(new ImageIcon("images/rew.gif"));

		play = new JButton(new ImageIcon("images/play.gif"));

		JButton forward = new JButton(new ImageIcon("images/forward.gif"));

		toolBar.add(refresh);
		toolBar.addSeparator();
		toolBar.add(stop);
		toolBar.add(rew);
		toolBar.add(play);
		toolBar.add(forward);

		return toolBar;
	}

	public JButton getPlayButton() {
		return this.play;
	}

	public JButton getRefreshButton() {
		return this.refresh;
	}

	public Dimension getSize() {
		return this.mainFrame.getSize();
	}

	public int getMenuBarHeight() {
		return this.menuBarHeight;
	}

}
