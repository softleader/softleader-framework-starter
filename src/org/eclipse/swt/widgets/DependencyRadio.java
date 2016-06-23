package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

public class DependencyRadio extends Button {

	private final String groupId;
	private final String artifactId;
	private boolean selected;

	public DependencyRadio(Composite parent, String groupId, String artifactId, boolean multiSelected,
			boolean defaultSelected) {
		super(parent, multiSelected ? SWT.CHECK : SWT.RADIO);
		setText(artifactId);
		setSelection(defaultSelected);
		this.groupId = groupId;
		this.artifactId = artifactId;
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
		text += "\t\t</dependency>";
		return text;
	}

	public String getComponentText(String version) {
		String text = "\t\t<dependent-module archiveName=\"" + getArtifactId() + "-" + version
				+ ".jar\" deploy-path=\"/WEB-INF/lib\" handle=\"module:/resource/" + getArtifactId() + "/"
				+ getArtifactId() + "\">";
		text += "\t\t\t<dependency-type>uses</dependency-type>";
		text += "\t\t</dependent-module>";
		return text;
	}

}
