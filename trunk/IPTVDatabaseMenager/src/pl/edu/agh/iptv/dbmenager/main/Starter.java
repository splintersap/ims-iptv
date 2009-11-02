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
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jvnet.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import pl.edu.agh.iptv.dbmenager.persistence.Category;
import pl.edu.agh.iptv.dbmenager.persistence.Movie;
import pl.edu.agh.iptv.dbmenager.persistence.MoviePayment;
import pl.edu.agh.iptv.dbmenager.persistence.OrderedMovie;

public class Starter extends JPanel {

	private static final long serialVersionUID = -7450798905446077365L;

	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Prices");

	private static EntityManagerFactory emf;

	private static EntityManager em;

	JTree orderTree;

	JTextArea descriptionTextArea = new JTextArea(10, 20);

	JComponent pricesPane;

	static DBTableModel model;

	Movie movie;

	final JTable table;

	JButton addLeafButton;

	JButton removeLeafButton;

	JButton removeButton;

	public Starter() {
		super(new GridLayout(2, 0));
		Query query = em.createQuery("FROM Movie");
		final List<Movie> movieList = query.getResultList();

		model = new DBTableModel(movieList, em);

		table = new JTable(model);
		setUpSportColumn(table, table.getColumnModel().getColumn(2));
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

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
				model.removeMovie(table.getSelectedRow());
				//table.remove(table.getSelectedRow());
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

		pricesPane = makePricesPanel("Prices");
		tabbedPane.addTab("Prices", pricesPane);

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

	protected JComponent makePricesPanel(String text) {
		JPanel panel = new JPanel(false);
		panel.setLayout(new BorderLayout());

		top = new DefaultMutableTreeNode("Prices");
		// createNodes(top);
		// createNodes();
		orderTree = new JTree(top);
		orderTree.setRootVisible(false);

		JPanel buttonPanel = new JPanel();
		addLeafButton = new JButton("Add");
		addLeafButton.setEnabled(false);
		removeLeafButton = new JButton("Remove");
		removeLeafButton.setEnabled(false);
		buttonPanel.add(addLeafButton);
		buttonPanel.add(removeLeafButton);
		panel.add(orderTree, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.PAGE_END);

		return panel;
	}

	private void createNodes(List<MoviePayment> moviePayments) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode orderNode = null;
		top.removeAllChildren();
		for (MoviePayment moviePayment : moviePayments) {
			category = new DefaultMutableTreeNode(moviePayment.getQuality()
					.name()
					+ " : " + moviePayment.getPirce());
			top.add(category);
			for (OrderedMovie orderedMovie : moviePayment.getOrderedMovieList()) {
				orderNode = new DefaultMutableTreeNode(orderedMovie.getUser()
						.getSip());
				category.add(orderNode);
			}
		}
		SwingUtilities.updateComponentTreeUI(orderTree);

		this.repaint();

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
		System.out.println("Zaznaczono = " + table.getSelectedRow());
		Movie movie = model.getMovieList().get(table.getSelectedRow());
		String description = movie.getDescription();
		descriptionTextArea.setText(description);

		em.getTransaction().begin();
		em.persist(movie);
		this.movie = movie;
		List<MoviePayment> moviePayments = movie.getMoviePayments();
		createNodes(moviePayments);
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
