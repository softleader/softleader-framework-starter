package tw.com.softleader.starter.io;

import java.util.function.Function;

import tw.com.softleader.starter.page.ProjectDetailsPage;

public class SnippetSource implements Function<String, byte[]> {

	protected final ProjectDetailsPage projectDetails;

	public SnippetSource(ProjectDetailsPage projectDetails) {
		super();
		this.projectDetails = projectDetails;
	}

	/**
	 * 會取代以下 keyword: 1: {pj} 2. {pkg}, 3. {pkgPath}
	 */
	@Override
	public byte[] apply(String source) {
		return source.replaceAll("\\{pj\\}", projectDetails.getProjectName().getValue())
				.replaceAll("\\{pkg\\}", projectDetails.getPkg().getValue())
				.replaceAll("\\{pkgPath\\}", projectDetails.getPkgPath()).getBytes();
	}

}
