package pl.edu.agh.iptv.persistence;

public enum Quality {
	LOW ("0.2"),
	MEDIUM ("0.4"),
	HIGH ("1");
	
	Quality(String scale) {
		this.scale = scale;
	}
	
	private String scale;
	
	public String getScale() {
		return scale;
	}
}
