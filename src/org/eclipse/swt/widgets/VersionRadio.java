package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

public class VersionRadio extends Button {

	private final String softleaderFramework;
	private final String ioPlatform;
	private boolean selected;

	public VersionRadio(Composite parent, String softleaderFramework, String ioPlatform, boolean defaultSelected) {
		super(parent, SWT.RADIO);
		setText(softleaderFramework);
		setSelection(defaultSelected);
		this.softleaderFramework = softleaderFramework;
		this.ioPlatform = ioPlatform;
		this.selected = defaultSelected;
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
