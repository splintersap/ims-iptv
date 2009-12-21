package pl.edu.agh.iptv.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.utils.SubstanceStripingUtils;

public class MenuCellRenderer extends JPanel implements ListCellRenderer {

	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	public MenuCellRenderer() {
		super(new BorderLayout());
		// this.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR,
		// 1.0);
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
			ratingLabel.setForeground(new ColorUIResource(Color.BLACK));
			titleLabel.setForeground(new ColorUIResource(Color.BLACK));
			setBackground(list.getSelectionBackground());

		} else {
			if (SubstanceLookAndFeel.isCurrentLookAndFeel())
				SubstanceStripingUtils
						.applyStripedBackground(list, index, this);
			this.setForeground(list.getForeground());
		}

		return this;

	}

}
