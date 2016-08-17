package tw.com.softleader.starter.pojo;

import tw.com.softleader.starter.enums.MvnScope;

public class WebappDependency {

	private String groupId;
	private String artifactId;
	private String version;
	private MvnScope scope;
	private boolean dft;
	private boolean enabled;

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

	public MvnScope getScope() {
		return scope;
	}

	public void setScope(MvnScope scope) {
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

}
