package tw.com.softleader.starter.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.GsonBuilder;

import tw.com.softleader.starter.NewSoftLeaderWebappStarter;
import tw.com.softleader.starter.pojo.Group.Layout;
import tw.com.softleader.starter.pojo.Group.Style;

public class StarterTest {

	@Test
	public void testToJson() {
		Starter starter = new Starter();
		starter.setBaseUrl(
				"https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/resources");

		Project pd;
		starter.setProject(pd = new Project());
		pd.setArtifact("softleader-project");
		pd.setDesc("SoftLeader Project");
		pd.setGroup("tw.com.softleader");
		pd.setPkg("tw.com.softleader.");
		pd.setVersion("0.0.1-SNAPSHOT");

		Group<Version> versionGroup;
		starter.setVersions(versionGroup = new Group<>());
		versionGroup.setText("SoftLeader Framework Version");
		versionGroup.setLayout(Layout.H);
		Collection<Version> versions;
		versionGroup.setData(versions = new ArrayList<>());
		versions.add(new Version("1.1.0.SNAPSHOT", "2.0.3.RELEASE", true));
		versions.add(new Version("1.0.0.SNAPSHOT", "1.1.3.RELEASE", false));

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
		data.add(new Dependency("tw.com.softleader", "softleader-web-mvc", null, null, true, false, null));
		data.add(new Dependency("tw.com.softleader", "softleader-security", null, null, true, false, null));
		data.add(new Dependency("tw.com.softleader", "softleader-resources", null, null, false, true, null));

		modules.add(group = new Group<>());
		group.setText("Domain");
		group.setStyle(Style.CHECK);
		group.setLayout(Layout.V);
		group.setData(data = new ArrayList<>());
		data.add(new Dependency("tw.com.softleader", "softleader-domain-rule", null, null, false, true, null));
		data.add(new Dependency("tw.com.softleader", "softleader-domain-formula", null, null, false, true, null));
		data.add(new Dependency("tw.com.softleader", "softleader-domain-scheduling", null, null, false, true, null));
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
		database.getData().add(
				new Database("POSTGRESQL", "org.postgresql", "postgresql", null, "org.postgresql.Driver", true, true));
		database.getData()
				.add(new Database("HSQL", "org.hsqldb", "hsqldb", null, "org.hsqldb.jdbcDriver", false, true));
		database.getData().add(new Database("H2", "com.h2database", "h2", null, "org.h2.Driver", false, true));

		String json = new GsonBuilder()// .setPrettyPrinting()
				.create().toJson(starter);
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
