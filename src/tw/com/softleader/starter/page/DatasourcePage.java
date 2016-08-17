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

import tw.com.softleader.starter.pojo.Webapp;
import tw.com.softleader.starter.pojo.WebappDatabase;

public class DatasourcePage extends WizardPage implements SoftLeaderStarterPage {

	private Collection<DataSourceRadio> datasources = new ArrayList<>();
	private InputText driverClass;
	private InputText url;
	// private Label urlHint;
	private InputText username;
	private InputText password;
	private Webapp webapp;

	private Listener textModifyListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			setPageComplete(validatePage());
		}
	};

	public DatasourcePage(String title, Webapp webapp) {
		super("Datasource Page");
		setTitle(title);
		this.webapp = webapp;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(composite, SWT.NONE);
		label.setText(webapp.getDatabaseText());
		label.setFont(parent.getFont());

		Group group = new Group(composite, SWT.SHADOW_IN);
		group.setLayout(new RowLayout(webapp.getDatabaseLayout().swt));
		webapp.getDatabases().stream().map(d -> new DataSourceRadio(group, d, () -> this)).forEach(datasources::add);

		Optional<WebappDatabase> defaultDatabase = webapp.getDatabases().stream().filter(WebappDatabase::isDft)
				.findFirst();
		driverClass = createText(composite, "Driver Class")
				.text(defaultDatabase.map(WebappDatabase::getDriver).orElse("")).width(TEXT_WIDTH)
				.listener(SWT.Modify, textModifyListener);
		url = createText(composite, "Url").width(TEXT_WIDTH)
				.text(defaultDatabase.map(WebappDatabase::getUrlHint).orElse(""))
				.listener(SWT.Modify, textModifyListener);
		// urlHint = createUrlHint(composite,
		// defaultDatabase.map(Database::getUrlHint).orElse(""));
		username = createText(composite, "Username").width(TEXT_WIDTH).listener(SWT.Modify, textModifyListener);
		password = createText(composite, "Password").width(TEXT_WIDTH).listener(SWT.Modify, textModifyListener);

		setControl(composite);
		setPageComplete(false);
		setMessage(null);
		Dialog.applyDialogFont(composite);
	}

	private Label createUrlHint(Composite parent, String initialValue) {
		Label urlLabel = new Label(parent, SWT.NONE);
		urlLabel.setText("");
		Label urlHint = new Label(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = TEXT_WIDTH;
		urlHint.setLayoutData(data);
		urlHint.setText(initialValue);
		return urlHint;
	}

	private boolean validatePage() {
		setErrorMessage(null);
		if (getDriverClass().getValue() == null || getDriverClass().getValue().isEmpty()) {
			setMessage("DriverClass is required");
			return false;
		}
		if (getUrl().getValue() == null || getUrl().getValue().isEmpty()) {
			setMessage("Url is required");
			return false;
		}
		if (getUsername().getValue() == null || getUsername().getValue().isEmpty()) {
			setMessage("Username is required");
			return false;
		}
		setMessage(null);
		return true;
	}

	public Collection<DataSourceRadio> getDatasources() {
		return datasources;
	}

	public InputText getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(InputText driverClass) {
		this.driverClass = driverClass;
	}

	public InputText getUrl() {
		return url;
	}

	// public Label getUrlHint() {
	// return urlHint;
	// }

	public InputText getUsername() {
		return username;
	}

	public InputText getPassword() {
		return password;
	}

}
