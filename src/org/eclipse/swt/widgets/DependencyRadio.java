package org.eclipse.swt.widgets;

import java.util.Collection;

import org.eclipse.swt.SWT;

public class DependencyRadio extends Button {

	private final String groupId;
	private final String artifactId;
	private boolean selected;

	public DependencyRadio(Composite parent, Collection<DependencyRadio> dependencies, String groupId,
			String artifactId, boolean multiSelected) {
		super(parent, multiSelected ? SWT.CHECK : SWT.RADIO);
		setText(artifactId);
		this.groupId = groupId;
		this.artifactId = artifactId;
		addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				dependencies.forEach(v -> v.selected = false);
				selected = true;
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

}
