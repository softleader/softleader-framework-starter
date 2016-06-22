package tw.com.softleader.starter.files;

import java.io.ByteArrayInputStream;

public class JavaInputStream extends ByteArrayInputStream {

	public JavaInputStream(String pkg, String pj, String source) {
		super(merge(pkg, pj, source).getBytes());
	}

	private static String merge(String pkg, String pj, String source) {
		source = source.replaceAll("\\{pkg\\}", pkg).replaceAll("\\{pj\\}", pj);
		return source;
	}

}
