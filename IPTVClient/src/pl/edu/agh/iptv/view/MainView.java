package pl.edu.agh.iptv.view;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.JButton;

public class MainView {

	private JFrame MainFrame = null; // @jve:decl-index=0:visual-constraint="12,8"
	private JPanel jContentPane = null;
	private JTabbedPane mainTabs = null;
	private JPanel moviesTab = null;
	private JScrollPane statisticsTab = null;
	private JScrollPane paymentsTab = null;
	private JScrollPane moviesListPane = null;
	private JScrollPane moviesDescPane = null;
	private JButton jButton = null;

	public MainView() {
		getMainFrame().setVisible(true);
	}

	/**
	 * This method initializes MainFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getMainFrame() {
		if (MainFrame == null) {
			MainFrame = new JFrame();
			MainFrame.setSize(new Dimension(704, 445));
			MainFrame.setContentPane(getJContentPane());
			MainFrame.setTitle("Watcher");
		}
		return MainFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout);
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
			mainTabs.addTab("Movies", getJScrollPane());
			mainTabs.addTab("Statistics", getStatisticsTabBeans());
			mainTabs.addTab("Payments", getPaymentsTab());
		}
		return mainTabs;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JPanel getJScrollPane() {
		if (moviesTab == null) {
			moviesTab = new JPanel();
			GridLayout moviesGridLayout = new GridLayout(1, 2);
			moviesTab.setLayout(moviesGridLayout);
			moviesTab.setToolTipText("All about movies");
			moviesTab.add(getMoviesListPane(), getMoviesListPane().getName());
			moviesTab.add(getMoviesDescPane(), getMoviesDescPane().getName());
		}
		return moviesTab;
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
	 * This method initializes moviesListPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getMoviesListPane() {
		if (moviesListPane == null) {
			moviesListPane = new JScrollPane();
			moviesListPane.setSize(100, 100);
		}
		return moviesListPane;
	}

	/**
	 * This method initializes moviesDescPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getMoviesDescPane() {
		if (moviesDescPane == null) {
			moviesDescPane = new JScrollPane();
			moviesDescPane.setSize(100, 100);
		}
		return moviesDescPane;
	}

}
