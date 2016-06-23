package org.eclipse.swt.widgets;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class DependencyRadio extends Button {

	private final String groupId;
	private final String artifactId;
	private boolean selected;

	public DependencyRadio(Composite parent, Collection<DependencyRadio> dependencies, String groupId,
			String artifactId, boolean multiSelected, boolean defaultSelected) {
		super(parent, multiSelected ? SWT.CHECK : SWT.RADIO);
		setText(artifactId);
		setSelection(defaultSelected);
		this.groupId = groupId;
		this.artifactId = artifactId;
		addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				dependencies.forEach(v -> v.selected = false);
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

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getPomText() {
		String text = "\t\t<dependency>\n";
		text += "\t\t\t<groupId>" + getGroupId() + "</groupId>\n";
		text += "\t\t\t<artifactId>" + getArtifactId() + "</artifactId>\n";
		text += "\t\t\t<version>${softleader-framework.version}</version>\n";
		text += "\t\t</dependency>\n";
		return text;
	}

}
