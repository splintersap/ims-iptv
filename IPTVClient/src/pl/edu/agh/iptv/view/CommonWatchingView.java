package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import pl.edu.agh.iptv.view.components.MyCalendar;
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
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("CANCEL");
	
	private MyCalendar myCalendar;

	JCalendar calendar = new JCalendar();

	public CommonWatchingView(JFrame mainFrame, MoviesTab moviesTab,
			String selected) {
		super(mainFrame);
		this.setTitle("Common watching");
		JList moviesList = new JList(moviesTab.getAllMovies().toArray());
		moviesList.setCellRenderer(new MenuCellRenderer());
		JScrollPane moviesSC = new JScrollPane(moviesList);

		JPanel descPane = new JPanel();
		descPane.setLayout(new BorderLayout());

		JPanel sDesc = new JPanel(new BorderLayout());
		sDesc.setPreferredSize(new Dimension(100, 70));		
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

		JPanel buttonsP = new JPanel(new FlowLayout());
		okButton.setName("COMMON_OK");
		cancelButton.setName("COMMON_CANCEL");
		buttonsP.add(okButton);
		buttonsP.add(cancelButton);
		
		descPane.add(sDesc, BorderLayout.NORTH);
		descPane.add(datePanel, BorderLayout.CENTER);
		descPane.add(buttonsP, BorderLayout.SOUTH);			

		this.add(moviesSC, BorderLayout.WEST);
		this.add(descPane, BorderLayout.EAST);

		this.pack();
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (((JButton) e.getSource()).getName().compareTo("START") == 0) {
			myCalendar = new MyCalendar(calendar, this);
		} else if (((JButton) e.getSource()).getName().compareTo("CALENDAR_OK") == 0) {

				Integer day = new Integer(calendar.getDayChooser().getDay());
				this.dayS.setText(day.toString());

				Integer month = new Integer(calendar.getMonthChooser()
						.getMonth() + 1);
				this.monthS.setText(month.toString());

				Integer year = new Integer(calendar.getYearChooser().getYear());
				this.yearS.setText(year.toString());

				myCalendar.dispose();

		} else if (((JButton) e.getSource()).getName().compareTo(
				"CALENDAR_CANCEL") == 0) {
			myCalendar.dispose();
		}
	}

}
