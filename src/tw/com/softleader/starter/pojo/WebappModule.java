package tw.com.softleader.starter.pojo;

import java.util.Collection;

import tw.com.softleader.starter.enums.SwtLayout;
import tw.com.softleader.starter.enums.SwtStyle;

public class WebappModule {

	private String dependencyText;
	private SwtLayout dependencyLayout;
	private SwtStyle dependencyStyle;
	private Collection<WebappDependency> dependencies;

	public String getDependencyText() {
		return dependencyText;
	}

	public void setDependencyText(String dependencyText) {
		this.dependencyText = dependencyText;
	}

	public SwtLayout getDependencyLayout() {
		return dependencyLayout;
	}

	public void setDependencyLayout(SwtLayout dependencyLayout) {
		this.dependencyLayout = dependencyLayout;
	}

	public SwtStyle getDependencyStyle() {
		return dependencyStyle;
	}

	public void setDependencyStyle(SwtStyle dependencyStyle) {
		this.dependencyStyle = dependencyStyle;
	}

	public Collection<WebappDependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Collection<WebappDependency> dependencies) {
		this.dependencies = dependencies;
	}

}
