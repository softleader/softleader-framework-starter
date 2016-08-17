package org.eclipse.swt.widgets;

import java.util.Optional;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import tw.com.softleader.starter.enums.MvnScope;
import tw.com.softleader.starter.enums.SwtStyle;
import tw.com.softleader.starter.pojo.WebappDependency;

public class DependencyRadio extends Button {

	public static final String SOFTLEADER_FRAMEOWK_VERISON = "${softleader-framework.version}";

	private final String groupId;
	private final String artifactId;
	private final String version;
	private final MvnScope scope;
	private boolean selected;

	public DependencyRadio(Composite parent, SwtStyle style, WebappDependency dependency) {
		this(parent, dependency.getArtifactId(), dependency.getGroupId(), dependency.getArtifactId(),
				dependency.getVersion(), dependency.getScope(), style.swt, dependency.isDft(), dependency.isEnabled());
	}

	public DependencyRadio(Composite parent, String text, String groupId, String artifactId, String version,
			MvnScope scope, int style, boolean defaultSelected, boolean enabled) {
		super(parent, style);
		setText(text);
		setSelection(defaultSelected);
		setEnabled(enabled);
		this.groupId = groupId;
		this.artifactId = artifactId;
		if ("tw.com.softleader".equals(getGroupId()) && (version == null || version.isEmpty())) {
			version = SOFTLEADER_FRAMEOWK_VERISON;
		}
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
		return Optional.ofNullable(scope).map(Object::toString).map(String::toLowerCase).orElse(null);

	}

}
