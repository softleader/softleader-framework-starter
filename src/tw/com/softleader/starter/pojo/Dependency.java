package tw.com.softleader.starter.pojo;

public class Dependency {

	private String group;
	private String artifact;
	private String version;
	private String scope;
	private boolean dft;
	private boolean enabled = true;
	private String snippet;

	public Dependency(String group, String artifact, String version, String scope, boolean dft, boolean enabled,
			String snippet) {
		super();
		this.group = group;
		this.artifact = artifact;
		this.version = version;
		this.scope = scope;
		this.dft = dft;
		this.enabled = enabled;
		this.snippet = snippet;
	}

	public Dependency() {
		super();
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
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

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

}
