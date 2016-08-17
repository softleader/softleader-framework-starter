package tw.com.softleader.starter.pojo;

public class WebappDatabase {

	private String name;
	private String groupId;
	private String artifactId;
	private String version;
	private String driver;
	private boolean dft;
	private boolean enabled;
	private String urlHint;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public boolean isDft() {
		return dft;
	}

	public void setDft(boolean dft) {
		this.dft = dft;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUrlHint() {
		return urlHint;
	}

	public void setUrlHint(String urlHint) {
		this.urlHint = urlHint;
	}

}
