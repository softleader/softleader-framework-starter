package tw.com.softleader.starter.pojo;

public class Source {

	private String name;
	private String path;
	private String archive; // archive location in zip file
	private String content;

	@Override
	public String toString() {
		return "Source [name=" + name + ", path=" + path + ", archive=" + archive + ", content=" + content + "]";
	}

	public Source() {
		super();
	}

	public Source(String path) {
		super();
		this.path = path;
	}

	public Source(String name, String path, String archive) {
		super();
		this.name = name;
		this.path = path;
		this.archive = archive;
	}

	public boolean isFolder() {
		return name == null || name.trim().isEmpty();
	}

	public boolean isFile() {
		return !isFolder();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFullPath() {
		if (getPath() == null || getPath().isEmpty()) {
			return getName();
		}
		if (getPath().endsWith("/")) {
			return getPath() + getName();
		}
		return getPath() + "/" + getName();
	}

	public String getArchive() {
		return archive;
	}

	public void setArchive(String archive) {
		this.archive = archive;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isWebApplicationInitializer() {
		return getName().endsWith("WebApplicationInitializer.java");
	}

	public boolean isComponent() {
		return getName().endsWith(".component");
	}

	public boolean isDatasource() {
		return getName().equals("datasource.properties");
	}

	public boolean isPom() {
		return getName().equals("pom.xml");
	}

}
