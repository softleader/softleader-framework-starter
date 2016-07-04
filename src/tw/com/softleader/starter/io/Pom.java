package tw.com.softleader.starter.io;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.DataSourceRadio;
import org.eclipse.swt.widgets.DependencyRadio;
import org.eclipse.swt.widgets.VersionRadio;

import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class Pom extends SnippetSource {

	protected final DependencyPage dependency;
	protected final DatasourcePage datasource;

	public Pom(ProjectDetailsPage projectDetails, DependencyPage dependency, DatasourcePage datasource) {
		super(projectDetails);
		this.dependency = dependency;
		this.datasource = datasource;
	}

	@Override
	public byte[] apply(String source) {
		source = source.replace("{projectName}", projectDetails.getProjectName().getValue());
		source = source.replace("{groupId}", projectDetails.getGroup().getValue());
		source = source.replace("{artifactId}", projectDetails.getArtifact().getValue());
		source = source.replace("{version}", projectDetails.getVersion().getValue());

		VersionRadio version = dependency.getVersions().stream().filter(VersionRadio::isSelected).findFirst().get(); // 一定會有選擇
		source = source.replace("{slVersion}", version.getSoftleaderFramework());
		source = source.replace("{ioVersion}", version.getIoPlatform());

		String dependencyText = dependency.getModules().values().stream().flatMap(Collection::stream)
				.filter(DependencyRadio::isSelected).sorted(Comparator.comparing(DependencyRadio::getArtifactId))
				.map(DependencyRadio::getPomText).collect(Collectors.joining("\n"));
		source = source.replace("{dependencies}", dependencyText);

		DataSourceRadio ds = datasource.getDatasources().stream().filter(DataSourceRadio::isSelected).findFirst().get(); // 一定會有選擇
		source = source.replace("{datasource}", ds.getPomText());

		return super.apply(source);
	}

}