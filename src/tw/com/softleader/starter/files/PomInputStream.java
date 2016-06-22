package tw.com.softleader.starter.files;

import java.io.ByteArrayInputStream;

import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class PomInputStream extends ByteArrayInputStream {

	public PomInputStream(String pj, ProjectDetailsPage projectDetails, DependencyPage dependency, String source) {
		super(merge(pj, projectDetails, dependency, source).getBytes());
	}

	private static String merge(String pj, ProjectDetailsPage projectDetails, DependencyPage dependency,
			String source) {
		source = source.replace("{projectName}", pj);
		source = source.replace("{groupId}", projectDetails.getGroup());
		source = source.replace("{artifactId}", projectDetails.getArtifact());
		source = source.replace("{version}", projectDetails.getVersion());

		// TODO: dependencies

		return source;
	}

}
