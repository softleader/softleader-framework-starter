package tw.com.softleader.starter.enums;

import org.eclipse.swt.SWT;

public enum SwtLayout {
	/**
	 * VERTICAL
	 */
	V(SWT.VERTICAL),
	/**
	 * HORIZONTAL
	 */
	H(SWT.HORIZONTAL);

	SwtLayout(int swt) {
		this.swt = swt;
	}

	public final int swt;
}
