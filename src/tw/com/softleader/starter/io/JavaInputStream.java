package tw.com.softleader.starter.io;

import java.io.ByteArrayInputStream;

public class JavaInputStream extends ByteArrayInputStream {

	public JavaInputStream(String pkg, String source) {
		super(merge(pkg, source).getBytes());
	}

	private static String merge(String pkg, String source) {
		source = source.replaceAll("\\{pkg\\}", pkg);
		return source;
	}

}
