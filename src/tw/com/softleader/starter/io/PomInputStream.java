package tw.com.softleader.starter.io;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.DataSourceRadio;
import org.eclipse.swt.widgets.DependencyRadio;
import org.eclipse.swt.widgets.VersionRadio;

import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class PomInputStream extends ByteArrayInputStream {

	public PomInputStream(ProjectDetailsPage projectDetails, DependencyPage dependency, DatasourcePage datasource,
			String source) {
		super(merge(projectDetails, dependency, datasource, source).getBytes());
	}

	private static String merge(ProjectDetailsPage projectDetails, DependencyPage dependency, DatasourcePage datasource,
			String source) {
		source = source.replace("{projectName}", projectDetails.getProjectName());
		source = source.replace("{groupId}", projectDetails.getGroup());
		source = source.replace("{artifactId}", projectDetails.getArtifact());
		source = source.replace("{version}", projectDetails.getVersion());

		VersionRadio version = dependency.getVersions().stream().filter(VersionRadio::isSelected).findFirst().get(); // 一定會有選擇
		source = source.replace("{slVersion}", version.getSoftleaderFramework());
		source = source.replace("{ioVersion}", version.getIoPlatform());

		String dependencyText = dependency.getDependencyGroups().values().stream().flatMap(Collection::stream)
				.filter(DependencyRadio::isSelected).sorted(Comparator.comparing(DependencyRadio::getArtifactId))
				.map(DependencyRadio::getPomText).collect(Collectors.joining("\n"));
		source = source.replace("{dependencies}", dependencyText);

		DataSourceRadio ds = datasource.getDatasources().stream().filter(DataSourceRadio::isSelected).findFirst().get(); // 一定會有選擇
		source = source.replace("{datasource}", ds.getPomText());

		return source;
	}

}
