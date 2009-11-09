package pl.edu.agh.iptv.view.components;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class DropDownButtonImpl extends DropDownButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected JPopupMenu getPopupMenu() {
		// TODO Auto-generated method stub
		JPopupMenu menu = new JPopupMenu("Purchase");

		JMenuItem menuItem1 = new JMenuItem("Low");
		JMenuItem menuItem2 = new JMenuItem("Medium");
		JMenuItem menuItem3 = new JMenuItem("High");

		menu.add(menuItem1);
		menu.add(menuItem2);
		menu.add(menuItem3);

		return null;
	}

}
