package pl.edu.agh.iptv.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import pl.edu.agh.iptv.components.ResizableGridLayout;
import pl.edu.agh.iptv.listeners.OrderMovieListener;

public class OrderMovieView extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String movieTitle = null;

	private JButton okButton = new JButton("OK");

	private JButton cancelButton = new JButton("Cancel");

	private ButtonGroup group;

	private JRadioButton radio1, radio2, radio3;

	private String quality = "LOW";

	public OrderMovieView(String movieTitle, OrderMovieListener listener,
			JFrame parent) {
		super(parent);
		this.movieTitle = movieTitle;
		this.setPreferredSize(new Dimension(300, 240));
		this.setMaximumSize(new Dimension(300, 240));
		this.setMinimumSize(new Dimension(300, 240));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(5, 1));

		group = new ButtonGroup();
		radio1 = new JRadioButton("Low quality");
		radio1.setSelected(true);
		radio1.setName("LOW");
		radio1.addActionListener(this);
		radio2 = new JRadioButton("Medium quality");
		radio2.setName("MEDIUM");
		radio2.addActionListener(this);
		radio3 = new JRadioButton("High quality");
		radio3.setName("HIGH");
		radio3.addActionListener(this);
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

		mainPanel.add(new JLabel("Choose the quality of movie " + movieTitle));
		mainPanel.add(radio1);
		mainPanel.add(radio2);
		mainPanel.add(radio3);
		mainPanel.add(buttonsPanel);

		this.add(mainScPane);

		this.pack();
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
		if (((JRadioButton) e.getSource()).getName().compareTo("LOW") == 0) {
			quality = "LOW";
		} else if (((JRadioButton) e.getSource()).getName().compareTo("MEDIUM") == 0) {
			quality = "MEDIUM";
		} else {
			quality = "HIGH";
		}
	}

}
