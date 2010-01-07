package pl.edu.agh.iptv.persistence;

import javax.persistence.Id;
import javax.persistence.Entity;

@Entity
public class Setting {
	
	public static final String VLCIP = "VLCIP";
	
	@Id
	private String name;
	private String value;
	
	public Setting() {
		
	}
	
	public Setting(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
