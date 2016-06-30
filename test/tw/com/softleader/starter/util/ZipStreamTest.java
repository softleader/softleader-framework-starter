package tw.com.softleader.starter.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorException;
import org.junit.Test;

import com.google.common.base.Charsets;

public class ZipStreamTest {

	@Test
	public void testForEach() throws CompressorException, IOException {
		InputStream in = ZipStreamTest.class.getClassLoader().getResourceAsStream("template.zip");
		ZipStream.forEach(in,
				(entry, out) -> System.out
						.println(entry.getName() + ":\n" + new String(out.toByteArray(), Charsets.UTF_8)),
				ByteArrayOutputStream::new);
	}

}
