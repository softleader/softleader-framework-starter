package tw.com.softleader.starter.io;

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
		source = source.replaceAll("\\{database\\}", ds.getDatabase().toUpperCase());
		source = source.replaceAll("\\{driverClass\\}", datasource.getDriverClass().getValue());
		source = source.replaceAll("\\{url\\}", datasource.getUrl().getValue());
		source = source.replaceAll("\\{username\\}", datasource.getUsername().getValue());
		source = source.replaceAll("\\{password\\}", datasource.getPassword().getValue());

		return super.apply(source);
	}

}
