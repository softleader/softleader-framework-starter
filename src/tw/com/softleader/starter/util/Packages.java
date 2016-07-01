package tw.com.softleader.starter.util;

import java.util.regex.Pattern;

public final class Packages {

	private Packages() {
	}

	public static final Pattern VALID_PACKAGES = Pattern.compile(
			"(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");

	public static boolean test(CharSequence input) {
		return VALID_PACKAGES.matcher(input).matches();
	}

}
