package tw.com.softleader.starter.files;

import java.io.ByteArrayInputStream;

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
		return source;
	}

}
