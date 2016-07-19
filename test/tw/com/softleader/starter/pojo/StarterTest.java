package tw.com.softleader.starter.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import tw.com.softleader.starter.NewSoftLeaderWebappStarter;
import tw.com.softleader.starter.pojo.Group.Layout;
import tw.com.softleader.starter.pojo.Group.Style;
import tw.com.softleader.starter.util.JSON;

public class StarterTest {

	@Test
	public void generateId() {
		// tw.com.softleader.starter.pojo.Starter.CURRENT_REVISION
		System.out.println(System.currentTimeMillis());
	}

	@Test
	public void testToJson() {
		Starter starter = new Starter();
		starter.setBaseUrl(
				"https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/resources");

		starter.setRevision(Starter.CURRENT_REVISION);

		Project pj;
		starter.setProject(pj = new Project());
		pj.setArtifact("softleader-");
		pj.setDesc("SoftLeader project for ");
		pj.setGroup("tw.com.softleader");
		pj.setPkg("tw.com.softleader");
		pj.setVersion("0.0.1-SNAPSHOT");
		pj.setGlobalSnippet("global-snippet/global-snippet.zip");

		Group<Version> versionGroup;
		starter.setVersions(versionGroup = new Group<>());
		versionGroup.setText("SoftLeader Framework Version");
		versionGroup.setLayout(Layout.H);
		Collection<Version> versions;
		versionGroup.setData(versions = new ArrayList<>());
		versions.add(new Version("1.1.0.SNAPSHOT", "2.0.3.RELEASE", true, true));
		versions.add(new Version("1.0.0.RELEASE", "1.1.3.RELEASE", false, false));

		Collection<Group<Dependency>> modules;
		starter.setModules(modules = new ArrayList<>());
		Group<Dependency> group;
		Collection<Dependency> data;
		modules.add(group = new Group<>());
		group.setText("Basic");
		group.setStyle(Style.CHECK);
		group.setLayout(Layout.H);
		group.setData(data = new ArrayList<>());
		data.add(new Dependency("tw.com.softleader", "softleader-util", null, null, true, false, null));
		data.add(new Dependency("tw.com.softleader", "softleader-commons", null, null, true, false, null));
		data.add(new Dependency("tw.com.softleader", "softleader-web", null, null, true, false, null));
		data.add(new Dependency("tw.com.softleader", "softleader-domain", null, null, true, false, null));
		data.add(new Dependency("tw.com.softleader", "softleader-data", null, null, true, false, null));

		modules.add(group = new Group<>());
		group.setText("Web");
		group.setStyle(Style.CHECK);
		group.setLayout(Layout.V);
		group.setData(data = new ArrayList<>());
		data.add(new Dependency("tw.com.softleader", "softleader-web-mvc", null, null, true, false,
				"softleader-web-mvc/softleader-web-mvc.zip"));
		data.add(new Dependency("tw.com.softleader", "softleader-security", null, null, true, false, null));
		data.add(new Dependency("tw.com.softleader", "softleader-resources", null, null, false, true, null));

		modules.add(group = new Group<>());
		group.setText("Domain");
		group.setStyle(Style.CHECK);
		group.setLayout(Layout.V);
		group.setData(data = new ArrayList<>());
		data.add(new Dependency("tw.com.softleader", "softleader-domain-rule", null, null, false, true,
				"softleader-domain-rule/softleader-domain-rule.zip"));
		data.add(new Dependency("tw.com.softleader", "softleader-domain-formula", null, null, false, true, null));
		data.add(new Dependency("tw.com.softleader", "softleader-domain-scheduling", null, null, false, true,
				"softleader-domain-scheduling/softleader-domain-scheduling.zip"));
		data.add(new Dependency("tw.com.softleader", "softleader-report-jasper", null, null, false, true, null));
		data.add(new Dependency("tw.com.softleader", "softleader-domain-bpm", null, null, false, false, null));

		modules.add(group = new Group<>());
		group.setText("Data");
		group.setStyle(Style.RADIO);
		group.setLayout(Layout.V);
		group.setData(data = new ArrayList<>());
		data.add(new Dependency("tw.com.softleader", "softleader-data-jpa", null, null, true, true, null));
		data.add(new Dependency("tw.com.softleader", "softleader-data-mybatis", null, null, false, true, null));

		modules.add(group = new Group<>());
		group.setText("Test");
		group.setStyle(Style.RADIO);
		group.setLayout(Layout.V);
		group.setData(data = new ArrayList<>());
		data.add(new Dependency("tw.com.softleader", "softleader-test", null, null, true, false, null));

		Group<Database> database;
		starter.setDatabase(database = new Group<>());
		database.setText("Database");
		database.setLayout(Layout.V);
		database.setStyle(Style.RADIO);
		database.setData(new ArrayList<>());
		database.getData().add(new Database("PostgreSQL", "org.postgresql", "postgresql", null, "org.postgresql.Driver",
				true, true, "jdbc:postgresql:[<//host>[:<5432>/]]<database>"));
		database.getData().add(new Database("MySQL", "mysql", "mysql-connector-java", null, "com.mysql.jdbc.Driver",
				false, true,
				"jdbc:mysql://<hostname>[,<failoverhost>][<:3306>]/<dbname>[?<param1>=<value1>][&<param2>=<value2>]"));
		database.getData().add(new Database("Oracle Thin ojdbc14", "com.oracle", "ojdbc14", "10.2.0.4.0",
				"oracle.jdbc.driver.OracleDriver", false, true, "jdbc:oracle:thin:@<server>[:<1521>]:<database_name>"));
		database.getData()
				.add(new Database("Microsoft sqljdbc4", "com.microsoft.sqlserver", "sqljdbc4", "4.0",
						"com.microsoft.sqlserver.jdbc.SQLServerDriver", false, true,
						"jdbc:sqlserver://<server_name>:1433;databaseName=<db_name>"));
		database.getData().add(new Database("HSQL", "org.hsqldb", "hsqldb", null, "org.hsqldb.jdbcDriver", false, true,
				"jdbc:hsqldb:hsql://<server>[:<1476>]"));
		database.getData().add(new Database("H2", "com.h2database", "h2", null, "org.h2.Driver", false, true,
				"jdbc:h2://<server>:<9092>/<db-name>"));

		String json = JSON.toString(starter);
		System.out.println(json);
	}

	@Test
	public void testFromJson() throws IOException {
		Starter starter = Starter.fromUrl(StarterTest.class.getClassLoader().getResource("starter.json"));
		Assert.assertNotNull(starter);
		Assert.assertNotNull(starter.getBaseUrl());
		Assert.assertNotNull(starter.getDatabase());
		Assert.assertNotNull(starter.getVersions());
		Assert.assertNotNull(starter.getModules());
		Assert.assertNotNull(starter.getProject());
		starter.getDatabase().getData().forEach(System.out::println);
	}

	@Test
	public void testFromUrl() throws IOException {
		Starter starter = Starter.fromUrl(NewSoftLeaderWebappStarter.STARTER);
		Assert.assertNotNull(starter);
		Assert.assertNotNull(starter.getBaseUrl());
		Assert.assertNotNull(starter.getDatabase());
		Assert.assertNotNull(starter.getVersions());
		Assert.assertNotNull(starter.getModules());
		Assert.assertNotNull(starter.getProject());
	}

}
