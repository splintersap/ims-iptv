package pl.edu.agh.iptv.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import pl.edu.agh.iptv.components.ResizableGridLayout;

public class OrderMovieView extends JFrame {

	private String movieTitle = null;

	private JButton okButton = new JButton("OK");

	private JButton cancelButton = new JButton("Cancel");

	public OrderMovieView(String movieTitle) {
		super("Choice window");
		// this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.movieTitle = movieTitle;
		this.setPreferredSize(new Dimension(300, 240));
		this.setMaximumSize(new Dimension(300, 240));
		this.setMinimumSize(new Dimension(300, 240));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(5, 1));

		ButtonGroup group = new ButtonGroup();
		JRadioButton radio1 = new JRadioButton("Low quality");
		JRadioButton radio2 = new JRadioButton("Medium quality");
		JRadioButton radio3 = new JRadioButton("High quality");
		group.add(radio1);
		group.add(radio2);
		group.add(radio3);

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
		cancelButton.setPreferredSize(new Dimension(100, 35));
		cancelButton.setMaximumSize(new Dimension(100, 35));

		okButtonPanel.add(okButton);
		cancelButtonPanel.add(cancelButton);

		buttonsPanel.add(new JPanel());
		buttonsPanel.add(okButtonPanel);
		buttonsPanel.add(new JPanel());
		buttonsPanel.add(cancelButtonPanel);
		buttonsPanel.add(new JPanel());

		JScrollPane mainScPane = new JScrollPane(mainPanel);

		mainPanel.add(new JLabel("Choose the quality of movie " + movieTitle));
		mainPanel.add(radio1);
		mainPanel.add(radio2);
		mainPanel.add(radio3);
		mainPanel.add(buttonsPanel);

		this.add(mainScPane);

		this.pack();
		this.setVisible(true);

	}

}
