package tw.com.softleader.starter.pojo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.google.common.collect.Lists;

import tw.com.softleader.starter.util.JSON;

public class SnippetTest {

	@Test
	public void testJpaSnippet() {
		Snippet snippet = new Snippet();

		Collection<String> folders;
		snippet.setFolders(folders = new ArrayList<>());
		folders.add("src/main/java/{pkgPath}/example/dao");
		folders.add("src/main/java/{pkgPath}/example/entity");
		folders.add("src/main/java/{pkgPath}/example/service");
		folders.add("src/main/java/{pkgPath}/example/service/impl");
		folders.add("src/main/java/{pkgPath}/example/web");
		folders.add("src/test/java/{pkgPath}/example/service");
		folders.add("src/test/java/{pkgPath}/example/web");

		snippet.setRootConfigs(Lists.newArrayList("DataSourceConfig.class"));
		snippet.setRemoveRootConfigs(Lists.newArrayList("tw.com.softleader.data.config.DataSourceConfiguration.class"));
		snippet.setServletConfigs(Lists.newArrayList("DataSourceConfig.class"));

		Collection<Source> sources;
		snippet.setSources(sources = new ArrayList<>());
		sources.add(new Source("DataSourceConfig.java", "src/main/java/{pkgPath}/config", "DataSourceConfig.java"));
		sources.add(
				new Source("ExampleController.java", "src/main/java/{pkgPath}/example/web", "ExampleController.java"));
		sources.add(new Source("ExampleControllerTest.java", "src/test/java/{pkgPath}/example/web",
				"ExampleControllerTest.java"));
		sources.add(new Source("ExampleDao.java", "src/main/java/{pkgPath}/example/dao", "ExampleDao.java"));
		sources.add(new Source("ExampleEntity.java", "src/main/java/{pkgPath}/example/entity", "ExampleEntity.java"));
		sources.add(new Source("ExampleAssociationEntity.java", "src/main/java/{pkgPath}/example/entity",
				"ExampleAssociationEntity.java"));
		sources.add(
				new Source("ExampleService.java", "src/main/java/{pkgPath}/example/service", "ExampleService.java"));
		sources.add(new Source("ExampleServiceImpl.java", "src/main/java/{pkgPath}/example/service/impl",
				"ExampleServiceImpl.java"));
		sources.add(new Source("ExampleServiceTest.java", "src/test/java/{pkgPath}/example/service",
				"ExampleServiceTest.java"));
		sources.add(
				new Source("ValidationMessages.properties", "src/main/resources/", "ValidationMessages.properties"));
		sources.add(
				new Source("ValidationMessages.properties", "src/test/resources/", "ValidationMessages.properties"));

		System.out.println(JSON.toString(snippet));
	}

	@Test
	public void testDomainSchedulingSnippet() {
		Snippet snippet = new Snippet();

		Collection<String> rootConfigs;
		snippet.setRootConfigs(rootConfigs = new ArrayList<>());
		rootConfigs.add("SchedulingConfig.class");

		Collection<Source> sources;
		snippet.setSources(sources = new ArrayList<>());
		sources.add(new Source("schedule.properties", "src/main/resources", "schedule.properties"));
		sources.add(new Source("SchedulingConfig.java", "src/main/java/{pkgPath}/config", "SchedulingConfig.java"));
		sources.add(new Source("JobController.java", "src/main/java/{pkgPath}/web", "JobController.java"));

		System.out.println(JSON.toString(snippet));
	}

	@Test
	public void testGlobalSnippet() {
		Snippet snippet = new Snippet();

		Collection<String> folders;
		snippet.setFolders(folders = new ArrayList<>());
		folders.add(".settings");
		folders.add("src/main/java/{pkgPath}/config");

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
		sources.add(new Source("WebApplicationInitializer.java", "src/main/java/{pkgPath}/config",
				"WebApplicationInitializer.java"));

		System.out.println(JSON.toString(snippet));
	}

	@Test
	public void testDoaminRule() {
		Snippet snippet = new Snippet();
		Collection<String> rootConfigs;
		snippet.setRootConfigs(rootConfigs = new ArrayList<>());
		rootConfigs.add("tw.com.softleader.rule.config.RuleConfiguration.class");

		System.out.println(JSON.toString(snippet));
	}

	@Test
	public void testWebMvc() {
		Snippet snippet = new Snippet();

		Collection<String> rootConfigs;
		snippet.setRootConfigs(rootConfigs = new ArrayList<>());
		rootConfigs.add("tw.com.softleader.data.config.DataSourceConfiguration.class");
		rootConfigs.add("tw.com.softleader.domain.config.DefaultDomainConfiguration.class");
		rootConfigs.add("WebSecurityConfig.class");
		rootConfigs.add("ServiceConfig.class");

		Collection<String> servletConfigs;
		snippet.setServletConfigs(servletConfigs = new ArrayList<>());
		servletConfigs.add("WebMvcConfig.class");

		Collection<String> folders;
		snippet.setFolders(folders = new ArrayList<>());
		folders.add("src/main/java/{pkgPath}/security/service");
		folders.add("src/main/java/{pkgPath}/index/web");
		folders.add("src/main/resources");
		folders.add("src/main/webapp/WEB-INF/pages");
		folders.add("src/test/java/{pkgPath}");
		folders.add("src/test/resources");

		Collection<Source> sources;
		snippet.setSources(sources = new ArrayList<>());
		sources.add(new Source("SecurityWebApplicationInitializer.java", "src/main/java/{pkgPath}/config",
				"SecurityWebApplicationInitializer.java"));
		sources.add(new Source("WebSecurityConfig.java", "src/main/java/{pkgPath}/config", "WebSecurityConfig.java"));
		sources.add(new Source("ServiceConfig.java", "src/main/java/{pkgPath}/config", "ServiceConfig.java"));
		sources.add(new Source("WebMvcConfig.java", "src/main/java/{pkgPath}/config", "WebMvcConfig.java"));
		sources.add(new Source("UserDetailsService.java", "src/main/java/{pkgPath}/security/service",
				"UserDetailsService.java"));
		sources.add(new Source("IndexController.java", "src/main/java/{pkgPath}/index/web", "IndexController.java"));
		sources.add(new Source("datasource.properties", "src/main/resources", "datasource.properties"));
		sources.add(new Source("datasource.properties", "src/test/resources", "datasource.properties"));
		sources.add(new Source("index.jsp", "src/main/webapp/WEB-INF/pages", "index.jsp"));
		sources.add(new Source("login.jsp", "src/main/webapp/WEB-INF/pages", "login.jsp"));
		sources.add(new Source("logback.xml", "src/main/resources/", "logback.xml"));
		sources.add(new Source("logback-test.xml", "src/test/resources/", "logback.xml"));

		String json = JSON.toString(snippet);
		System.out.println(json);
	}

	@Test
	public void testLoad() throws MalformedURLException, IOException {
		Snippet snippet = Snippet.load(
				"https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/resources/softleader-web-mvc.zip");
		System.out.println("Root configs are:");
		snippet.getRootConfigs().forEach(System.out::println);

		System.out.println("Servlet configs are:");
		snippet.getServletConfigs().forEach(System.out::println);

		System.out.println("Folders are:");
		snippet.getFolders().forEach(System.out::println);

		System.out.println("Files are:");
		snippet.getSources().forEach(System.out::println);
	}
}
