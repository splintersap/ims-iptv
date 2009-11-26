package pl.edu.agh.iptv.view.components;

import java.awt.Color;

import javax.swing.ImageIcon;

public class ListItem {
	private Color color;
	private String value;
	private String uri;
	private ImageIcon image;

	public ListItem(Color c, String s) {
		color = c;
		value = s;
	}

	public ListItem(Color c, String s, String uri, ImageIcon i) {
		color = c;
		value = s;
		image = i;
	}

	public Color getColor() {
		return color;
	}

	public String getValue() {
		return value;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

}
