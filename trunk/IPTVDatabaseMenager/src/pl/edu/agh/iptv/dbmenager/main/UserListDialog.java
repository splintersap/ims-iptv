package pl.edu.agh.iptv.dbmenager.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import pl.edu.agh.iptv.persistence.MoviePayment;
import pl.edu.agh.iptv.persistence.OrderedMovie;
import pl.edu.agh.iptv.persistence.User;

public class UserListDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -8495893989778979479L;

	private PricesPanel pricesPanel;
	MoviePayment moviePayment;
	private static String SELECT_COMMAND = "select";
	private static String CANCEL_COMMAND = "cancel";
	private JList list;

	public UserListDialog(PricesPanel pricesPanel, MoviePayment moviePayment) {
		this.pricesPanel = pricesPanel;
		this.moviePayment = moviePayment;
		setLayout(new BorderLayout());
		List<User> userList = getUsersFromDB();
		list = new JList(userList.toArray());
		add(list, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		JButton selectButton = new JButton("Select");
		selectButton.setActionCommand(SELECT_COMMAND);
		selectButton.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand(CANCEL_COMMAND);
		cancelButton.addActionListener(this);
		buttonPanel.add(selectButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.PAGE_END);

		pack();
		setLocationRelativeTo(null);

		setVisible(true);
	}

	private List<User> getUsersFromDB() {
		List<User> finalUserList = new ArrayList<User>();
		EntityManager em = Starter.getEntityMenager();
		em.getTransaction().begin();
		Query query = em.createQuery("FROM User");
		final List<User> userList = query.getResultList();
		
		if (moviePayment != null) {
			em.refresh(moviePayment);
			//MoviePayment mp = em.find(MoviePayment.class, moviePayment.getId());

			for (User user : userList) {
				OrderedMovie orderedMovie = moviePayment.getOrderByUser(user.getSip());
				if(orderedMovie == null)
				{
					finalUserList.add(user);
				}
			}

		}
		em.getTransaction().commit();
		

		return finalUserList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(SELECT_COMMAND)) {
			List<User> userList = new ArrayList<User>();
			Object[] tab = list.getSelectedValues();
			for (Object obj : tab) {
				if (obj instanceof User) {
					userList.add((User) obj);
				}
			}
			pricesPanel.addUsers(userList);
			dispose();
		} else if (e.getActionCommand().equals(CANCEL_COMMAND)) {
			dispose();
		}

	}

}
