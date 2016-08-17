package tw.com.softleader.starter.pojo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.swt.widgets.DataSourceRadio;
import org.eclipse.swt.widgets.DependencyRadio;
import org.eclipse.swt.widgets.InputText;
import org.eclipse.swt.widgets.VersionRadio;

import com.google.common.base.Charsets;

import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;
import tw.com.softleader.starter.page.SiteInfoPage;
import tw.com.softleader.starter.util.ArchiveStream;
import tw.com.softleader.starter.util.JSON;

public class Snippet {

	ProjectDetails project;
	Version version;
	Collection<Dependency> dependencies;
	Database database;

	private transient Set<String> dirs;
	private transient Map<String, String> srcs;

	public Snippet(ProjectDetailsPage projectDetails, DependencyPage dependency, DatasourcePage datasource,
			SiteInfoPage siteInfo) throws IOException, IllegalStateException, ArchiveException {
		super();

		project = new ProjectDetails();
		project.setName(projectDetails.getProjectName().getValue());
		project.setPkg(projectDetails.getPkg().getValue());
		project.setGroupId(projectDetails.getGroup().getValue());
		project.setArtifactId(projectDetails.getArtifact().getValue());
		project.setVersion(projectDetails.getVersion().getValue());
		Optional.ofNullable(projectDetails.getDesc()).map(InputText::getValue).ifPresent(project::setDesc);

		VersionRadio vr = dependency.getVersions().stream().filter(VersionRadio::isSelected).findFirst().get(); // 一定會有選擇
		version = new Version();
		version.setSoftleaderFramework(vr.getSoftleaderFramework());
		version.setSpringIoPlatform(vr.getIoPlatform());

		DataSourceRadio ds = datasource.getDatasources().stream().filter(DataSourceRadio::isSelected).findFirst().get(); // 一定會有選擇
		database = new Database();
		database.setName(ds.getDatabase());
		database.setGroupId(ds.getGroupId());
		database.setArtifactId(ds.getArtifactId());
		database.setVersion(ds.getVersion());
		database.setScope(ds.getScope());
		database.setDriverClass(datasource.getDriverClass().getValue());
		database.setUrl(datasource.getUrl().getValue());
		database.setUsername(datasource.getUsername().getValue());
		database.setPassword(datasource.getPassword().getValue());

		dependencies = dependency.getModules().values().stream().flatMap(Collection::stream)
				.filter(DependencyRadio::isSelected).map(dr -> {
					Dependency d = new Dependency();
					d.setGroupId(dr.getGroupId());
					d.setArtifactId(dr.getArtifactId());
					d.setVersion(dr.getVersion());
					d.setScope(dr.getScope());
					return d;
				}).collect(Collectors.toList());

		srcs = new HashMap<>();
		dirs = new HashSet<>();

		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpPost httppost = new HttpPost(siteInfo.getBaseUrl());
			httppost.setHeader("Accept", "application/zip");
			httppost.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity(JSON.toString(this), Charsets.UTF_8);
			entity.setChunked(true);
			httppost.setEntity(entity);
			try (CloseableHttpResponse response = httpclient.execute(httppost)) {
				BufferedInputStream in = new BufferedInputStream(response.getEntity().getContent());
				ArchiveStream.of(in).forEach((entry, entryIn) -> {
					if (!entry.getName().endsWith("__MACOSX")) {
						String path = entry.getName();
						if (path.startsWith("/")) {
							path = path.substring(1, path.length());
						}
						if (entry.isDirectory()) {
							dirs.add(path);
						} else {
							Optional.ofNullable(Paths.get(path).getParent()).map(Path::toString).ifPresent(dirs::add);
							try {
								srcs.put(path, new String(IOUtils.toByteArray(entryIn), Charsets.UTF_8));
							} catch (IOException e) {
								throw new IllegalStateException(e);
							}
						}
					}
				});
			}
		}
	}

	public Collection<String> getDirs() {
		return dirs;
	}

	public Map<String, String> getSrcs() {
		return srcs;
	}

}
