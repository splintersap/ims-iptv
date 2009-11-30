package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pl.edu.agh.iptv.view.movies.MoviesTab;

public class CommonWatchingView extends JDialog {

	public CommonWatchingView(JFrame mainFrame, MoviesTab moviesTab) {
		JList moviesList = new JList(moviesTab.getAllMovies().toArray());
		JScrollPane moviesSC = new JScrollPane(moviesList);

		JPanel descPane = new JPanel();

		this.add(moviesSC, BorderLayout.WEST);
		this.add(descPane, BorderLayout.EAST);

		this.pack();
		this.setVisible(true);

	}

}
