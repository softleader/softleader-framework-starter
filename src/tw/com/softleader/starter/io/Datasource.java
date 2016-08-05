package tw.com.softleader.starter.io;

import java.util.Optional;

import org.eclipse.swt.widgets.DataSourceRadio;

import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class Datasource extends SnippetSource {

	protected final DatasourcePage datasource;

	public Datasource(ProjectDetailsPage projectDetails, DatasourcePage datasource) {
		super(projectDetails);
		this.datasource = datasource;
	}

	@Override
	public byte[] apply(String source) {
		DataSourceRadio ds = datasource.getDatasources().stream().filter(DataSourceRadio::isSelected).findFirst().get();
		source = source.replace("{database}", ds.getDatabase().toUpperCase());
		source = source.replace("{driverClass}", datasource.getDriverClass().getValue());
		source = source.replace("{url}", datasource.getUrl().getValue());
		source = source.replace("{username}", datasource.getUsername().getValue());
		source = source.replace("{password}", Optional.ofNullable(datasource.getPassword().getValue()).orElse(""));

		return super.apply(source);
	}

}
