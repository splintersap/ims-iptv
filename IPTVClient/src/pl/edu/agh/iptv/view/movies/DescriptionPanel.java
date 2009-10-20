package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pl.edu.agh.iptv.components.ResizableGridLayout;

public class DescriptionPanel extends JPanel{

	public DescriptionPanel(String title, String director,
			String category, String description) {
		
		Font headerFont = new Font("Arial", Font.BOLD | Font.ITALIC, 14);
		this.setLayout(new ResizableGridLayout(8, 1));

		JTextArea titleArea = new JTextArea(title);
		JTextArea directorArea = new JTextArea(director);
		JTextArea categoryArea = new JTextArea(category);
		JTextArea descArea = new JTextArea(description);
		

		JScrollPane descScrollPane = new JScrollPane(descArea);
		descScrollPane.setPreferredSize(new Dimension(100, 50));
		descScrollPane.setMinimumSize(new Dimension(100, 50));

		JLabel titleHeaderLabel = new JLabel(new ImageIcon("images/rating/full_star_big.gif"));
		JLabel directorHeaderLabel = new JLabel("Director");
		JLabel categoryHeaderLabel = new JLabel("Category");
		JLabel descHeaderLabel = new JLabel("Description");
		JLabel commentHEaderLabel = new JLabel("Rating");

		titleHeaderLabel.setFont(headerFont);
		directorHeaderLabel.setFont(headerFont);
		categoryHeaderLabel.setFont(headerFont);
		descHeaderLabel.setFont(headerFont);

		this.add(titleHeaderLabel);
		this.add(titleArea);
		this.add(directorHeaderLabel);
		this.add(directorArea);
		this.add(categoryHeaderLabel);
		this.add(categoryArea);
		this.add(descHeaderLabel);
		this.add(descScrollPane);		

	}
	
}
