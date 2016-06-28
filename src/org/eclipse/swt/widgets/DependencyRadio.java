package org.eclipse.swt.widgets;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import tw.com.softleader.starter.pojo.Dependency;
import tw.com.softleader.starter.pojo.Group.Style;

public class DependencyRadio extends Button {

	private final String groupId;
	private final String artifactId;
	private final String version;
	private final String scope;
	private boolean selected;

	public DependencyRadio(Composite parent, Style style, Dependency dependency) {
		this(parent, dependency.getArtifact(), dependency.getGroup(), dependency.getArtifact(), dependency.getVersion(),
				dependency.getScope(), style.swt, dependency.isDft(), dependency.isEnabled());
	}

	public DependencyRadio(Composite parent, String text, String groupId, String artifactId, String version,
			String scope, int style, boolean defaultSelected, boolean enabled) {
		super(parent, style);
		setText(text);
		setSelection(defaultSelected);
		setEnabled(enabled);
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.scope = scope;
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

	public String getVersion() {
		return version;
	}

	public String getScope() {
		return scope;
	}

	public String getPomText() {
		String text = "\t\t<dependency>\n";
		text += "\t\t\t<groupId>" + getGroupId() + "</groupId>\n";
		text += "\t\t\t<artifactId>" + getArtifactId() + "</artifactId>\n";
		if (getVersion() != null && !getVersion().isEmpty()) {
			text += "\t\t\t<version>" + getVersion() + "</version>\n";
		}
		if (getScope() != null && !getScope().isEmpty()) {
			text += "\t\t\t<scope>" + getScope() + "</scope>\n";
		}
		text += "\t\t</dependency>";
		return text;
	}

	public String getComponentText(String version) {
		String text = "\t\t<dependent-module archiveName=\"" + getArtifactId() + "-" + version
				+ ".jar\" deploy-path=\"/WEB-INF/lib\" handle=\"module:/resource/" + getArtifactId() + "/"
				+ getArtifactId() + "\">\n";
		text += "\t\t\t<dependency-type>uses</dependency-type>\n";
		text += "\t\t</dependent-module>";
		return text;
	}

}
