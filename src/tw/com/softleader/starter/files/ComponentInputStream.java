package tw.com.softleader.starter.files;

import java.io.ByteArrayInputStream;

import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class ComponentInputStream extends ByteArrayInputStream {

	public ComponentInputStream(ProjectDetailsPage projectDetails, DependencyPage dependency, String source) {
		super(merge(projectDetails, dependency, source).getBytes());
	}

	private static String merge(ProjectDetailsPage projectDetails, DependencyPage dependency, String source) {
		// source = source.replaceAll("\\{pkg\\}", pkg).replaceAll("\\{pj\\}",
		// pj);
		// TODO

		source = source.replaceAll("\\{artifact\\}", projectDetails.getArtifact());
		return source;
	}

}
