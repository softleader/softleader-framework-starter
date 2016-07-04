package tw.com.softleader.starter.io;

import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.DependencyRadio;
import org.eclipse.swt.widgets.VersionRadio;

import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class Component extends SnippetSource {

	protected final DependencyPage dependency;

	public Component(ProjectDetailsPage projectDetails, DependencyPage dependency) {
		super(projectDetails);
		this.dependency = dependency;
	}

	@Override
	public byte[] apply(String source) {
		VersionRadio version = dependency.getVersions().stream().filter(VersionRadio::isSelected).findFirst().get(); // 一定會有選擇
		String dependentModule = dependency.getModules().values().stream().flatMap(Collection::stream)
				.filter(DependencyRadio::isSelected)
				.map(select -> select.getComponentText(version.getSoftleaderFramework()))
				.collect(Collectors.joining("\n"));
		source = source.replace("{dependent-module}", dependentModule);
		return super.apply(source);
	}

}
