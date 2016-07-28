package tw.com.softleader.starter.pojo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import tw.com.softleader.starter.util.JSON;

public class Starter {

	static final long CURRENT_REVISION = 1469679202591L; // tw.com.softleader.starter.pojo.StarterTest.generateId()

	private long revision;
	private String baseUrl;
	private Project project;
	private Group<Version> versions;
	private Collection<Group<Dependency>> modules = new ArrayList<>();
	private Group<Database> database;

	@VisibleForTesting
	Starter() {
		super();
	}

	public boolean isUpToDate() {
		return revision <= CURRENT_REVISION;
	}

	public static Starter fromUrl(URL url) throws IOException {
		String json = Resources.toString(url, Charsets.UTF_8);
		return JSON.from(json, Starter.class);
	}

	public static Starter fromUrl(String url) throws IOException {
		return fromUrl(new URL(url));
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Group<Database> getDatabase() {
		return database;
	}

	public void setDatabase(Group<Database> database) {
		this.database = database;
	}

	public Group<Version> getVersions() {
		return versions;
	}

	public void setVersions(Group<Version> versions) {
		this.versions = versions;
	}

	public Collection<Group<Dependency>> getModules() {
		return modules;
	}

	public void setModules(Collection<Group<Dependency>> modules) {
		this.modules = modules;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

}
