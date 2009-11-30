package pl.edu.agh.iptv.view.components;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.jvnet.substance.api.renderers.SubstanceDefaultListCellRenderer;

public class MenuCellRenderer extends JPanel implements ListCellRenderer  {

	/**
	 * 
	 */

	public MenuCellRenderer() {
		super(new BorderLayout());
		setOpaque(true);
	}

	private static final long serialVersionUID = 1514467862671434333L;

	private final static ImageIcon starIcon = new ImageIcon(
			"images/rating/full_star_small.gif");
	private final static ImageIcon recordIcon = new ImageIcon(
			"images/menu/disk.gif");
	private final static ImageIcon broadcastIcon = new ImageIcon(
			"images/menu/broadcast.gif");
	private final static ImageIcon vodIcon = new ImageIcon(
			"images/menu/vod.gif");

	@Override
	public Component getListCellRendererComponent(JList list, Object value, // value
			// to display
			int index, // cell index
			boolean isSelected, // is selected
			boolean hasFocus) // cell has focus?
	{
		this.removeAll();
		MenuListItem listItem = (MenuListItem) value;
		JLabel starLabel = new JLabel(starIcon);
		JLabel categoryLabel = null;
		if (listItem.getCategory() == MenuListItem.VOD) {
			categoryLabel = new JLabel(vodIcon);
		} else if (listItem.getCategory() == MenuListItem.BROADCAST) {
			categoryLabel = new JLabel(broadcastIcon);
		} else if (listItem.getCategory() == MenuListItem.RECORDING) {
			categoryLabel = new JLabel(recordIcon);
		}

		if (!listItem.isOrdered) {
			categoryLabel.setEnabled(false);
		}

		JPanel titlePanel = new JPanel();

		JLabel titleLabel = new JLabel(listItem.getTitle());
		titlePanel.add(categoryLabel);
		titlePanel.add(titleLabel);

		JPanel ratingPanel = new JPanel();

		JLabel ratingLabel = new JLabel(listItem.getRating().toString());
		ratingPanel.add(ratingLabel);

		if (listItem.getRating() == 0.0) {
			starLabel.setEnabled(false);
		}

		ratingPanel.add(starLabel);

		this.add(titlePanel, BorderLayout.WEST);
		this.add(ratingPanel, BorderLayout.EAST);

		if (isSelected) {
			// this.setForeground(list.getSelectionForeground());
			setBackground(list.getSelectionBackground());
		} else {
			
			setBackground(list.getBackground());
			this.setForeground(list.getForeground());
		}

		return this;
		/*setText("sadfasdfasd");
		setIcon(broadcastIcon);
		//setLayout(new BorderLayout());
		add(new JLabel("asdfasdf"), BorderLayout.PAGE_END);
		//JLabel test = new JLabel("Test");
		//add(test);
		return this;*/
	}

}
