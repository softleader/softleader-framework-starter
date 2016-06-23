package org.eclipse.swt.widgets;

public class DataSourceRadio extends DependencyRadio {

	private final String database;

	public DataSourceRadio(Composite parent, String database, String groupId, String artifactId,
			boolean defaultSelected) {
		super(parent, groupId, artifactId, false, defaultSelected);
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

}
