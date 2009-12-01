package pl.edu.agh.iptv.view.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JCalendar;

public class MyCalendar extends JDialog {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyCalendar(JCalendar calendar, ViewWithCalendar parent) {
			super(parent);

			this.add(calendar, BorderLayout.CENTER);
			JPanel buttonsP = new JPanel();
			buttonsP.setLayout(new FlowLayout());
			buttonsP.add(new JLabel());
			JButton ok = new JButton("OK");
			ok.setName("CALENDAR_OK");
			ok.addActionListener(parent);
			buttonsP.add(ok);
			buttonsP.add(new JLabel());
			JButton cancel = new JButton("CANCEL");
			cancel.setName("CALENDAR_CANCEL");
			cancel.addActionListener(parent);
			buttonsP.add(cancel);
			this.add(buttonsP, BorderLayout.SOUTH);
			this.pack();
			this.setVisible(true);

		}

	}
