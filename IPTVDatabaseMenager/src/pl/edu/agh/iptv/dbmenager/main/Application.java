package pl.edu.agh.iptv.dbmenager.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.agh.iptv.dbmenager.movietab.MovieTab;
import pl.edu.agh.iptv.dbmenager.telnettab.TelnetTab;
import pl.edu.agh.iptv.dbmenager.usertab.UsersTab;




public class Application {
	
	private static EntityManagerFactory emf;

	private static EntityManager em;
	
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {

		Logger.getLogger("org").setLevel(Level.WARN);

		emf = Persistence.createEntityManagerFactory("examplePersistenceUnit");
		em = emf.createEntityManager();

		/*
		 * Setting the new look and feel.
		 */
		try {
			UIManager
					.setLookAndFeel("org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Substance Raven Graphite failed to initialize");
		}

		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Database Menager");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTabbedPane tabbedPane = new JTabbedPane();
	
		// Create and set up the content pane.
		MovieTab movieTab = new MovieTab();
		movieTab.setOpaque(true); // content panes must be opaque
		
		UsersTab usersTab = new UsersTab();
		usersTab.setOpaque(true);

		TelnetTab telnetTab = new TelnetTab();
		telnetTab.setOpaque(true);
		
		tabbedPane.addTab("Movies", movieTab);
		tabbedPane.addTab("Users", usersTab);
		tabbedPane.addTab("VLC", telnetTab);
		
		frame.setContentPane(tabbedPane);

		// Display the window.
		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}
	
	public static EntityManager getEntityMenager()
	{
		return em;
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
