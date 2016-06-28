package org.eclipse.swt.widgets;

import java.util.function.Supplier;

import org.eclipse.swt.SWT;

import tw.com.softleader.starter.pojo.Database;
import tw.com.softleader.starter.pojo.Group.Style;

public class DataSourceRadio extends DependencyRadio {

	private final String database;

	public DataSourceRadio(Composite parent, Database database, Supplier<InputText> driverClassText) {
		this(parent, database.getName(), database.getGroup(), database.getArtifact(), database.getVersion(),
				database.isDft(), database.isEnabled());
		if (database.isDft()) {
			driverClassText.get().setText(database.getDriver());
		}
		addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (database.getDriver() != null && !database.getDriver().isEmpty()) {
					driverClassText.get().setText(database.getDriver());
				}
			}
		});
	}

	public DataSourceRadio(Composite parent, String database, String groupId, String artifactId, String version,
			boolean defaultSelected, boolean enabled) {
		super(parent, database, groupId, artifactId, version, null, Style.RADIO.swt, defaultSelected, enabled);
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

}
