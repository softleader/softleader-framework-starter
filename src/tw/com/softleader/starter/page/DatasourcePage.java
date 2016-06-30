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

	private Collection<DataSourceRadio> datasources = new ArrayList<>();
	private InputText driverClass;
	private InputText url;
	private Label urlHint;
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
		starter.getDatabase().getData().stream().map(d -> new DataSourceRadio(group, d, () -> this))
				.forEach(datasources::add);

		Optional<Database> defaultDatabase = starter.getDatabase().getData().stream().filter(Database::isDft)
				.findFirst();
		driverClass = createText(composite, "Driver Class", defaultDatabase.map(Database::getDriver).orElse(""),
				TEXT_WIDTH, textModifyListener);
		url = createText(composite, "Url", "", TEXT_WIDTH, textModifyListener);
		urlHint = createUrlHint(composite, defaultDatabase.map(Database::getUrlHint).orElse(""));
		username = createText(composite, "Username", "", TEXT_WIDTH, textModifyListener);
		password = createText(composite, "Password", "", TEXT_WIDTH, textModifyListener);

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
		if (getDriverClass().getValue().isEmpty()) {
			setMessage("DriverClass is required");
			return false;
		}
		if (getUrl().getValue().isEmpty()) {
			setMessage("Url is required");
			return false;
		}
		if (getPassword().getValue().isEmpty()) {
			setMessage("Password is required");
			return false;
		}
		if (getUsername().getValue().isEmpty()) {
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

	public Label getUrlHint() {
		return urlHint;
	}

	public InputText getUsername() {
		return username;
	}

	public InputText getPassword() {
		return password;
	}

}
