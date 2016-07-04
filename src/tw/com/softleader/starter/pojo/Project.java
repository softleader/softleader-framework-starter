package tw.com.softleader.starter.pojo;

public class Project {

	private String group;
	private String artifact;
	private String version;
	private String desc;
	private String pkg;
	private String globalSnippet; // 包含 project settings 那些 global 的程式碼

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getGlobalSnippet() {
		return globalSnippet;
	}

	public void setGlobalSnippet(String globalSnippet) {
		this.globalSnippet = globalSnippet;
	}

}
