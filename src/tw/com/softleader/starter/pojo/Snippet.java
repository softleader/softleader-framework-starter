package tw.com.softleader.starter.pojo;

import java.util.Collection;

public class Snippet {

	private String zip;
	private Collection<String> rootConfigs;
	private Collection<String> servletConfigs;
	private Collection<Source> sources;

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Collection<String> getRootConfigs() {
		return rootConfigs;
	}

	public void setRootConfigs(Collection<String> rootConfigs) {
		this.rootConfigs = rootConfigs;
	}

	public Collection<String> getServletConfigs() {
		return servletConfigs;
	}

	public void setServletConfigs(Collection<String> servletConfigs) {
		this.servletConfigs = servletConfigs;
	}

	public Collection<Source> getSources() {
		return sources;
	}

	public void setSources(Collection<Source> sources) {
		this.sources = sources;
	}

}
