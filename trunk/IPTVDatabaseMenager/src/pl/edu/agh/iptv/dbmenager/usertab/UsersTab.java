package pl.edu.agh.iptv.dbmenager.usertab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.persistence.Query;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import pl.edu.agh.iptv.dbmenager.main.Application;
import pl.edu.agh.iptv.dbmenager.movietab.MovieTab;
import pl.edu.agh.iptv.persistence.User;

public class UsersTab extends JPanel {

	private static final long serialVersionUID = 1390553054472399406L;

	private UserTableModel model;

	private JTable table;
	
	public UsersTab() {
		super(new BorderLayout());
		
		Query query = Application.getEntityMenager().createQuery("FROM User");
		List<User> movieList = query.getResultList();
		
		model = new UserTableModel(movieList, Application.getEntityMenager());
		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
	}
}
