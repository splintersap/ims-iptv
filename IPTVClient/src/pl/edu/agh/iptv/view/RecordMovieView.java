package pl.edu.agh.iptv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;

public class RecordMovieView extends JDialog implements ActionListener {

	private Date startDate;
	private Date endDate;

	private JTextField dayS = new JTextField(2);
	private JTextField monthS = new JTextField(2);
	private JTextField yearS = new JTextField(4);
	private JTextField hourS = new JTextField(2);
	private JTextField minuteS = new JTextField(2);

	private JTextField dayE = new JTextField(2);
	private JTextField monthE = new JTextField(2);
	private JTextField yearE = new JTextField(4);
	private JTextField hourE = new JTextField(2);
	private JTextField minuteE = new JTextField(2);

	private MyCalendar myCalendar;

	JCalendar calendar = new JCalendar();

	private boolean isStart;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecordMovieView(ActionListener listener, JFrame parent,
			String movieTitle) {
		super(parent);
		this.setTitle("Recording settings");

		this.setPreferredSize(new Dimension(550, 150));
		this.setMaximumSize(new Dimension(550, 150));
		this.setMinimumSize(new Dimension(550, 150));

		JPanel startP = new JPanel();
		startP.setLayout(new FlowLayout());
		startP.add(new JLabel("Start date:"));
		startP.add(new JLabel());
		startP.add(new JLabel("day"));
		startP.add(dayS);
		startP.add(new JLabel("month"));
		startP.add(monthS);
		startP.add(new JLabel("year"));
		startP.add(yearS);
		startP.add(new JLabel());
		JButton calendarBS = new JButton(new ImageIcon(
				"images/recording/calendar.gif"));
		calendarBS.addActionListener(this);
		calendarBS.setName("START");
		startP.add(calendarBS);
		startP.add(new JLabel());
		startP.add(new JLabel("Start time:"));
		startP.add(new JLabel());
		startP.add(new JLabel("hour"));
		startP.add(hourS);
		startP.add(new JLabel("minute"));
		startP.add(minuteS);

		JPanel endP = new JPanel();
		endP.setLayout(new FlowLayout());
		endP.add(new JLabel("  End date:"));
		endP.add(new JLabel());
		endP.add(new JLabel("day"));
		endP.add(dayE);
		endP.add(new JLabel("month"));
		endP.add(monthE);
		endP.add(new JLabel("year"));
		endP.add(yearE);
		endP.add(new JLabel());
		JButton calendarBE = new JButton(new ImageIcon(
				"images/recording/calendar.gif"));
		calendarBE.addActionListener(this);
		calendarBE.setName("END");
		endP.add(calendarBE);
		endP.add(new JLabel());
		endP.add(new JLabel("  End time:"));
		endP.add(new JLabel());
		endP.add(new JLabel("hour"));
		endP.add(hourE);
		endP.add(new JLabel("minute"));
		endP.add(minuteE);

		JPanel buttonsP = new JPanel();
		buttonsP.setLayout(new FlowLayout());
		JButton ok = new JButton("OK");
		ok.setName("OK");
		ok.addActionListener(listener);
		JButton cancel = new JButton("CANCEL");
		cancel.setName("CANCEL");
		cancel.addActionListener(listener);
		buttonsP.add(ok);
		buttonsP.add(cancel);

		this.add(startP, BorderLayout.NORTH);
		this.add(endP, BorderLayout.CENTER);
		this.add(buttonsP, BorderLayout.SOUTH);

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (((JButton) e.getSource()).getName().compareTo("START") == 0) {
			isStart = true;
			myCalendar = new MyCalendar();
		} else if (((JButton) e.getSource()).getName().compareTo("END") == 0) {
			isStart = false;
			myCalendar = new MyCalendar();
		} else if (((JButton) e.getSource()).getName().compareTo("CALENDAR_OK") == 0) {
			if (isStart) {

				Integer day = new Integer(calendar.getDayChooser().getDay());
				this.dayS.setText(day.toString());
				this.dayE.setText(day.toString());

				Integer month = new Integer(calendar.getMonthChooser()
						.getMonth() + 1);
				this.monthS.setText(month.toString());
				this.monthE.setText(month.toString());

				Integer year = new Integer(calendar.getYearChooser().getYear());
				this.yearS.setText(year.toString());
				this.yearE.setText(year.toString());

				myCalendar.dispose();
			} else {
				Integer day = new Integer(calendar.getDayChooser().getDay());
				this.dayE.setText(day.toString());

				Integer month = new Integer(calendar.getMonthChooser()
						.getMonth() + 1);
				this.monthE.setText(month.toString());

				Integer year = new Integer(calendar.getYearChooser().getYear());
				this.yearE.setText(year.toString());
				myCalendar.dispose();
			}
		} else if (((JButton) e.getSource()).getName().compareTo(
				"CALENDAR_CANCEL") == 0) {
			myCalendar.dispose();
		}
	}

	public String getDayS() {
		return dayS.getText();
	}

	public String getMonthS() {
		return monthS.getText();
	}

	public String getYearS() {
		return yearS.getText();
	}

	public String getHourS() {
		return hourS.getText();
	}

	public String getMinuteS() {
		return minuteS.getText();
	}

	public String getDayE() {
		return dayE.getText();
	}

	public String getMonthE() {
		return monthE.getText();
	}

	public String getYearE() {
		return yearE.getText();
	}

	public String getHourE() {
		return hourE.getText();
	}

	public String getMinuteE() {
		return minuteE.getText();
	}

	private class MyCalendar extends JDialog {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyCalendar() {
			super(RecordMovieView.this);

			this.add(calendar, BorderLayout.CENTER);
			JPanel buttonsP = new JPanel();
			buttonsP.setLayout(new FlowLayout());
			buttonsP.add(new JLabel());
			JButton ok = new JButton("OK");
			ok.setName("CALENDAR_OK");
			ok.addActionListener(RecordMovieView.this);
			buttonsP.add(ok);
			buttonsP.add(new JLabel());
			JButton cancel = new JButton("CANCEL");
			cancel.setName("CALENDAR_CANCEL");
			cancel.addActionListener(RecordMovieView.this);
			buttonsP.add(cancel);
			this.add(buttonsP, BorderLayout.SOUTH);
			this.pack();
			this.setVisible(true);

		}

	}

}
