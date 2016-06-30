package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import tw.com.softleader.starter.pojo.Version;

public class VersionRadio extends Button {

	private final String softleaderFramework;
	private final String ioPlatform;
	private boolean selected;

	public VersionRadio(Composite parent, Version version) {
		super(parent, SWT.RADIO);
		setText(version.getSl());
		setSelection(version.isDft());
		setEnabled(version.isEnabled());
		this.softleaderFramework = version.getSl();
		this.ioPlatform = version.getIo();
		this.selected = version.isDft();
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				selected = getSelection();
			}
		});
	}

	public boolean isSelected() {
		return selected;
	}

	public String getSoftleaderFramework() {
		return softleaderFramework;
	}

	public String getIoPlatform() {
		return ioPlatform;
	}

	@Override
	public String toString() {
		return "VersionRadio [softleaderFramework=" + softleaderFramework + ", ioPlatform=" + ioPlatform + ", selected="
				+ selected + "]";
	}

}
