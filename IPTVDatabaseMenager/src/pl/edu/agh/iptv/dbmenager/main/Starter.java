package pl.edu.agh.iptv.dbmenager.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jvnet.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import pl.edu.agh.iptv.persistence.Category;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MoviePayment;
import pl.edu.agh.iptv.telnet.TelnetWorker;

public class Starter extends JPanel {

	private static final long serialVersionUID = -7450798905446077365L;


	private static EntityManagerFactory emf;

	private static EntityManager em;

	JTextArea descriptionTextArea = new JTextArea(10, 20);

	static DBTableModel model;

	Movie movie;

	final JTable table;

	JButton addLeafButton;

	JButton removeLeafButton;

	JButton removeButton;
	
	PricesPanel pricesPanel;

	public Starter() {
		super(new GridLayout(2, 0));
		Query query = em.createQuery("FROM Movie");
		final List<Movie> movieList = query.getResultList();

		model = new DBTableModel(movieList, em);

		table = new JTable(model);
		setUpSportColumn(table, table.getColumnModel().getColumn(2));
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);


		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectMovieAction(table);
			}
		});

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		JPanel newMoviePanel = new JPanel();
		JButton newMovieButton = new JButton("New movie");
		removeButton = new JButton("Remove");
		removeButton.setEnabled(false);
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Remove button started");
				System.out.println("Selected row = " + table.getSelectedRow());
				String uuid = (String) table.getValueAt(table.getSelectedRow(), 6);
				int selectedRow = table.getSelectedRow();
				TelnetWorker telnet = TelnetWorker.removeInstance(uuid);
				telnet.start();
				try {
					telnet.join();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				
				model.removeMovie(table.convertRowIndexToModel(selectedRow));
			}
		});
		newMovieButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AddMovieDialog();

			}
		});
		newMoviePanel.add(newMovieButton);
		newMoviePanel.add(removeButton);
		tablePanel.add(newMoviePanel, BorderLayout.SOUTH);
		// Add the scroll pane to this panel.
		add(tablePanel);

		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent panel1 = makeDescriptionPanel();
		tabbedPane.addTab("Description", panel1);

		pricesPanel = new PricesPanel();
		tabbedPane.addTab("Prices", pricesPanel);

		add(tabbedPane);
	}

	protected JComponent makeDescriptionPanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new BorderLayout());
		descriptionTextArea.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(descriptionTextArea);

		panel.add(scrollPane, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				em.getTransaction().begin();
				em.persist(movie);
				movie.setDescription(descriptionTextArea.getText());
				em.getTransaction().commit();
			}
		});

		JButton revertButton = new JButton("Revert");
		revertButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				descriptionTextArea.setText(movie.getDescription());
			}
		});
		buttonPanel.add(saveButton);
		buttonPanel.add(revertButton);
		panel.add(buttonPanel, BorderLayout.PAGE_END);
		return panel;
	}

	public static void persistMovie(Movie movie) {
		em.getTransaction().begin();
		em.persist(movie);
		em.getTransaction().commit();
		model.addMovie(movie);
	}
	
	public static EntityManager getEntityMenager()
	{
		return em;
	}

	public void setUpSportColumn(JTable table, TableColumn sportColumn) {
		// Set up the editor for the sport cells.
		JComboBox comboBox = new JComboBox(Category.values());
		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

		// Set up tool tips for the sport cells.
		DefaultTableCellRenderer renderer = new SubstanceDefaultTableCellRenderer();
		sportColumn.setCellRenderer(renderer);
	}

	private void selectMovieAction(JTable table) {

		DBTableModel model = (DBTableModel) table.getModel();
		if(table.getSelectedRow() == -1)
		{
			return;
		}
		Movie movie = model.getMovieList().get(table.getSelectedRow());
		String description = movie.getDescription();
		descriptionTextArea.setText(description);

		em.getTransaction().begin();
		em.persist(movie);
		this.movie = movie;
		List<MoviePayment> moviePayments = movie.getMoviePayments();
		pricesPanel.createNodes(moviePayments);
		em.getTransaction().commit();

		removeButton.setEnabled(true);

	}

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

		// Create and set up the content pane.
		Starter newContentPane = new Starter();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setLocationRelativeTo(null);

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
