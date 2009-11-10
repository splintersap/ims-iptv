package pl.edu.agh.iptv.data;

import java.util.ArrayList;
import java.util.List;

public class Movie {
	private String title;
	private String category;
	private String description;
	private String director;
	private Integer userRating;
	private Double allUsersRating;
	private List<Comment> commentList = new ArrayList<Comment>();
	private List<MovieDescription> movieDescriptionList = new ArrayList<MovieDescription>();

	public List<MovieDescription> getMovieDescriptionList() {
		return movieDescriptionList;
	}

	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}

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

	public void addCommonComment(Comment comment) {
		commentList.add(comment);
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public void addCommonMovieDescription(
			MovieDescription movieDescription) {
		movieDescriptionList.add(movieDescription);
	}

	@Override
	public String toString() {
		return title + ", " + director + ", " + category.toString();
	};

	public Movie(String title, String category, String description,
			String director, Integer userRating, Double allUsersRating) {
		this.title = title;
		this.category = category;
		this.description = description;
		this.director = director;
		this.userRating = userRating;
		this.allUsersRating = allUsersRating;
	}

}
