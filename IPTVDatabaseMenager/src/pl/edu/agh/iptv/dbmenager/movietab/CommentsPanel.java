package pl.edu.agh.iptv.dbmenager.movietab;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.MovieComment;

public class CommentsPanel extends JPanel {

	private static final long serialVersionUID = -3286899977723008103L;

	public void showComments(Movie movie) {
		this.removeAll();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		List<MovieComment> commentsList = movie.getCommentsList();

		if (commentsList.size() == 0) {
			JLabel commentLabel = new JLabel("No comments");
			this.add(commentLabel);
		} else {

			for (MovieComment comment : commentsList) {
				JLabel commentLabel = new JLabel(comment.getUser() + " : "
						+ comment.getComment());
				this.add(commentLabel);
			}
		}
	}
}
