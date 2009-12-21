package pl.edu.agh.iptv.dbmenager.movietab;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MovieRating;

public class RatingPanel extends JPanel {

	private static final long serialVersionUID = -8094462383591203052L;

	public void showRating(Movie movie) {
		this.removeAll();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		List<MovieRating> ratingList = movie.getRating();
		if(ratingList.size() == 0) {
			JLabel ratingLabel = new JLabel("No rating");
			add(ratingLabel);
		} else {
			JLabel overallRatingLabel = new JLabel("Rating : " + movie.getOverallRating().toString());
			add(overallRatingLabel);
			for(MovieRating rating : ratingList)
			{
				JLabel ratingLabel = new JLabel(rating.getUser() + " : " + rating.getRating());
				add(ratingLabel);
			}
		}
	}

}
