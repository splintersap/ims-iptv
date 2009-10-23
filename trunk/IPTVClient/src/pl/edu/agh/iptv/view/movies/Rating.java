package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.components.ResizableGridLayout;

public class Rating {

	/*
	 * Average rate for a chosen movie.
	 */
	double avg;

	private IPTVClient iptvClient;

	int currentlyHighlighted = 0;
	int currentlyUnhighlighted = 0;

	ImageIcon smallEmpty = new ImageIcon("images/rating/empty_star_small.gif");
	ImageIcon smallFull = new ImageIcon("images/rating/full_star_small.gif");

	public Rating(double avg) {
		this.avg = avg;
	}

	/**
	 * This function creates the panel which displays information about total
	 * rate which is an average of all rates given by people watching selected
	 * movie.
	 * 
	 * @return panel with information about total rate
	 */
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

	/**
	 * This function creates the panel used for rating the movies.
	 * 
	 * @return panel for rating
	 */
	public JPanel fieldForRating() {

		JPanel ratingPanel = new JPanel();
		ratingPanel.setLayout(new ResizableGridLayout(1, 10));
		Font headerFont = new Font("Arial", Font.BOLD | Font.ITALIC, 14);
		JLabel ratingHeader = new JLabel("Your rate");
		ratingHeader.setFont(headerFont);
		ratingPanel.add(ratingHeader);

		RatingPanel labelsPanel = new RatingPanel(5);
		labelsPanel.setLayout(new GridLayout(1, 5));

		for (int i = 0; i < 5; i++) {
			JLabel label = new JLabel(smallEmpty);
			labelsPanel.addLabel(label, i);
			label
					.addMouseListener(new SingleLabelRatingListener(
							labelsPanel, i));
			labelsPanel.add(label);

		}

		ratingPanel.add(labelsPanel);

		JLabel emptyHeader = new JLabel();

		emptyHeader.setPreferredSize(new Dimension(150, 40));

		ratingPanel.add(emptyHeader);

		return ratingPanel;

	}

	public void setIPTVClient(IPTVClient iptvClient) {
		this.iptvClient = iptvClient;
	}

	/**
	 * Panel holding all labels with the stars for rating. It contains the
	 * method which changes the images (full/empty star) to express the rating
	 * scale.
	 * 
	 * @author Wozniak
	 * 
	 */
	private class RatingPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		/*
		 * Array of all the labels for rating.
		 */
		private JLabel[] labels;

		private int numberOfLabels;

		public RatingPanel(int numberOfLabels) {
			super();
			this.numberOfLabels = numberOfLabels;
			labels = new JLabel[this.numberOfLabels];
		}

		public void addLabel(JLabel label, int position) {
			if (position < labels.length && position >= 0)
				labels[position] = label;
		}

		/**
		 * This function updates images on the labels. Labels which has numbers
		 * smaller or equal to the currently selected label are getting full
		 * star. The rest of them get empty star.
		 * 
		 * @param currentLabel
		 *            current label to which points user
		 */
		public void updateRating(int currentLabel) {
			for (int i = 0; i <= currentLabel; i++) {
				labels[i].setIcon(smallFull);
			}

			for (int i = currentLabel + 1; i < numberOfLabels; i++) {
				labels[i].setIcon(smallEmpty);
			}
		}

	}

	/**
	 * Class listening to the actions regarding single label with a star for
	 * rating.
	 * 
	 * @author Wozniak
	 * 
	 */
	private class SingleLabelRatingListener implements MouseListener {

		/*
		 * Rating panel which holds all labels with starts for rating.
		 */
		private RatingPanel ratingPanel;

		/*
		 * Number of a label to which this listener belongs to.
		 */
		private int number;

		public SingleLabelRatingListener(RatingPanel ratingPanel, int number) {
			this.number = number;
			this.ratingPanel = ratingPanel;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			iptvClient.setUserRating(this.number);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			ratingPanel.updateRating(number);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			ratingPanel.updateRating(-1);
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
