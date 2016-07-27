package tw.com.softleader.starter.pojo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.compress.archivers.ArchiveEntry;

import com.google.common.base.Charsets;

import tw.com.softleader.starter.util.JSON;
import tw.com.softleader.starter.util.ZipStream;

public class Snippet {

	public Snippet() {
	}

	private static final String SNIPPET_JSON = "snippet.json";

	private Collection<String> rootConfigs = new ArrayList<>();
	private Collection<String> removeRootConfigs = new ArrayList<>();
	private Collection<String> servletConfigs = new ArrayList<>();
	private Collection<String> folders = new ArrayList<>();
	private Collection<Source> sources = new ArrayList<>();

	public static Snippet load(String url) throws MalformedURLException, IOException {
		InputStream in = new URL(url).openStream();
		Map<String, String> files = ZipStream.toMap(in, ArchiveEntry::getName,
				out -> new String(out.toByteArray(), Charsets.UTF_8), ByteArrayOutputStream::new);

		String json = files.get(SNIPPET_JSON);
		if (json == null) {
			throw new IllegalStateException("URL [" + url + "] does not contains " + SNIPPET_JSON);
		}
		Snippet snippet = JSON.from(json, Snippet.class);
		snippet.getSources()
				.forEach(src -> Optional.ofNullable(files.get(src.getArchive())).ifPresent(src::setContent));
		return snippet;
	}

	public Collection<String> getRootConfigs() {
		return rootConfigs;
	}

	public void setRootConfigs(Collection<String> rootConfigs) {
		this.rootConfigs = rootConfigs;
	}

	public Collection<String> getServletConfigs() {
		return servletConfigs;
	}

	public void setServletConfigs(Collection<String> servletConfigs) {
		this.servletConfigs = servletConfigs;
	}

	public Collection<String> getFolders() {
		return folders;
	}

	public void setFolders(Collection<String> folders) {
		this.folders = folders;
	}

	public Collection<Source> getSources() {
		return sources;
	}

	public void setSources(Collection<Source> sources) {
		this.sources = sources;
	}

	public Collection<String> getRemoveRootConfigs() {
		return removeRootConfigs;
	}

	public void setRemoveRootConfigs(Collection<String> removeRootConfigs) {
		this.removeRootConfigs = removeRootConfigs;
	}

	@Override
	public String toString() {
		return "Snippet [rootConfigs=" + rootConfigs + ", servletConfigs=" + servletConfigs + ", folders=" + folders
				+ ", sources=" + sources + "]";
	}

}
