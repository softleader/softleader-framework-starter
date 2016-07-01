package tw.com.softleader.starter.io;

import java.util.Collection;
import java.util.stream.Collectors;

public class WebApplicationInitializerInputStream extends JavaInputStream {

	public WebApplicationInitializerInputStream(String projectName, String pkg, Collection<String> rootConfigs,
			Collection<String> servletConfigs, String source) {
		super(projectName, pkg, merge(pkg, rootConfigs, servletConfigs, source));
	}

	private static String merge(String pkg, Collection<String> rootConfigs, Collection<String> servletConfigs,
			String source) {
		String root = rootConfigs.stream().map(config -> config.replaceAll("\\{pkg\\}", pkg))
				.collect(Collectors.joining(", ", "{", "}"));
		source = source.replace("{rootConfigClasses}", root);
		String servlet = servletConfigs.stream().map(config -> config.replaceAll("\\{pkg\\}", pkg))
				.collect(Collectors.joining(", ", "{", "}"));
		source = source.replace("{servletConfigClasses}", servlet);
		return source;
	}

}
