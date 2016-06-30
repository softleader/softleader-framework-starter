package tw.com.softleader.starter.pojo;

public class Database {

	private String name;
	private String group;
	private String artifact;
	private String version;
	private String driver;
	private boolean dft;
	private boolean enabled;
	private String urlHint;

	public Database() {
		super();
	}

	public Database(String name, String group, String artifact, String version, String driver, boolean dft,
			boolean enabled, String urlHint) {
		super();
		this.name = name;
		this.group = group;
		this.artifact = artifact;
		this.version = version;
		this.driver = driver;
		this.dft = dft;
		this.enabled = enabled;
		this.urlHint = urlHint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getArtifact() {
		return artifact;
	}

	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}

	public boolean isDft() {
		return dft;
	}

	public void setDft(boolean dft) {
		this.dft = dft;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	@Override
	public String toString() {
		return "Database [name=" + name + ", group=" + group + ", artifact=" + artifact + ", version=" + version
				+ ", driver=" + driver + ", dft=" + dft + ", enabled=" + enabled + ", urlHint=" + urlHint + "]";
	}
}
