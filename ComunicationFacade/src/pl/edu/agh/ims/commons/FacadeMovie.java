package pl.edu.agh.ims.commons;

public class FacadeMovie {
	private String title;
	private String category;
	private String description;
	private String director;
	private Integer userRating;
	private Double allUsersRating;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getUserRating() {
		return userRating;
	}

	public void setUserRating(int userRating) {
		this.userRating = userRating;
	}

	public Double getAllUsersRating() {
		return allUsersRating;
	}

	public void setAllUsersRating(Double allUsersRating) {
		this.allUsersRating = allUsersRating;
	}

	public FacadeMovie(String title, String category, String description,
			String director, Integer userRating, Double allUsersRating) {
		this.title = title;
		this.category = category;
		this.description = description;
		this.director = director;
		this.userRating = userRating;
		this.allUsersRating = allUsersRating;
	}

}