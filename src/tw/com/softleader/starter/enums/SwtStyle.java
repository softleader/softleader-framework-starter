package tw.com.softleader.starter.enums;

import org.eclipse.swt.SWT;

public enum SwtStyle {

	CHECK(SWT.CHECK), RADIO(SWT.RADIO),;

	SwtStyle(int swt) {
		this.swt = swt;
	}

	public final int swt;
}
