package pl.edu.agh.ims.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonMovie {
	private String title;
	private String category;
	private String description;
	private String director;
	private Integer userRating;
	private Double allUsersRating;
	private List<CommonComment> commentList = new ArrayList<CommonComment>();
	private List<CommonMovieDescription> movieDescriptionList = new ArrayList<CommonMovieDescription>();

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
	
	public void addCommonComment(CommonComment comment) {
		commentList.add(comment);
	}

	public void addCommonMovieDescription(CommonMovieDescription movieDescription) {
		movieDescriptionList.add(movieDescription);
	}
	
	@Override
	public String toString() {
		return title + ", " + director + ", " + category.toString();
	};
	
	public CommonMovie(String title, String category, String description,
			String director, Integer userRating, Double allUsersRating) {
		this.title = title;
		this.category = category;
		this.description = description;
		this.director = director;
		this.userRating = userRating;
		this.allUsersRating = allUsersRating;
	}

}
