package tw.com.softleader.starter.io;

import java.util.Collection;
import java.util.stream.Collectors;

import tw.com.softleader.starter.page.ProjectDetailsPage;

public class WebApplicationInitializer extends SnippetSource {

	protected final Collection<String> rootConfigs;
	protected final Collection<String> servletConfigs;

	public WebApplicationInitializer(ProjectDetailsPage projectDetails, Collection<String> rootConfigs,
			Collection<String> servletConfigs) {
		super(projectDetails);
		this.rootConfigs = rootConfigs;
		this.servletConfigs = servletConfigs;
	}

	@Override
	public byte[] apply(String source) {
		String root = rootConfigs.stream().collect(Collectors.joining(", ", "{", "}"));
		source = source.replaceAll("\\{rootConfigClasses\\}", root);
		String servlet = servletConfigs.stream().collect(Collectors.joining(", ", "{", "}"));
		source = source.replaceAll("\\{servletConfigClasses\\}", servlet);
		return super.apply(source);
	}

}
