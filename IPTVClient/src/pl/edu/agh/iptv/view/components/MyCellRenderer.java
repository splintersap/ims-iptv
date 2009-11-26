package pl.edu.agh.iptv.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class MyCellRenderer extends JPanel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyCellRenderer() {
		// Don't paint behind the component
		super(new FlowLayout(FlowLayout.LEFT));
		setOpaque(true);
	}

	// Set the attributes of the
	// class and return a reference
	public Component getListCellRendererComponent(JList list, Object value, // value
			// to
			// display
			int index, // cell index
			boolean iss, // is selected
			boolean chf) // cell has focus?
	{
		 
		this.removeAll();
		this.add(new JLabel(((ListItem) value).getImage()), BorderLayout.WEST);
		this.add(new JLabel(((ListItem) value).getValue()), BorderLayout.EAST);
		
		if (iss) {
			 setBorder(BorderFactory.createLineBorder(Color.gray, 2));			
		} else {
			setBorder(BorderFactory.createLineBorder(list.getBackground(), 2));
		}

		return this;
	}
}
