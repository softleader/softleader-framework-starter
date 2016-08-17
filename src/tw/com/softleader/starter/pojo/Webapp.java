package tw.com.softleader.starter.pojo;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import tw.com.softleader.starter.enums.SwtLayout;
import tw.com.softleader.starter.enums.SwtStyle;
import tw.com.softleader.starter.util.JSON;

public class Webapp {

	public static final long CURRENT_REVISION = 1471418080640L; // tw.com.softleader.starter.pojo.StarterTest.generateId()

	private long revision;
	private String baseUrl;
	private String projectGroupId;
	private String projectArtifactId;
	private String projectVersion;
	private String projectDesc;
	private String projectPkg;
	private String versionText;
	private SwtLayout versionLayout;
	private SwtStyle versionStyle;
	private Collection<WebappVersion> versions;
	private Collection<WebappModule> modules;
	private String databaseText;
	private SwtLayout databaseLayout;
	private SwtStyle databaseStyle;
	private Collection<WebappDatabase> databases;

	public static Webapp fromUrl(URL url) throws IOException {
		String json = Resources.toString(url, Charsets.UTF_8);
		return JSON.from(json, Webapp.class);
	}

	public static Webapp fromUrl(String url) throws IOException {
		return fromUrl(new URL(url));
	}

	public boolean isUpToDate() {
		return revision <= CURRENT_REVISION;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public String getProjectGroupId() {
		return projectGroupId;
	}

	public void setProjectGroupId(String projectGroupId) {
		this.projectGroupId = projectGroupId;
	}

	public String getProjectArtifactId() {
		return projectArtifactId;
	}

	public void setProjectArtifactId(String projectArtifactId) {
		this.projectArtifactId = projectArtifactId;
	}

	public String getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}

	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}

	public String getProjectPkg() {
		return projectPkg;
	}

	public void setProjectPkg(String projectPkg) {
		this.projectPkg = projectPkg;
	}

	public String getVersionText() {
		return versionText;
	}

	public void setVersionText(String versionText) {
		this.versionText = versionText;
	}

	public SwtLayout getVersionLayout() {
		return versionLayout;
	}

	public void setVersionLayout(SwtLayout versionLayout) {
		this.versionLayout = versionLayout;
	}

	public SwtStyle getVersionStyle() {
		return versionStyle;
	}

	public void setVersionStyle(SwtStyle versionStyle) {
		this.versionStyle = versionStyle;
	}

	public Collection<WebappVersion> getVersions() {
		return versions;
	}

	public void setVersions(Collection<WebappVersion> versions) {
		this.versions = versions;
	}

	public Collection<WebappModule> getModules() {
		return modules;
	}

	public void setModules(Collection<WebappModule> modules) {
		this.modules = modules;
	}

	public String getDatabaseText() {
		return databaseText;
	}

	public void setDatabaseText(String databaseText) {
		this.databaseText = databaseText;
	}

	public SwtLayout getDatabaseLayout() {
		return databaseLayout;
	}

	public void setDatabaseLayout(SwtLayout databaseLayout) {
		this.databaseLayout = databaseLayout;
	}

	public SwtStyle getDatabaseStyle() {
		return databaseStyle;
	}

	public void setDatabaseStyle(SwtStyle databaseStyle) {
		this.databaseStyle = databaseStyle;
	}

	public Collection<WebappDatabase> getDatabases() {
		return databases;
	}

	public void setDatabases(Collection<WebappDatabase> databases) {
		this.databases = databases;
	}

}
