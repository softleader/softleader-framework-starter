package tw.com.softleader.starter.files;

import java.util.Optional;

public class F {

	private String name;
	private String path;
	private Optional<String> dependency;
	private Optional<String> content;

	public F(String name, String path, String dependency) {
		super();
		this.name = name;
		this.path = path;
		this.dependency = (dependency == null || dependency.trim().isEmpty()) ? Optional.empty()
				: Optional.of(dependency);
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public Optional<String> getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = Optional.ofNullable(content);
	}

	public Optional<String> getDependency() {
		return dependency;
	}

	@Override
	public String toString() {
		return "F [name=" + name + ", path=" + path + ", dependency=" + dependency + ", content=" + content + "]";
	}

	public boolean isFolder() {
		return name == null || name.trim().isEmpty();
	}

	public boolean isFile() {
		return !isFolder();
	}

	public boolean isJava() {
		return getName().endsWith(".java");
	}

	public boolean isPOM() {
		return getName().endsWith("pom.xml");
	}

}
