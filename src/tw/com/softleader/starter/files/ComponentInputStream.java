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
		// source = source.replaceAll("\\{pkg\\}", pkg).replaceAll("\\{pj\\}",
		// pj);
		// TODO

		source = source.replaceAll("\\{pj\\}", projectName);

		VersionRadio version = dependency.getVersions().stream().filter(VersionRadio::isSelected).findFirst().get(); // 一定會有選擇
		source = source.replace("{slVersion}", version.getSoftleaderFramework());
		source = source.replace("{ioVersion}", version.getIoPlatform());

		String dependencyText = dependency.getDependencyGroups().values().stream().flatMap(Collection::stream)
				.filter(DependencyRadio::isSelected).map(DependencyRadio::getPomText).collect(Collectors.joining("/n"));
		source = source.replace("{dependencies}", dependencyText);
		
		return source;
	}

}
