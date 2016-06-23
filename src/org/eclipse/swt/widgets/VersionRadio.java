package org.eclipse.swt.widgets;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class VersionRadio extends Button {

	private final String softleaderFramework;
	private final String ioPlatform;
	private boolean selected;

	public VersionRadio(Composite parent, Collection<VersionRadio> versions, String softleaderFramework,
			String ioPlatform, boolean defaultSelected) {
		super(parent, SWT.RADIO);
		setText(softleaderFramework);
		setSelection(defaultSelected);
		this.softleaderFramework = softleaderFramework;
		this.ioPlatform = ioPlatform;
		addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				versions.forEach(v -> v.selected = false);
				selected = true;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
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
}
