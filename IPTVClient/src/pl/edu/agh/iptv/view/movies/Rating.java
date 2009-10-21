package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.edu.agh.iptv.components.ResizableGridLayout;

public class Rating {

	double avg;

	int currentlyHighlighted = 0;
	int currentlyUnhighlighted = 0;

	ImageIcon smallEmpty = new ImageIcon("images/rating/empty_star_small.gif");
	ImageIcon smallFull = new ImageIcon("images/rating/full_star_small.gif");

	public Rating(double avg) {
		this.avg = avg;
	}

	public JPanel displayAverageRating() {

		JPanel output = new JPanel();

		output.setLayout(new ResizableGridLayout(1, 10));

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
		output.add(ratingHeader);

		int numb = 0;
		for (int i = 1; i <= full; i++) {
			numb++;
			output.add(new JLabel(new ImageIcon(
					"images/rating/full_star_big.gif")));
		}

		if (half == 1) {
			output.add(new JLabel(new ImageIcon(
					"images/rating/half_star_big.gif")));
			numb++;
		}

		for (int i = numb; i < 5; i++) {
			output.add(new JLabel(new ImageIcon(
					"images/rating/empty_star_big.gif")));
		}

		output.add(emptyHeader);

		return output;
	}

	public JPanel fieldForRating() {

		JPanel ratingPanel = new JPanel();
		ratingPanel.setLayout(new ResizableGridLayout(1, 10));
		Font headerFont = new Font("Arial", Font.BOLD | Font.ITALIC, 14);
		JLabel ratingHeader = new JLabel("Your rate");
		ratingHeader.setFont(headerFont);
		ratingPanel.add(ratingHeader);

		for (int i = 1; i <= 5; i++) {
			JLabel label = new JLabel(smallEmpty);

			label.addMouseListener(new RatingListener(i));
			ratingPanel.add(label);

		}

		JLabel emptyHeader = new JLabel();

		emptyHeader.setPreferredSize(new Dimension(150, 40));

		ratingPanel.add(emptyHeader);

		return ratingPanel;

	}

	private class RatingListener implements MouseListener {

		boolean emptyIcon = true;
		int number;

		public RatingListener(int number) {
			this.number = number;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			if (emptyIcon && currentlyHighlighted == number - 1){
				
				((JLabel) e.getComponent()).setIcon(smallFull);
				currentlyHighlighted = number;
				currentlyUnhighlighted = number + 1;
				emptyIcon = !emptyIcon;
				
				
			}else if (!emptyIcon && currentlyUnhighlighted == number + 1){
				
				((JLabel) e.getComponent()).setIcon(smallEmpty);
				currentlyUnhighlighted = number;
				currentlyHighlighted = number - 1;
				emptyIcon = !emptyIcon;
				
			}			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
