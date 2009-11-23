package pl.edu.agh.iptv.presence.data;

public class Buddy {

	private String displayName;

	private String uri;

	private boolean online;

	public Buddy(String displayName, String uri) {
		this.displayName = displayName;
		this.uri = uri;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
