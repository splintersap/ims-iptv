package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.edu.agh.iptv.components.ResizableGridLayout;

public class Rating extends JPanel {

	public Rating(double avg) {
		this.setLayout(new ResizableGridLayout(1, 10));

		int full = (int) avg;
		int half = 0;
		double rest = avg - full;

		if (rest >= 0.25 && rest <= 0.75) {
			half = 1;
		} else if (rest > 0.75) {
			full += 1;
		}

		JLabel ratingHeader = new JLabel("Rating");

		JLabel emptyHeader = new JLabel();
		emptyHeader.setPreferredSize(new Dimension(90, 40));

		Font headerFont = new Font("Arial", Font.BOLD | Font.ITALIC, 14);
		ratingHeader.setFont(headerFont);
		this.add(ratingHeader);

		int numb = 0;
		for (int i = 1; i <= full; i++) {
			numb++;
			this.add(new JLabel(
					new ImageIcon("images/rating/full_star_big.gif")));
		}

		if (half == 1) {
			this.add(new JLabel(
					new ImageIcon("images/rating/half_star_big.gif")));
			numb++;
		}

		for (int i = numb; i < 5; i++) {
			this.add(new JLabel(new ImageIcon(
					"images/rating/empty_star_big.gif")));
		}

		this.add(emptyHeader);

	}

}
