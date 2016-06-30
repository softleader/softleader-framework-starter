package tw.com.softleader.starter.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DataSourceRadio;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.InputText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import tw.com.softleader.starter.pojo.Database;
import tw.com.softleader.starter.pojo.Starter;

public class DatasourcePage extends WizardPage implements SoftLeaderStarterPage {

	// private static final String DATASOURCE =
	// "https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/template/datasource.xml";
	private Collection<DataSourceRadio> datasources = new ArrayList<>();
	private InputText driverClass;
	private InputText url;
	private InputText username;
	private InputText password;
	private Starter starter;

	private Listener textModifyListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			setPageComplete(validatePage());
		}
	};

	public DatasourcePage(String title, Starter starter) {
		super("Datasource Page");
		setTitle(title);
		this.starter = starter;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(composite, SWT.NONE);
		label.setText(starter.getDatabase().getText());
		label.setFont(parent.getFont());

		Group group = new Group(composite, SWT.SHADOW_IN);
		group.setLayout(new RowLayout(starter.getDatabase().getLayout().swt));
		starter.getDatabase().getData().stream().map(d -> new DataSourceRadio(group, d, () -> driverClass))
				.forEach(datasources::add);

		Optional<String> defaultDriver = starter.getDatabase().getData().stream().filter(Database::isDft).findFirst()
				.map(Database::getDriver);
		driverClass = createText(composite, "Driver Class", defaultDriver.orElse(""), TEXT_WIDTH, textModifyListener);
		url = createText(composite, "Url", "", TEXT_WIDTH, textModifyListener);
		username = createText(composite, "Username", "", TEXT_WIDTH, textModifyListener);
		password = createText(composite, "Password", "", TEXT_WIDTH, textModifyListener);

		setControl(composite);
		setPageComplete(false);
		setMessage(null);
		Dialog.applyDialogFont(composite);
	}

	private boolean validatePage() {
		setErrorMessage(null);
		if (getDriverClass().isEmpty()) {
			setMessage("DriverClass is required");
			return false;
		}
		if (getUrl().isEmpty()) {
			setMessage("Url is required");
			return false;
		}
		if (getPassword().isEmpty()) {
			setMessage("Password is required");
			return false;
		}
		if (getUsername().isEmpty()) {
			setMessage("Username is required");
			return false;
		}
		setMessage(null);
		return true;
	}

	public Collection<DataSourceRadio> getDatasources() {
		return datasources;
	}

	public String getDriverClass() {
		return driverClass.getValue();
	}

	public String getUrl() {
		return url.getValue();
	}

	public String getPassword() {
		return password.getValue();
	}

	public String getUsername() {
		return username.getValue();
	}

}
