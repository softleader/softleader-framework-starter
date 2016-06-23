package tw.com.softleader.starter.files;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.DependencyRadio;
import org.eclipse.swt.widgets.VersionRadio;

import tw.com.softleader.starter.page.DependencyPage;

public class ComponentInputStream extends ByteArrayInputStream {

	public ComponentInputStream(String projectName, DependencyPage dependency, String source) {
		super(merge(projectName, dependency, source).getBytes());
	}

	private static String merge(String projectName, DependencyPage dependency, String source) {
		source = source.replaceAll("\\{pj\\}", projectName);

		VersionRadio version = dependency.getVersions().stream().filter(VersionRadio::isSelected).findFirst().get(); // 一定會有選擇
		String dependentModule = dependency.getDependencyGroups().values().stream().flatMap(Collection::stream)
				.filter(DependencyRadio::isSelected)
				.map(select -> select.getComponentText(version.getSoftleaderFramework()))
				.collect(Collectors.joining("/n"));
		source = source.replace("{dependent-module}", dependentModule);

		return source;
	}

}
