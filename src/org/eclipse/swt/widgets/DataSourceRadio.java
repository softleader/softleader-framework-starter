package org.eclipse.swt.widgets;

public class DataSourceRadio extends DependencyRadio {

	private final String database;

	public DataSourceRadio(Composite parent, String database, String groupId, String artifactId,
			boolean defaultSelected, boolean enabled) {
		super(parent, groupId, artifactId, false, defaultSelected, enabled);
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

	public String getPomText() {
		String text = "\t\t<dependency>\n";
		text += "\t\t\t<groupId>" + getGroupId() + "</groupId>\n";
		text += "\t\t\t<artifactId>" + getArtifactId() + "</artifactId>\n";
		text += "\t\t</dependency>";
		return text;
	}

}
