package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.view.MainView;

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
	private JList orderedMoviesList = null;

	private JList allMoviesList = null;

	private JList recommendedMoviesList = null;

	/*
	 * This is panel which holds the list with movies.
	 */
	private JPanel panelForMoviesList = null;

	private MainView parent = null;

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
			ResizableGridLayout resizableLayout = new ResizableGridLayout(4, 1,
					1, 0);

			panelForMoviesList.setLayout(resizableLayout);

			JLabel orderedMoviesLabel = new JLabel("ORDERED MOVIES");
			JLabel allMoviesLabel = new JLabel("ALL MOVIES");

			orderedMoviesLabel.setFont(new Font("Times New Roman", Font.BOLD
					| Font.ITALIC, 14));

			allMoviesLabel.setFont(new Font("Times New Roman", Font.BOLD
					| Font.ITALIC, 14));

			orderedMoviesList = new JList();
			orderedMoviesList.setAutoscrolls(true);
			orderedMoviesList.setName("Ordered movies");

			orderedMoviesList.setToolTipText("Ordered movies");

			JScrollPane orderedScroller = new JScrollPane(orderedMoviesList);
			orderedScroller.setPreferredSize(new Dimension(listWidth - 30,
					listSize));
			orderedScroller.setMinimumSize(new Dimension(listWidth - 30,
					listSize));

			allMoviesList = new JList();
			allMoviesList.setAutoscrolls(true);
			allMoviesList.setName("All movies");

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

			panelForMoviesList.add(orderedMoviesLabel);
			panelForMoviesList.add(orderedScroller);

			panelForMoviesList.add(allMoviesLabel);
			panelForMoviesList.add(allScroller);

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

		List<String> bought = new ArrayList<String>();
		String[] allMovies = new String[moviesArray.length];

		for (int i = 0; i < moviesArray.length; i++) {
			String str = moviesArray[i];
			String[] movieInformations = str.split("\\|");
			System.out.println(movieInformations[0]);
			allMovies[i] = movieInformations[0];
		
			if ("true".equals(movieInformations[1])) {
				bought.add(allMovies[i]);
			}
		}

		orderedMoviesList.setListData(bought.toArray());
		allMoviesList.setListData(allMovies);
		this.getPanelForMoviesList().repaint();

	}

	public JList getOrderedMoviesList() {
		return this.orderedMoviesList;
	}

	public JList getAllMoviesList() {
		return this.allMoviesList;
	}

	public JList getRecommendedMoviesList() {
		return this.recommendedMoviesList;
	}

}
