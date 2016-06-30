package tw.com.softleader.starter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

public final class ZipStream {

	private ZipStream() {
	}

	public static <O extends OutputStream> void forEach(InputStream in, BiConsumer<ArchiveEntry, O> action,
			Supplier<O> outSupplier) throws IOException {
		if (in.available() <= 0) {
			throw new IllegalArgumentException("input stream is not available");
		}
		try (ZipArchiveInputStream zin = new ZipArchiveInputStream(in)) {
			ArchiveEntry entry;
			while ((entry = zin.getNextZipEntry()) != null) {
				if (entry.isDirectory()) {
					continue;
				}
				if (entry.getName().startsWith("__MACOSX")) {
					continue;
				}
				try (O out = outSupplier.get()) {
					IOUtils.copy(zin, out);
					action.accept(entry, out);
				}
			}
		}
	}

	public static <K, V, O extends OutputStream> Map<K, V> toMap(InputStream in, Function<ArchiveEntry, K> keyMapper,
			Function<O, V> valueMapper, Supplier<O> outSupplier) throws IOException {
		Map<K, V> entries = new HashMap<>();
		forEach(in, (entry, out) -> entries.put(keyMapper.apply(entry), valueMapper.apply(out)), outSupplier);
		return entries;
	}

}
