package org.eclipse.swt.widgets;

import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;

import tw.com.softleader.starter.enums.SwtStyle;
import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.pojo.WebappDatabase;

public class DataSourceRadio extends DependencyRadio {

	private final String database;

	public DataSourceRadio(Composite parent, WebappDatabase database, Supplier<DatasourcePage> pageSupplier) {
		this(parent, database.getName(), database.getGroupId(), database.getArtifactId(), database.getVersion(),
				database.isDft(), database.isEnabled());
		addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				DatasourcePage page = pageSupplier.get();
				Optional.ofNullable(database.getDriver()).ifPresent(page.getDriverClass()::setText);
				Optional.ofNullable(database.getUrlHint()).ifPresent(page.getUrl()::setText);
			}
		});
	}

	public DataSourceRadio(Composite parent, String database, String groupId, String artifactId, String version,
			boolean defaultSelected, boolean enabled) {
		super(parent, database, groupId, artifactId, version, null, SwtStyle.RADIO.swt, defaultSelected, enabled);
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

}
