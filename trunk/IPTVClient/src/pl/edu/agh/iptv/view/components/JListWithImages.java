package pl.edu.agh.iptv.view.components;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import sun.swing.DefaultLookup;

public class JListWithImages extends JList {

	public JListWithImages() {
		setCellRenderer(new CustomCellRenderer());
	}

	private void setupList() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		Vector vector = new Vector();
		panel.setForeground(Color.black);
		panel.setBackground(Color.white);

		// first line
		JPanel jp1 = new JPanel();
		jp1.add(new JLabel(new ImageIcon("images/chat/available.gif")));
		jp1.add(new JLabel("A line for Gumby"));
		jp1.add(new JLabel(new ImageIcon("images/chat/available.gif")));

		// second line
		JPanel jp2 = new JPanel();
		jp2.add(new JLabel(new ImageIcon("images/chat/available.gif")));
		jp2.add(new JLabel("Another line for Gumby"));
		jp2.add(new JLabel(new ImageIcon("images/chat/available.gif")));

		vector.addElement(jp1);
		vector.addElement(jp2);

		JListWithImages jlwi = new JListWithImages();
		jlwi.setListData(vector);

		panel.add(jlwi);
		frame.getContentPane().add(panel);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}

	class CustomCellRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel component = (JLabel) value;
			setComponentOrientation(list.getComponentOrientation());

	        Color bg = null;
	        Color fg = null;

	        JList.DropLocation dropLocation = list.getDropLocation();
	        if (dropLocation != null
	                && !dropLocation.isInsert()
	                && dropLocation.getIndex() == index) {

	            bg = DefaultLookup.getColor(component, ui, "List.dropCellBackground");
	            fg = DefaultLookup.getColor(component, ui, "List.dropCellForeground");

	            isSelected = true;
	        }

		if (isSelected) {
	            setBackground(bg == null ? list.getSelectionBackground() : bg);
		    setForeground(fg == null ? list.getSelectionForeground() : fg);
		}
		else {
		    setBackground(list.getBackground());
		    setForeground(list.getForeground());
		}
	        
		if (value instanceof Icon) {
		}
		else {
		}

		setEnabled(list.isEnabled());
		setFont(list.getFont());
	        
	        Border border = null;
	        if (cellHasFocus) {
	            if (isSelected) {
	                border = DefaultLookup.getBorder(component, ui, "List.focusSelectedCellHighlightBorder");
	            }
	            if (border == null) {
	                border = DefaultLookup.getBorder(component, ui, "List.focusCellHighlightBorder");
	            }
	        } else {
//	            border = getNoFocusBorder();
	        }
		setBorder(border);

			return component;
		}
	}
}
