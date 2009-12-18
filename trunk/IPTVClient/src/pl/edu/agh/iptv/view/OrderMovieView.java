package pl.edu.agh.iptv.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.data.MovieDescription;
import pl.edu.agh.iptv.listeners.OrderMovieListener;
import pl.edu.agh.iptv.view.movies.DescriptionPanel;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class OrderMovieView extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String movieTitle = null;

	private JButton okButton = new JButton("OK");

	private JButton cancelButton = new JButton("Cancel");

	private ButtonGroup group;

	private String quality = "LOW";

	private List<MovieDescription> movieDesc;

	private List<JRadioButton> radioButtons = new ArrayList<JRadioButton>();

	public OrderMovieView(String movieTitle, OrderMovieListener listener,
			JFrame parent, MoviesTab moviesTab) {
		super(parent);
		this.movieTitle = movieTitle;
		this.setPreferredSize(new Dimension(300, 240));
		this.setMaximumSize(new Dimension(300, 240));
		this.setMinimumSize(new Dimension(300, 240));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(5, 1));

		if (moviesTab.getDescriptionPanel() instanceof DescriptionPanel) {
			movieDesc = ((DescriptionPanel) moviesTab.getDescriptionPanel())
					.getMovie().getMovieDescriptionList();
		} else {
			return;
		}

		group = new ButtonGroup();

		mainPanel.add(new JLabel("Choose the quality of movie " + movieTitle));

		for (MovieDescription description : movieDesc) {
			if (!description.isOrdered()) {
				JRadioButton button = new JRadioButton(description.getQuality()
						+ " - " + description.getPrice() / 100.0 + " zl");

				button.setName(description.getQuality());
				button.addActionListener(this);
				radioButtons.add(button);
				mainPanel.add(button);
				group.add(button);
			}
		}

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new ResizableGridLayout(1, 5));
		JPanel okButtonPanel = new JPanel();
		JPanel cancelButtonPanel = new JPanel();
		okButtonPanel.setPreferredSize(new Dimension(100, 35));
		okButtonPanel.setMaximumSize(new Dimension(100, 35));
		cancelButtonPanel.setPreferredSize(new Dimension(100, 35));
		cancelButtonPanel.setMaximumSize(new Dimension(100, 35));

		okButton.setPreferredSize(new Dimension(100, 35));
		okButton.setMaximumSize(new Dimension(100, 35));
		okButton.setName("OK");
		okButton.addActionListener(listener);

		cancelButton.setPreferredSize(new Dimension(100, 35));
		cancelButton.setMaximumSize(new Dimension(100, 35));
		cancelButton.setName("CANCEL");
		cancelButton.addActionListener(listener);

		okButtonPanel.add(okButton);
		cancelButtonPanel.add(cancelButton);

		buttonsPanel.add(new JPanel());
		buttonsPanel.add(okButtonPanel);
		buttonsPanel.add(new JPanel());
		buttonsPanel.add(cancelButtonPanel);
		buttonsPanel.add(new JPanel());

		JScrollPane mainScPane = new JScrollPane(mainPanel);

		mainPanel.add(buttonsPanel);

		this.add(mainScPane);

		this.pack();
		this.setLocationRelativeTo(parent);
		this.setVisible(true);

	}

	public String getSelectedMovieTitle() {
		return this.movieTitle;
	}

	public String getSelectedQuality() {

		return this.quality;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		quality = ((JRadioButton) e.getSource()).getName();

	}

}
