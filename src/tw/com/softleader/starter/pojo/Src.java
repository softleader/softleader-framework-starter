package tw.com.softleader.starter.pojo;

public class Src {

	private String name;
	private String path;
	private String resource;

	public Src() {
		super();
	}

	public Src(String name, String path, String resource) {
		super();
		this.name = name;
		this.path = path;
		this.resource = resource;
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

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
}
