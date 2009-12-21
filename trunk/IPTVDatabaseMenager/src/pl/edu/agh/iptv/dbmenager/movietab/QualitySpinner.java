package pl.edu.agh.iptv.dbmenager.movietab;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import pl.edu.agh.iptv.persistence.Quality;

public class QualitySpinner extends JSpinner{

	private static final long serialVersionUID = -6650759173300294435L;
	private Quality quality;

	public QualitySpinner() {
		super();
		SpinnerModel spinerModel =
	        new SpinnerNumberModel(100, 			//initial value
	                               0, 				//min
	                               Long.MAX_VALUE,  //max
	                               1);       
		
		this.setModel(spinerModel);
	}
	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}
	
}
