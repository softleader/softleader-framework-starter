package tw.com.softleader.starter.files;

import java.io.ByteArrayInputStream;

import org.eclipse.swt.widgets.DataSourceRadio;

import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class DatasourceInputStream extends ByteArrayInputStream {

	public DatasourceInputStream(ProjectDetailsPage projectDetails, DatasourcePage datasource, String source) {
		super(merge(projectDetails, datasource, source).getBytes());
	}

	private static String merge(ProjectDetailsPage projectDetails, DatasourcePage datasource, String source) {
		source = source.replace("{pkg}", projectDetails.getPkg());

		DataSourceRadio ds = datasource.getDatasources().stream().filter(DataSourceRadio::isSelected).findFirst()
				.get();
		source = source.replace("{database}", ds.getDatabase().toUpperCase());
		source = source.replace("{driverClass}", datasource.getDriverClass());
		source = source.replace("{url}", datasource.getUrl());
		source = source.replace("{username}", datasource.getUsername());
		source = source.replace("{password}", datasource.getPassword());

		return source;
	}

}
