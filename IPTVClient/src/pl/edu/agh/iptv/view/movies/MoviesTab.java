package pl.edu.agh.iptv.view.movies;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.view.MainView;
import pl.edu.agh.iptv.view.components.MenuCellRenderer;
import pl.edu.agh.iptv.view.components.MenuListItem;

public class MoviesTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int moviesListPaneWidth = 250;

	/*
	 * This is the pane on the left side with the movies list.
	 */
	private JScrollPane moviesListPane = null;

	/*
	 * This is the pane on the right side with a movie description, ranking and
	 * vlc view (only when the movie is started).
	 */
	private JScrollPane moviesDescPane = null;

	/*
	 * List containing ordered movies.
	 */
	// private JList orderedMoviesList = null;
	private JList allMoviesList = null;

	private JList recommendedMoviesList = null;

	/*
	 * This is panel which holds the list with movies.
	 */
	private JPanel panelForMoviesList = null;

	private MainView parent = null;

	private List<MenuListItem> movieList = null;

	public MoviesTab(MainView parent) {
		this.parent = parent;
		ResizableGridLayout moviesGridLayout = new ResizableGridLayout(1, 2, 2,
				0);
		this.setLayout(moviesGridLayout);
		this.setToolTipText("All about movies");
		this.add(getMoviesListPane(), getMoviesListPane().getName());
		this.add(getMoviesDescPane(), getMoviesDescPane().getName());

	}

	/**
	 * This method initializes moviesListPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getMoviesListPane() {
		if (moviesListPane == null) {

			moviesListPane = new JScrollPane();

			moviesListPane.setPreferredSize(new Dimension(
					this.moviesListPaneWidth, this.parent.getSize().height
							- this.parent.getMenuBarHeight()));
			moviesListPane.setMinimumSize(new Dimension(
					this.moviesListPaneWidth, this.parent.getSize().height
							- this.parent.getMenuBarHeight()));

			moviesListPane.getViewport().add(getPanelForMoviesList());

		}
		return moviesListPane;
	}

	/**
	 * This method initializes moviesDescPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	public JScrollPane getMoviesDescPane() {
		if (moviesDescPane == null) {
			moviesDescPane = new JScrollPane();
			moviesDescPane.setPreferredSize(new Dimension(
					this.parent.getSize().width - this.moviesListPaneWidth,
					this.parent.getSize().height));

		}
		return moviesDescPane;
	}

	/**
	 * Creating the panel which keeps list of movies (ordered, the most popular,
	 * all movies).
	 * 
	 * @return
	 */
	private JPanel getPanelForMoviesList() {

		if (panelForMoviesList == null) {

			int listSize = 50;
			int listWidth = this.moviesListPaneWidth - 50;

			panelForMoviesList = new JPanel();
			//ResizableGridLayout resizableLayout = new ResizableGridLayout(4, 1,
			//		1, 0);

			BorderLayout layout = new BorderLayout();
			layout.setVgap(10);
			panelForMoviesList.setLayout(layout);
			
			
			JPanel labelPanel = new JPanel();
			
			
			
			JLabel allMoviesLabel = new JLabel("VIDEOS");

			allMoviesLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
			allMoviesLabel.setBorder(new EmptyBorder(10,30,0,0));
			labelPanel.add(allMoviesLabel);

			allMoviesList = new JList();
			allMoviesList.setAutoscrolls(true);
			allMoviesList.setName("Movies");
			allMoviesList.setCellRenderer(new MenuCellRenderer());

			allMoviesList.setToolTipText("All movies");

			JScrollPane allScroller = new JScrollPane(allMoviesList);
			allScroller
					.setPreferredSize(new Dimension(listWidth - 30, listSize));
			allScroller.setMinimumSize(new Dimension(listWidth - 30, listSize));

			JLabel orderedMoviesList = new JLabel("ALL MOVIES");
			orderedMoviesList.setFont(new Font("Times New Roman", Font.BOLD
					| Font.ITALIC, 14));
			orderedMoviesList
					.setPreferredSize(new Dimension(listWidth - 10, 30));

			panelForMoviesList.add(labelPanel, BorderLayout.NORTH);
			panelForMoviesList.add(allScroller, BorderLayout.CENTER);

		}

		return panelForMoviesList;

	}

	public void changeDescriptionPanel(String Title) {

	}

	/**
	 * Setting the panel which contains information describing single movie.
	 * 
	 * @param descPanel
	 *            panel which will appear on the right side of 'Watcher'
	 */
	public void setDescriptionPanel(JPanel descPanel) {

		this.moviesDescPane.getViewport().removeAll();
		this.moviesDescPane.getViewport().add(descPanel);

	}

	/**
	 * Method setting the list of movies. It is called when server sends a
	 * response with a list of all movies.
	 * 
	 * @param moviesArray
	 */
	public void setListOfMovies(String[] moviesArray) {

		movieList = new ArrayList<MenuListItem>();

		for (String movieLine : moviesArray) {
			String[] movieInformations = movieLine.split("\\|");
			String title = movieInformations[0];
			String isOrderedString = movieInformations[1];
			Double rating = Double.valueOf(movieInformations[2]);
			String mediaType = movieInformations[3];
			boolean isOrdered = false;

			if ("true".equals(isOrderedString)) {
				isOrdered = true;
			}

			MenuListItem listItem = new MenuListItem(title, rating, mediaType,
					isOrdered);
			movieList.add(listItem);
		}

		Collections.sort(movieList, new Comparator<MenuListItem>() {

			@Override
			public int compare(MenuListItem item1, MenuListItem item2) {
				if (!item1.isOrdered().equals(item2.isOrdered())) {
					return item1.isOrdered().compareTo(item2.isOrdered())
							* (-1);
				}

				if (!item1.getCategory().equals(item2.getCategory())) {
					return item1.getCategory().compareTo(item2.getCategory());
				}

				if (!item1.getRating().equals(item2.getRating())) {
					return item1.getRating().compareTo(item2.getRating())
							* (-1);
				}

				return item1.getTitle().compareTo(item2.getTitle());
			}

		});

		allMoviesList.setListData(movieList.toArray());
		this.getPanelForMoviesList().repaint();

	}

	/*
	 * public JList getOrderedMoviesList() { return this.orderedMoviesList; }
	 */

	public JList getAllMoviesList() {
		return this.allMoviesList;
	}

	public JList getRecommendedMoviesList() {
		return this.recommendedMoviesList;
	}

	public List<MenuListItem> getAllMovies() {
		return this.movieList;
	}

}
