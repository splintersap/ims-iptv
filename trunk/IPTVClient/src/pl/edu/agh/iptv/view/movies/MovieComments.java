package pl.edu.agh.iptv.view.movies;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pl.edu.agh.iptv.commons.CommonComment;
import pl.edu.agh.iptv.commons.CommonMovie;
import pl.edu.agh.iptv.components.ResizableGridLayout;

public class MovieComments {

	private CommonMovie movie;
	private JTextArea commentField;
	private JButton commentButton;

	public MovieComments(CommonMovie movie) {
		this.movie = movie;
	}

	public JScrollPane displayMovieComments() {

		JPanel commPanel = new JPanel();
		JScrollPane commScrollPane = new JScrollPane(commPanel);

		commScrollPane.setPreferredSize(new Dimension(200, 100));

		ResizableGridLayout mainLayout = new ResizableGridLayout(movie
				.getCommentList().size() * 2, 1);
		commPanel.setLayout(mainLayout);

		for (CommonComment comment : movie.getCommentList()) {

			JTextArea commInfo = new JTextArea();
			commInfo.setLineWrap(true);
			commInfo.setFont(new Font("Times New Roman", Font.BOLD, 12));
			commInfo.setText("User: " + comment.getUser() + " - "
					+ comment.getDate());

			JTextArea commText = new JTextArea();
			commText.setLineWrap(true);
			commText.setText(comment.getComment() + "\n");

			commPanel.add(commInfo);
			commPanel.add(commText);
		}

		return commScrollPane;

	}

	public JScrollPane displayCommentField() {

		commentField = new JTextArea();
		JScrollPane commentFieldPane = new JScrollPane(commentField);
		commentField.setPreferredSize(new Dimension(200, 80));
		commentField.setLineWrap(true);
		return commentFieldPane;

	}

	public JButton displayCommentButton() {

		commentButton = new JButton("Post Comment");
		commentButton.setSize(new Dimension(60, 20));
		return commentButton;

	}

	public JTextArea getCommentField() {
		return this.commentField;
	}

	public JButton getCommentButton() {
		return this.commentButton;
	}
}
