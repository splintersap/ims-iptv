package pl.edu.agh.ims.commons.rating;

public class RatingMessage {

	private String movieTitle;
	private int rate;	
	
	public RatingMessage(String movieTitle, int rate){
		this.movieTitle = movieTitle;
		this.rate = rate;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public int getRate() {
		return rate;
	}
	
	
	
}
