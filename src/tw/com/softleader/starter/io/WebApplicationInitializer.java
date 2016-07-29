package tw.com.softleader.starter.io;

import java.util.Collection;
import java.util.stream.Collectors;

import tw.com.softleader.starter.page.ProjectDetailsPage;
import tw.com.softleader.starter.pojo.Snippet;

public class WebApplicationInitializer extends SnippetSource {

	protected final Collection<String> rootConfigs;
	protected final Collection<String> servletConfigs;
	protected final Collection<String> servletFilters;

	public WebApplicationInitializer(ProjectDetailsPage projectDetails, Collection<Snippet> snippets) {
		super(projectDetails);
		this.rootConfigs = snippets.stream().map(Snippet::getRootConfigs).flatMap(Collection::stream)
				.collect(Collectors.toSet());
		snippets.stream().map(Snippet::getRemoveRootConfigs).flatMap(Collection::stream)
				.forEach(this.rootConfigs::remove);
		this.servletConfigs = snippets.stream().map(Snippet::getServletConfigs).flatMap(Collection::stream)
				.collect(Collectors.toSet());
		this.servletFilters = snippets.stream().map(Snippet::getServletFilters).flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}

	@Override
	public byte[] apply(String source) {
		String roots = rootConfigs.stream().collect(Collectors.joining(", ", "{", "}"));
		source = source.replace("{rootConfigClasses}", roots);
		String servlets = servletConfigs.stream().collect(Collectors.joining(", ", "{", "}"));
		source = source.replace("{servletConfigClasses}", servlets);
		String filters = servletFilters.stream().collect(Collectors.joining(", ", "{", "}"));
		source = source.replace("{servletFilters}", filters);
		return super.apply(source);
	}

}
