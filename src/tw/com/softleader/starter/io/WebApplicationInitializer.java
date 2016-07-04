package tw.com.softleader.starter.io;

import java.util.Collection;
import java.util.stream.Collectors;

import tw.com.softleader.starter.page.ProjectDetailsPage;
import tw.com.softleader.starter.pojo.Snippet;

public class WebApplicationInitializer extends SnippetSource {

	protected final Collection<String> rootConfigs;
	protected final Collection<String> servletConfigs;

	public WebApplicationInitializer(ProjectDetailsPage projectDetails, Collection<Snippet> snippets) {
		super(projectDetails);
		this.rootConfigs = snippets.stream().map(Snippet::getRootConfigs).flatMap(Collection::stream)
				.collect(Collectors.toSet());
		this.servletConfigs = snippets.stream().map(Snippet::getServletConfigs).flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}

	@Override
	public byte[] apply(String source) {
		String root = rootConfigs.stream().collect(Collectors.joining(", ", "{", "}"));
		source = source.replace("{rootConfigClasses}", root);
		String servlet = servletConfigs.stream().collect(Collectors.joining(", ", "{", "}"));
		source = source.replace("{servletConfigClasses}", servlet);
		return super.apply(source);
	}

}
