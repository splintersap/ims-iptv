package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.data.Movie;

public class DescriptionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Rating ratingPanel;
	private MovieComments commentsPanel;
	private Movie movie;

	public DescriptionPanel(Movie movie) {
		this.movie = movie;
		Font headerFont = new Font("Arial", Font.BOLD | Font.ITALIC, 14);
		this.setLayout(new ResizableGridLayout(15, 1));

		JTextArea titleArea = new JTextArea(movie.getTitle());
		JTextArea directorArea = new JTextArea(movie.getDirector());
		JTextArea categoryArea = new JTextArea(movie.getCategory());
		JTextArea descArea = new JTextArea(movie.getDescription());
		descArea.setLineWrap(true);

		JScrollPane descScrollPane = new JScrollPane(descArea);
		descScrollPane.setPreferredSize(new Dimension(200, 100));

		JLabel titleHeaderLabel = new JLabel("Title");
		JLabel directorHeaderLabel = new JLabel("Director");
		JLabel categoryHeaderLabel = new JLabel("Category");
		JLabel descHeaderLabel = new JLabel("Description");
		JLabel commentHeaderLabel = new JLabel("Comments");
		JLabel userCommentHeaderLabel = new JLabel("Add your comment");

		titleHeaderLabel.setFont(headerFont);
		directorHeaderLabel.setFont(headerFont);
		categoryHeaderLabel.setFont(headerFont);
		descHeaderLabel.setFont(headerFont);
		commentHeaderLabel.setFont(headerFont);
		userCommentHeaderLabel.setFont(headerFont);

		ratingPanel = new Rating(movie);
		this.add(ratingPanel.displayAverageRating());
		this.add(ratingPanel.fieldForRating());
		this.add(titleHeaderLabel);
		this.add(titleArea);
		this.add(directorHeaderLabel);
		this.add(directorArea);
		this.add(categoryHeaderLabel);
		this.add(categoryArea);
		this.add(descHeaderLabel);
		this.add(descScrollPane);

		commentsPanel = new MovieComments(movie);
		this.add(commentHeaderLabel);
		this.add(commentsPanel.displayMovieComments());
		this.add(userCommentHeaderLabel);
		this.add(commentsPanel.displayCommentField());

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(commentsPanel.displayCommentButton());
		this.add(buttonPanel);

	}

	public Rating getRatingPanel() {
		return this.ratingPanel;
	}

	public MovieComments getMovieComments() {
		return this.commentsPanel;
	}

	public Movie getMovie() {
		return this.movie;
	}
	
}
