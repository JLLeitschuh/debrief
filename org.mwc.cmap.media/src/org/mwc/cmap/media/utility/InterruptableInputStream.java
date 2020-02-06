
package org.mwc.cmap.media.utility;

import java.io.IOException;
import java.io.InputStream;

public abstract class InterruptableInputStream extends InputStream {

	private boolean interrupted;
	private InputStream in;
	
	public InterruptableInputStream(InputStream in) {
		this.in = in;
	}
	
	public boolean wasInterrupted() {
		return interrupted;
	}
	
	protected abstract void checkInterrupted() throws IOException;
	
	private void doCheck() throws IOException {
		try {
			checkInterrupted();
		} catch (IOException ex) {
			interrupted = true;
			throw ex;
		}
	}

	@Override
	public int read() throws IOException {
		doCheck();
		return in.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		doCheck();
		return in.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		doCheck();
		return in.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		doCheck();
		return in.skip(n);
	}

	@Override
	public int available() throws IOException {
		doCheck();
		return in.available();
	}

	@Override
	public void close() throws IOException {
		doCheck();
		in.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		in.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		doCheck();
		in.reset();
	}

	@Override
	public boolean markSupported() {
		return in.markSupported();
	}
}
