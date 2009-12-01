package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import pl.edu.agh.iptv.view.components.MenuCellRenderer;
import pl.edu.agh.iptv.view.components.ViewWithCalendar;
import pl.edu.agh.iptv.view.movies.MoviesTab;

import com.toedter.calendar.JCalendar;

public class CommonWatchingView extends ViewWithCalendar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField dayS = new JTextField(2);
	private JTextField monthS = new JTextField(2);
	private JTextField yearS = new JTextField(4);
	private JTextField hourS = new JTextField(2);
	private JTextField minuteS = new JTextField(2);

	JCalendar calendar = new JCalendar();

	public CommonWatchingView(JFrame mainFrame, MoviesTab moviesTab,
			String selected) {
		super(mainFrame);
		JList moviesList = new JList(moviesTab.getAllMovies().toArray());
		moviesList.setCellRenderer(new MenuCellRenderer());
		JScrollPane moviesSC = new JScrollPane(moviesList);

		JPanel descPane = new JPanel();
		descPane.setLayout(new BorderLayout());

		JPanel sDesc = new JPanel(new BorderLayout());
		sDesc.add(new JLabel("Common watching"), BorderLayout.NORTH);
		sDesc.add(new JLabel("Users to invite: " + selected),
				BorderLayout.CENTER);

		JPanel datePanel = new JPanel(new BorderLayout());

		JPanel startP1 = new JPanel();
		startP1.setLayout(new FlowLayout());
		startP1.add(new JLabel("Start date:"));
		startP1.add(new JLabel());
		startP1.add(new JLabel("day"));
		startP1.add(dayS);
		startP1.add(new JLabel("month"));
		startP1.add(monthS);
		startP1.add(new JLabel("year"));
		startP1.add(yearS);
		startP1.add(new JLabel());
		JButton calendarBS = new JButton(new ImageIcon(
				"images/recording/calendar.gif"));
		calendarBS.addActionListener(this);
		calendarBS.setName("START");
		startP1.add(calendarBS);

		JPanel startP2 = new JPanel();
		startP2.add(new JLabel("Start time:"));
		startP2.add(new JLabel());
		startP2.add(new JLabel("hour"));
		startP2.add(hourS);
		startP2.add(new JLabel("minute"));
		startP2.add(minuteS);

		datePanel.add(startP1, BorderLayout.NORTH);
		datePanel.add(startP2, BorderLayout.CENTER);

		descPane.add(sDesc, BorderLayout.NORTH);
		descPane.add(datePanel, BorderLayout.CENTER);

		this.add(moviesSC, BorderLayout.WEST);
		this.add(descPane, BorderLayout.EAST);

		this.pack();
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
