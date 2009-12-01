package pl.edu.agh.iptv.view.components;

import java.awt.Dialog;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

public abstract class ViewWithCalendar extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewWithCalendar(Dialog parent) {
		super(parent);
	}

	public ViewWithCalendar(JFrame parent) {
		super(parent);
	}

}
