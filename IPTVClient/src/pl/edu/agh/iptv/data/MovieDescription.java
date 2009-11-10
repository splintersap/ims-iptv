package pl.edu.agh.iptv.data;

import java.util.Date;

public class MovieDescription {
	private String quality;
	private long price;
	private Date data;
	private boolean isOrdered;

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public MovieDescription(String quality, long price, Date data, boolean isOrdered) {
		this.quality = quality;
		this.price = price;
		this.data = data;
		this.isOrdered = isOrdered;
	}

}
