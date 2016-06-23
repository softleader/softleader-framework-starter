package org.eclipse.swt.widgets;

public class DataSourceRadio extends DependencyRadio {

	private final String database;

	public DataSourceRadio(Composite parent, String database, String groupId, String artifactId, String version,
			boolean defaultSelected, boolean enabled) {
		super(parent, database, groupId, artifactId, version, null, false, defaultSelected, enabled);
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

}
