package tw.com.softleader.starter.io;

import java.io.ByteArrayInputStream;

public class JavaInputStream extends ByteArrayInputStream {

	public JavaInputStream(String projectName, String pkg, String source) {
		super(merge(projectName, pkg, source).getBytes());
	}

	private static String merge(String projectName, String pkg, String source) {
		source = source.replaceAll("\\{pj\\}", projectName).replaceAll("\\{pkg\\}", pkg);
		return source;
	}

}
