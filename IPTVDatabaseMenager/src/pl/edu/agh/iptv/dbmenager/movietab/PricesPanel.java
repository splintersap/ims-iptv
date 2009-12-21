package pl.edu.agh.iptv.dbmenager.movietab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import pl.edu.agh.iptv.dbmenager.main.Application;
import pl.edu.agh.iptv.persistence.MoviePayment;
import pl.edu.agh.iptv.persistence.OrderedMovie;
import pl.edu.agh.iptv.persistence.User;

public class PricesPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 8405173028506601942L;
	protected InformationTreeNode rootNode;
	protected DefaultTreeModel treeModel;
	protected JTree tree;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static String ADD_COMMAND = "add";
	private static String REMOVE_COMMAND = "remove";
	private JButton addLeafButton;
	private JButton removeLeafButton;

	PricesPanel() {

		super();
		setLayout(new BorderLayout());
		rootNode = new InformationTreeNode("Prices");
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.addTreeSelectionListener(new TreeSelectionListener() {		

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("Node selected");
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();

				if (node == null) {
					// Nothing is selected.
					return;
				}

				if (node.getLevel() == 1) {
					addLeafButton.setEnabled(true);
					removeLeafButton.setEnabled(false);
				} else {
					addLeafButton.setEnabled(false);
					removeLeafButton.setEnabled(true);
				}

			}
		});

		JPanel buttonPanel = new JPanel();
		addLeafButton = new JButton("Add");
		addLeafButton.setActionCommand(ADD_COMMAND);
		addLeafButton.addActionListener(this);
		addLeafButton.setEnabled(false);
		removeLeafButton = new JButton("Remove");
		removeLeafButton.setActionCommand(REMOVE_COMMAND);
		removeLeafButton.addActionListener(this);
		removeLeafButton.setEnabled(false);
		buttonPanel.add(addLeafButton);
		buttonPanel.add(removeLeafButton);
		JScrollPane scroll = new JScrollPane(tree);
		scroll.setPreferredSize(new Dimension(50, 50));
		add(scroll, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);

	}

	public void createNodes(List<MoviePayment> moviePayments) {
		InformationTreeNode category = null;
		InformationTreeNode orderNode = null;
		rootNode.removeAllChildren();
		for (MoviePayment moviePayment : moviePayments) {
			category = new InformationTreeNode(moviePayment);
			Application.getEntityMenager().refresh(moviePayment);
			category.setObject(moviePayment);
			rootNode.add(category);
			for (OrderedMovie orderedMovie : moviePayment.getOrderedMovieList()) {
				orderNode = new InformationTreeNode(orderedMovie.getUser());
				orderNode.setObject(orderedMovie);
				category.add(orderNode);
			}
		}
		SwingUtilities.updateComponentTreeUI(tree);

		this.repaint();

	}

	public void removeCurrentNode() {
		System.out.println("Jestem w remove node");
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			InformationTreeNode currentNode = (InformationTreeNode) (currentSelection
					.getLastPathComponent());
			InformationTreeNode parent = (InformationTreeNode) (currentNode
					.getParent());
			if (parent != null) {
				treeModel.removeNodeFromParent(currentNode);

				System.out.println("Tutaj");

				OrderedMovie orderedMovie = (OrderedMovie) currentNode
						.getObject();
				System.out.println(orderedMovie.getId());
				// em.persist(orderedMovie);
				EntityManager em = Application.getEntityMenager();
				em.getTransaction().begin();
				OrderedMovie oMovie = em.find(OrderedMovie.class, orderedMovie
						.getId());
				oMovie.getUser().getOrderedMoviesList().remove(oMovie);
				oMovie.getMoviePayment().getOrderedMovieList().remove(oMovie);
				em.remove(oMovie);
				// em.remove(orderedMovie);
				em.getTransaction().commit();

				return;
			}
		}

		// Either there was no selection, or the root was selected.
		toolkit.beep();
	}


	public InformationTreeNode addUser(OrderedMovie orederdMovie) {
		
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode) (parentPath
					.getLastPathComponent());
		}
		
		InformationTreeNode childNode = new InformationTreeNode(orederdMovie.getUser().getSip());
		childNode.setObject(orederdMovie);

		// It is key to invoke this on the TreeModel, and NOT
		// DefaultMutableTreeNode
		treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());

		// Make sure the user can see the lovely new node.
		
		tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		
		return childNode;
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (ADD_COMMAND.equals(command)) {
			TreePath currentSelection = tree.getSelectionPath();
			MoviePayment moviePayment= null;
			if (currentSelection != null) {
				InformationTreeNode currentNode = (InformationTreeNode) (currentSelection
						.getLastPathComponent());
				moviePayment = (MoviePayment) currentNode.getObject();

			}
			new UserListDialog(this, moviePayment);

		} else if (REMOVE_COMMAND.equals(command)) {
			// Remove button clicked
			removeCurrentNode();
		}
	}

	public void addUsers(List<User> userList) {
		System.out.println("Adding users");

		TreePath currentSelection = tree.getSelectionPath();
		MoviePayment moviePayment = null;

		if (currentSelection != null) {
			InformationTreeNode currentNode = (InformationTreeNode) (currentSelection
					.getLastPathComponent());
			moviePayment = (MoviePayment) currentNode.getObject();

		}

		EntityManager em = Application.getEntityMenager();
		em.getTransaction().begin();

		em.persist(moviePayment);
		for (User user : userList) {
			em.persist(user);
			OrderedMovie orderMovie = user.addOrderedMovie(moviePayment.getMovie(), moviePayment
					.getQuality());
			addUser(orderMovie);
		}
		em.getTransaction().commit();

	}
}
