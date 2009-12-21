package pl.edu.agh.iptv.view.components;


public class MenuListItem {
	
	public static final int VOD = 0;
	public static final int BROADCAST = 1;
	public static final int RECORDING = 2;
	
	private String title;
	private Double rating;
	private int category;
	Boolean isOrdered;
	
	
	
	public String getTitle() {
		return title;
	}

	public Double getRating() {
		return rating;
	}

	public Integer getCategory() {
		return category;
	}
	
	public Boolean isOrdered() {
		return isOrdered;
	}

	public MenuListItem(String title, Double rating, String category, boolean isOrdered) {
		this.title = title;
		this.rating = rating;
		if("VOD".equals(category)) {
			this.category = VOD;
		} else if("BROADCAST".equals(category)) {
			this.category = BROADCAST;
		} else if("RECORDING".equals(category)) {
			this.category = RECORDING;
		}
		this.isOrdered = isOrdered;
	}
	
	
	
	
}
