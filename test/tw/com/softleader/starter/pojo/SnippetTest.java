package tw.com.softleader.starter.pojo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import tw.com.softleader.starter.util.JSON;

public class SnippetTest {

	@Test
	public void testToJson() {
		Snippet snippet = new Snippet();

		Collection<String> folders;
		snippet.setFolders(folders = new ArrayList<>());
		folders.add(".settings");
		folders.add("src/main/java/{pkg}/config");
		folders.add("src/main/java/{pkg}/service");
		folders.add("src/main/java/{pkg}/web");
		folders.add("src/main/resources");
		folders.add("src/main/webapp/WEB-INF/pages");
		folders.add("src/test/java/{pkg}");
		folders.add("src/test/resources");

		Collection<Source> sources;
		snippet.setSources(sources = new ArrayList<>());
		sources.add(new Source("org.eclipse.wst.common.component", ".settings", "org.eclipse.wst.common.component"));
		sources.add(new Source("org.eclipse.wst.common.project.facet.core.xml", ".settings",
				"org.eclipse.wst.common.project.facet.core.xml"));
		sources.add(new Source("org.eclipse.m2e.core.prefs", ".settings", "org.eclipse.m2e.core.prefs"));
		sources.add(new Source("org.eclipse.jdt.core.prefs", ".settings", "org.eclipse.jdt.core.prefs"));
		sources.add(new Source("org.eclipse.core.resources.prefs", ".settings", "org.eclipse.core.resources.prefs"));
		sources.add(new Source("org.eclipse.wst.validation.prefs", ".settings", "org.eclipse.wst.validation.prefs"));
		sources.add(new Source(".classpath", "", "classpath"));
		sources.add(new Source("pom.xml", "", "pom.xml"));
		sources.add(new Source("SecurityWebApplicationInitializer.java", "src/main/java/{pkg}/config",
				"SecurityWebApplicationInitializer.java"));
		sources.add(new Source("WebSecurityConfig.java", "src/main/java/{pkg}/config", "WebSecurityConfig.java"));
		sources.add(new Source("WebApplicationInitializer.java", "src/main/java/{pkg}/config",
				"WebApplicationInitializer.java"));
		sources.add(new Source("UserDetailsService.java", "src/main/java/{pkg}/service", "UserDetailsService.java"));
		sources.add(new Source("IndexController.java", "src/main/java/{pkg}/web", "IndexController.java"));
		sources.add(new Source("datasource.properties", "src/main/resources", "datasource.properties"));
		sources.add(new Source("index.jsp", "src/main/webapp/WEB-INF/pages", "index.jsp"));
		sources.add(new Source("login.jsp", "src/main/webapp/WEB-INF/pages", "login.jsp"));

		String json = JSON.toString(snippet);
		System.out.println(json);
	}

	@Test
	public void testLoad() throws MalformedURLException, IOException {
		Snippet snippet = Snippet.load(
				"https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/resources/softleader-web-mvc.zip");
		System.out.println("Folders are:");
		snippet.getFolders().forEach(System.out::println);
		System.out.println("Files are:");
		snippet.getSources().forEach(System.out::println);
	}
}
