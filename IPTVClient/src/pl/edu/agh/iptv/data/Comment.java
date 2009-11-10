package pl.edu.agh.iptv.data;

import java.util.Date;

public class Comment {
	private Date date;
	private String comment;
	private String user;

	public Comment(Date date, String comment, String user) {
		this.date = date;
		this.comment = comment;
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
