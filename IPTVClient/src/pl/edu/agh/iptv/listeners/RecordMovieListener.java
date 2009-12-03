package pl.edu.agh.iptv.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;

import pl.edu.agh.iptv.IPTVClient;
import pl.edu.agh.iptv.view.RecordMovieView;
import pl.edu.agh.iptv.view.components.MenuListItem;
import pl.edu.agh.iptv.view.movies.MoviesTab;

public class RecordMovieListener implements ActionListener {

	private IPTVClient iptvClient;

	private JFrame parent;

	private MoviesTab moviesTab;

	private RecordMovieView recM;

	public RecordMovieListener(IPTVClient iptvClient, JFrame parent,
			MoviesTab moviesTab) {
		this.iptvClient = iptvClient;
		this.parent = parent;
		this.moviesTab = moviesTab;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {

			if (((JButton) e.getSource()).getName().compareTo("RECORD") == 0) {
				recM = new RecordMovieView(this, parent,
						(MenuListItem) moviesTab.getAllMoviesList()
								.getSelectedValue());
			} else if (((JButton) e.getSource()).getName().compareTo("OK") == 0) {
				
				Calendar startCalendar = new GregorianCalendar(new Integer(recM.getYearS()), new Integer(recM.getMonthS()) - 1, new Integer(recM
						.getDayS()), new Integer(recM.getHourS()), new Integer(recM.getMinuteS()));
				
				Calendar endCalendar = new GregorianCalendar(new Integer(recM.getYearE()),
						new Integer(recM.getMonthE()) - 1, new Integer(recM
								.getDayE()), new Integer(recM.getHourE()),
						new Integer(recM.getMinuteE()));
				
//				Date startDate = new Date(new Integer(recM.getYearS()),
//						new Integer(recM.getMonthS()), new Integer(recM
//								.getDayS()), new Integer(recM.getHourS()),
//						new Integer(recM.getMinuteS()));
//				Date endDate = new Date(new Integer(recM.getYearE()),
//						new Integer(recM.getMonthE()), new Integer(recM
//								.getDayE()), new Integer(recM.getHourE()),
//						new Integer(recM.getMinuteE()));

				MenuListItem movie = (MenuListItem) moviesTab
						.getAllMoviesList().getSelectedValue();

				iptvClient.recordMovie(startCalendar.getTime(), endCalendar.getTime(), movie.getTitle());

				recM.dispose();

			} else if (((JButton) e.getSource()).getName().compareTo("CANCEL") == 0) {
				recM.dispose();
			}

		}
	}
}
