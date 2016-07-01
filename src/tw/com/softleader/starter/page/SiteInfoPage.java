package tw.com.softleader.starter.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.InputText;
import org.eclipse.swt.widgets.Listener;

import tw.com.softleader.starter.pojo.Starter;

public class SiteInfoPage extends WizardPage implements SoftLeaderStarterPage {

	private final Starter starter;
	private InputText baseUrl;

	private Listener textModifyListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			setPageComplete(validatePage());
		}
	};

	public SiteInfoPage(String title, Starter starter) {
		super("Project details page");
		setTitle(title);
		this.starter = starter;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createSiteInfo(composite);
		setPageComplete(true);
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
	}

	private void createSiteInfo(Composite parent) {
		Group composite = new Group(parent, SWT.SHADOW_IN);
		composite.setText("Site Info");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		composite.setData(data);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		baseUrl = createText(composite, "Base Url").text(starter.getBaseUrl()).width(TEXT_WIDTH).listener(SWT.Modify,
				textModifyListener);
	}

	private boolean validatePage() {
		setErrorMessage(null);
		if (getBaseUrl().isEmpty()) {
			setMessage("BaseUrl is required");
			return false;
		}

		setMessage(null);
		return true;
	}

	public String getBaseUrl() {
		return baseUrl.getValue();
	}

}
