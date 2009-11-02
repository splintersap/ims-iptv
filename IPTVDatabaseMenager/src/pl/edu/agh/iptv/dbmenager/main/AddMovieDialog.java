package pl.edu.agh.iptv.dbmenager.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pl.edu.agh.iptv.dbmenager.persistence.Category;
import pl.edu.agh.iptv.dbmenager.persistence.Movie;
import pl.edu.agh.iptv.dbmenager.persistence.Quality;

public class AddMovieDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -4290000270971157783L;
	final JTextField titleTextFiled;
	final JTextField directorTextFiled;
	final JComboBox categoryComboBox;
	final JTextField moviePathTextField;
	final JTextArea descriptionTextArea;
	final JButton cancelButton;
	final JButton saveButton;
	QualitySpinner[] spinners;

	public AddMovieDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		JPanel informationPanel = new JPanel();
		informationPanel.setLayout(new GridLayout(4, 2, 10, 6));
		JLabel titleLabel = new JLabel("Title");
		titleTextFiled = new JTextField(20);
		informationPanel.add(titleLabel);
		informationPanel.add(titleTextFiled);

		JLabel directorLabel = new JLabel("Director");
		directorTextFiled = new JTextField(20);
		informationPanel.add(directorLabel);
		informationPanel.add(directorTextFiled);

		JLabel categoryLabel = new JLabel("Category");
		categoryComboBox = new JComboBox(Category.values());
		informationPanel.add(categoryLabel);
		informationPanel.add(categoryComboBox);

		JPanel moviePathPanel = new JPanel();
		moviePathPanel.setLayout(new BoxLayout(moviePathPanel,
				BoxLayout.LINE_AXIS));
		JLabel moviePathLabel = new JLabel("Movie Path");
		final JButton browseButton = new JButton("Browse");
		final JFileChooser fc = new JFileChooser();

		browseButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(browseButton);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					moviePathTextField.setText(file.getAbsolutePath());
					// This is where a real application would open the file.
					System.out.println("Opening: " + file.getName());
				} else {
					System.out.println("Open command cancelled by user.");
				}

			}
		});
		moviePathTextField = new JTextField(20);
		informationPanel.add(moviePathLabel);
		moviePathPanel.add(moviePathTextField);
		moviePathPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		moviePathPanel.add(browseButton);
		informationPanel.add(moviePathPanel);

		add(informationPanel);

		add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(new BorderLayout());
		JLabel descriptionLabel = new JLabel("Description");
		descriptionTextArea = new JTextArea(5, 20);
		descriptionPanel.add(descriptionLabel, BorderLayout.PAGE_START);
		descriptionPanel.add(descriptionTextArea, BorderLayout.CENTER);

		add(descriptionPanel);
		add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel pricesPanel = new JPanel();
		pricesPanel.setLayout(new GridLayout(Quality.values().length, 2));

		spinners = new QualitySpinner[Quality.values().length];

		int i = 0;
		for (Quality quality : Quality.values()) {

			spinners[i] = new QualitySpinner();
			spinners[i].setQuality(quality);
			JLabel qualityLabel = new JLabel(quality.name());
			pricesPanel.add(qualityLabel);
			pricesPanel.add(spinners[i]);
			i++;
		}

		add(pricesPanel);
		add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel buttonPanel = new JPanel();
		saveButton = new JButton("Save");
		cancelButton = new JButton("Cancel");

		cancelButton.addActionListener(this);
		saveButton.addActionListener(this);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		add(buttonPanel);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(cancelButton)) {
			this.dispose();
		} else if (arg0.getSource().equals(saveButton)) {
			Movie movie = new Movie(titleTextFiled.getText(),
					moviePathTextField.getText());
			movie
					.setCategory((Category) categoryComboBox
							.getSelectedItem());
			movie.setDirector(directorTextFiled.getText());
			movie.setDescription(descriptionTextArea.getText());
			for (QualitySpinner spinner : spinners) {
				movie.addMoviePayment(Double.valueOf(
						spinner.getValue().toString()).longValue(), spinner
						.getQuality());
			}

			Starter.persistMovie(movie);
			this.dispose();
		}
	}
}
