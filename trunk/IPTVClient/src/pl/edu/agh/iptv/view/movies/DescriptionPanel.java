package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pl.edu.agh.iptv.components.ResizableGridLayout;

public class DescriptionPanel extends JPanel {

	
	private Rating ratingPanel;
	
	public DescriptionPanel(String title, String director, String category,
			String description, double rating) {

		Font headerFont = new Font("Arial", Font.BOLD | Font.ITALIC, 14);
		this.setLayout(new ResizableGridLayout(11, 1));

		JTextArea titleArea = new JTextArea(title);
		JTextArea directorArea = new JTextArea(director);
		JTextArea categoryArea = new JTextArea(category);
		JTextArea descArea = new JTextArea(description);

		JScrollPane descScrollPane = new JScrollPane(descArea);
		descScrollPane.setPreferredSize(new Dimension(200, 50));
		descScrollPane.setMinimumSize(new Dimension(200, 50));

		JLabel titleHeaderLabel = new JLabel("Title");
		JLabel directorHeaderLabel = new JLabel("Director");
		JLabel categoryHeaderLabel = new JLabel("Category");
		JLabel descHeaderLabel = new JLabel("Description");
		JLabel commentHeaderLabel = new JLabel("Comments");

		titleHeaderLabel.setFont(headerFont);
		directorHeaderLabel.setFont(headerFont);
		categoryHeaderLabel.setFont(headerFont);
		descHeaderLabel.setFont(headerFont);
		commentHeaderLabel.setFont(headerFont);

		ratingPanel = new Rating(rating);
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
		this.add(commentHeaderLabel);

	}

	private JPanel panelWithComments() {
		JPanel commentsPanel = new JPanel();

		return commentsPanel;
	}

	public Rating getRatingPanel(){
		return this.ratingPanel;
	}
	
}
