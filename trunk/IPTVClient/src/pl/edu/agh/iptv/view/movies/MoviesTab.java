package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.controllers.MoviesListController;
import pl.edu.agh.iptv.view.MainView;

public class MoviesTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int moviesListPaneWidth = 250;

	private JScrollPane moviesListPane = null;
	private JScrollPane moviesDescPane = null;

	private JList moviesList = null;
	private JPanel panelForMoviesList = null;

	private MainView parent = null;

	/**
	 * This method initializes moviesListPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getMoviesListPane() {
		if (moviesListPane == null) {

			moviesListPane = new JScrollPane(
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

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
	 * This method initializes moviesDescPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getMoviesDescPane() {
		if (moviesDescPane == null) {
			moviesDescPane = new JScrollPane(
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			moviesDescPane.setPreferredSize(new Dimension(
					this.parent.getSize().width - this.moviesListPaneWidth,
					this.parent.getSize().height));
			moviesDescPane.setMinimumSize(new Dimension(
					this.parent.getSize().width - this.moviesListPaneWidth,
					this.parent.getSize().height));

		}
		return moviesDescPane;
	}

	private JPanel getPanelForMoviesList() {

		if (panelForMoviesList == null) {
			panelForMoviesList = new JPanel();
			panelForMoviesList.setLayout(new ResizableGridLayout(4, 1));

			panelForMoviesList.setPreferredSize(new Dimension(
					this.moviesListPaneWidth, this.parent.getSize().height));

			JLabel orderedMoviesLabel = new JLabel("ORDERED MOVIES");
			orderedMoviesLabel.setFont(new Font("Times New Roman", Font.BOLD
					| Font.ITALIC, 14));

			moviesList = new JList();
			moviesList.setAutoscrolls(true);
			moviesList.setName("Ordered movies");
			moviesList.setToolTipText("Ordered movies");

			JLabel recommendedMoviesLabel = new JLabel("RECOMMENDED MOVIES");
			recommendedMoviesLabel.setFont(new Font("Times New Roman",
					Font.BOLD | Font.ITALIC, 14));

			panelForMoviesList.add(orderedMoviesLabel);
			panelForMoviesList.add(moviesList);
			panelForMoviesList.add(recommendedMoviesLabel);
		}

		return panelForMoviesList;

	}

	public void setListOfMovies(String[] moviesArray) {

		moviesList.setListData(moviesArray);
		this.getPanelForMoviesList().repaint();

	}

	public JList getMoviesList() {
		return this.moviesList;
	}

}
