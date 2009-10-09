package pl.edu.agh.iptv.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class MainView {

	private JFrame MainFrame = null; // @jve:decl-index=0:visual-constraint="12,8"
	private JPanel jContentPane = null;
	private JTabbedPane mainTabs = null;
	private JScrollPane moviesTab = null;
	private JScrollPane statisticsTab = null;

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
		}
		return mainTabs;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (moviesTab == null) {
			moviesTab = new JScrollPane();
			moviesTab.setToolTipText("All about movies");
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

}
