package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.agh.iptv.view.components.MenuCellRenderer;
import pl.edu.agh.iptv.view.components.MenuListItem;
import pl.edu.agh.iptv.view.components.MyCalendar;
import pl.edu.agh.iptv.view.components.ViewWithCalendar;
import pl.edu.agh.iptv.view.movies.MoviesTab;

import com.toedter.calendar.JCalendar;

public class CommonWatchingView extends ViewWithCalendar implements
		ListSelectionListener {

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

	private Calendar startDate;

	private Map<String, String> urisToUsers = null;

	JCalendar calendar = new JCalendar();

	private String movieToWatch = null;

	private String date;

	private JLabel movieToWatchLabel = new JLabel(
			"Movie to watch: choose the movie");

	public CommonWatchingView(JFrame mainFrame, MoviesTab moviesTab,
			Map<String, String> urisToUsers, String selected) {
		super(mainFrame);
		this.setTitle("Common watching");
		this.urisToUsers = urisToUsers;
		JList moviesList = new JList(moviesTab.getAllMovies().toArray());
		moviesList.setCellRenderer(new MenuCellRenderer());
		moviesList.addListSelectionListener(this);
		JScrollPane moviesSC = new JScrollPane(moviesList);

		JPanel descPane = new JPanel();
		descPane.setLayout(new BorderLayout());
		descPane.setMinimumSize(new Dimension(200, 100));

		JPanel sDesc = new JPanel(new BorderLayout());
		// sDesc.setPreferredSize(new Dimension(100, 70));
		sDesc.add(new JLabel("Users to invite: " + selected),
				BorderLayout.CENTER);

		sDesc.add(movieToWatchLabel, BorderLayout.SOUTH);

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

		JScrollPane descScrollPane = new JScrollPane(descPane);

		this.add(moviesSC, BorderLayout.WEST);
		this.add(descScrollPane, BorderLayout.CENTER);

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

			Integer month = new Integer(
					calendar.getMonthChooser().getMonth());
			this.monthS.setText(month.toString());

			Integer year = new Integer(calendar.getYearChooser().getYear());
			this.yearS.setText(year.toString());

			startDate = new GregorianCalendar(year, month, day);

			date = day + "." + month + "." + year + " - ";

			myCalendar.dispose();

		} else if (((JButton) e.getSource()).getName().compareTo(
				"CALENDAR_CANCEL") == 0) {
			myCalendar.dispose();
		}
	}

	public JButton getOkButton() {
		return this.okButton;
	}

	public JButton getCancelButton() {
		return this.cancelButton;
	}

	public Map<String, String> getUrisToUsers() {
		return this.urisToUsers;
	}

	public String getMovieToWatch() {
		return this.movieToWatch;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (e.getValueIsAdjusting() == true) {
			return;
		}

		this.movieToWatch = ((MenuListItem) ((JList) e.getSource())
				.getSelectedValue()).getTitle();
		this.movieToWatchLabel.setText("Movie to watch: " + this.movieToWatch);
	}

	public Date getDate() {
		return this.startDate.getTime();
	}

	public String getStartDate() {
		return this.date;
	}

	public Integer getHour() {
		return new Integer(hourS.getText());
	}

	public Integer getMinute() {
		return new Integer(minuteS.getText());
	}

}
