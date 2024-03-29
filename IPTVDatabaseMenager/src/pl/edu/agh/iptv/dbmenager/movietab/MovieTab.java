package pl.edu.agh.iptv.dbmenager.movietab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.persistence.Query;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jvnet.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import pl.edu.agh.iptv.dbmenager.main.Application;
import pl.edu.agh.iptv.persistence.Category;
import pl.edu.agh.iptv.persistence.MediaType;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MoviePayment;
import pl.edu.agh.iptv.persistence.Setting;
import pl.edu.agh.iptv.telnet.AbstractTelnetWorker;
import pl.edu.agh.iptv.telnet.RemovingTelnetClient;
import pl.edu.agh.iptv.telnet.StartBroadcastTelnetClient;
import pl.edu.agh.iptv.telnet.StopBroadcastTelnetClient;

public class MovieTab extends JPanel {

	private static final long serialVersionUID = -7450798905446077365L;


	JTextArea descriptionTextArea = new JTextArea(10, 20);

	static DBTableModel model;

	Movie movie;

	final JTable table;

	JButton addLeafButton;

	JButton removeLeafButton;

	JButton removeButton;
	
	PricesPanel pricesPanel;
	
	CommentsPanel commentsPanel;
	
	RatingPanel ratingPanel;
	
	JButton startMulticastButton;
	
	JButton stopMulticastButton;

	public MovieTab() {
		super(new GridLayout(2, 0));
		Query query = Application.getEntityMenager().createQuery("FROM Movie");
		final List<Movie> movieList = query.getResultList();

		model = new DBTableModel(movieList, Application.getEntityMenager());

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
				//TODO ip of telnet
				System.out.println("Remove button started");
				System.out.println("Selected row = " + table.getSelectedRow());
				Movie movie = MovieTab.this.movie;
				//String uuid = (String) table.getValueAt(table.getSelectedRow(), 6);
				for(MoviePayment moviePayment : movie.getMoviePayments())
				{
					AbstractTelnetWorker telnet = new RemovingTelnetClient(moviePayment.getUuid(), "127.0.0.1");
					AbstractTelnetWorker.doTelnetWork(telnet);
				}
				
				int selectedRow = table.getSelectedRow();
				model.removeMovie(table.convertRowIndexToModel(selectedRow));
			}
		});
		newMovieButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AddMovieDialog();

			}
		});
		
		startMulticastButton = new JButton("Start multicast");
		startMulticastButton.setEnabled(false);
		startMulticastButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String address  = ((Setting)Application.getEntityMenager().find(Setting.class, Setting.BROADCASTIP)).getValue();
				for(MoviePayment payment : movie.getMoviePayments()) {
					AbstractTelnetWorker telnet = new StartBroadcastTelnetClient(address, payment.getUuid());
					AbstractTelnetWorker.doTelnetWork(telnet);
				}
			}});
		stopMulticastButton = new JButton("Stop multicast");
		stopMulticastButton.setEnabled(false);
		stopMulticastButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for(MoviePayment payment : movie.getMoviePayments()) {
					String address  = ((Setting)Application.getEntityMenager().find(Setting.class, Setting.BROADCASTIP)).getValue();
					AbstractTelnetWorker telnet = new StopBroadcastTelnetClient(address, payment.getUuid());
					AbstractTelnetWorker.doTelnetWork(telnet);
				}
			}});
		
		newMoviePanel.add(newMovieButton);
		newMoviePanel.add(removeButton);
		newMoviePanel.add(startMulticastButton);
		newMoviePanel.add(stopMulticastButton);
		
		tablePanel.add(newMoviePanel, BorderLayout.SOUTH);
		// Add the scroll pane to this panel.
		add(tablePanel);

		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent descriptionPanel = makeDescriptionPanel();
		tabbedPane.addTab("Description", descriptionPanel);

		pricesPanel = new PricesPanel();
		tabbedPane.addTab("Prices", pricesPanel);
		
		commentsPanel = new CommentsPanel();
		tabbedPane.addTab("Comments", commentsPanel);
		
		ratingPanel = new RatingPanel();
		tabbedPane.addTab("Rating", ratingPanel);

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
				Application.getEntityMenager().getTransaction().begin();
				Application.getEntityMenager().persist(movie);
				movie.setDescription(descriptionTextArea.getText());
				Application.getEntityMenager().getTransaction().commit();
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
		Application.getEntityMenager().getTransaction().begin();
		Application.getEntityMenager().persist(movie);
		Application.getEntityMenager().getTransaction().commit();
		model.addMovie(movie);
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
		if(movie.getMediaType().equals(MediaType.MULTICAST)) {
			startMulticastButton.setEnabled(true);
			stopMulticastButton.setEnabled(true);
		} else {
			startMulticastButton.setEnabled(false);
			stopMulticastButton.setEnabled(false);
		}
		
		String description = movie.getDescription();
		descriptionTextArea.setText(description);

		Application.getEntityMenager().getTransaction().begin();
		Application.getEntityMenager().persist(movie);
		this.movie = movie;
		List<MoviePayment> moviePayments = movie.getMoviePayments();
		pricesPanel.createNodes(moviePayments);
		commentsPanel.showComments(movie);
		ratingPanel.showRating(movie);
		repaint();
		Application.getEntityMenager().getTransaction().commit();

		removeButton.setEnabled(true);

	}



	
}
