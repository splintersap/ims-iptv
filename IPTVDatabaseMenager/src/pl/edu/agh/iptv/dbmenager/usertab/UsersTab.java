package pl.edu.agh.iptv.dbmenager.usertab;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.persistence.Query;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import pl.edu.agh.iptv.dbmenager.main.Application;
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
		
		JPanel addUserPanel = new JPanel();
		JButton addUserButton = new JButton("Add");
		addUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = getParentFrame();
				new AddUserDialog(frame, UsersTab.this);
			}

			private JFrame getParentFrame() {
				Container container = UsersTab.this.getParent();
				
				while(!(container instanceof JFrame) ) {
					container = container.getParent();
				}
				
				return (JFrame) container;
			}});
		
		JButton removeUserButton = new JButton("Remove");
		removeUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = UsersTab.this.table.getSelectedRow();
				// not selected -1 
				if(selectedRow != -1) {
					model.removeUser(table.convertRowIndexToModel(selectedRow));
				}
			}});
		
		JPanel addRemoveUserPanel = new JPanel();
		addRemoveUserPanel.add(addUserButton);
		addRemoveUserPanel.add(removeUserButton);
		
		add(scrollPane, BorderLayout.CENTER);
		add(addRemoveUserPanel, BorderLayout.PAGE_END);
		
	}

	public void persistUser(User user) {
		Application.getEntityMenager().getTransaction().begin();
		Application.getEntityMenager().persist(user);
		Application.getEntityMenager().getTransaction().commit();
		model.addUser(user);
	}
}
