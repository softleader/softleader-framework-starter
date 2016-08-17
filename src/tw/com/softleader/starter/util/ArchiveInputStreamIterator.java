package tw.com.softleader.starter.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;

public class ArchiveInputStreamIterator<U> implements Iterator<U>, AutoCloseable {

	public ArchiveInputStreamIterator(ArchiveInputStream in, BiFunction<ArchiveEntry, ArchiveInputStream, U> mapper) {
		super();
		this.in = in;
		this.mapper = mapper;
	}

	private final ArchiveInputStream in;
	private final BiFunction<ArchiveEntry, ArchiveInputStream, U> mapper;
	private U cached;
	private boolean finished = false;

	@Override
	public boolean hasNext() {
		if (cached != null) {
			return true;
		} else if (finished) {
			return false;
		} else {
			try {
				ArchiveEntry next = in.getNextEntry();
				if (next == null) {
					finished = true;
					return false;
				} else {
					cached = mapper.apply(next, in);
					return true;
				}
			} catch (Exception e) {
				close();
				throw new IllegalStateException(e);
			}
		}
	}

	@Override
	public U next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No more archives");
		}
		try {
			return cached;
		} finally {
			cached = null;
		}
	}

	@Override
	public void close() {
		finished = true;
		try {
			in.close();
		} catch (IOException e) {
			// no-op
		}
		cached = null;
	}

	public void remove() {
		throw new UnsupportedOperationException("Remove unsupported on ArchiveInputStreamIterator");
	}

}
