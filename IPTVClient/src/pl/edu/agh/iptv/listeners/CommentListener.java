package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.movies.MovieComments;

public class CommentListener implements ActionListener {

	private IPTVClient iptvClient;
	private MovieComments movieComment;

	public CommentListener(IPTVClient iptvClient, MovieComments movieComment) {
		this.iptvClient = iptvClient;
		this.movieComment = movieComment;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub		
		iptvClient.setUserComment(movieComment.getCommentField().getText());
		movieComment.getCommentField().setText("");
	}

}
