package tw.com.softleader.starter.page;

import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.BidiUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.InputText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WorkingSetGroup;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter;

public class ProjectDetailsPage extends WizardPage {

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;
	private static final String DEFAULT_GROUP = "tw.com.softleader";
	private static final String DEFAULT_ARTIFACT = "softleader-project";
	private static final String DEFAULT_DESCRIPTION = "SoftLeader Project";
	private static final String DEFAULT_PACKAGE = "tw.com.softleader.";
	private static final String DEFAULT_VERSION = "0.0.1-SNAPSHOT";
	private InputText projectName;
	private InputText group;
	private InputText artifact;
	private InputText version;
	private InputText desc;
	private InputText pkg;
	private URI locationURI;
	private ProjectContentsLocationArea locationArea;
	private WorkingSetGroup workingSetGroup;

	private Listener textModifyListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			setPageComplete(validatePage());
		}
	};
	private Listener nameModifyListener = new Listener() {
		@Override
		public void handleEvent(Event e) {
			setLocationForSelection();
			setPageComplete(validatePage());
		}
	};

	public ProjectDetailsPage(String title) {
		super("Project details page");
		setTitle(title);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createProjectDetails(composite);
		createMavenDetails(composite);
		setPageComplete(false);
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
	}

	private void createProjectDetails(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		initializeDialogUnits(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createProjectNameGroup(composite);
		locationArea = new ProjectContentsLocationArea(getErrorReporter(), composite);

		// Scale the button based on the rest of the dialog
		setButtonLayoutData(locationArea.getBrowseButton());
	}

	private void createMavenDetails(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		group = createText(composite, "Group", DEFAULT_GROUP);
		artifact = createText(composite, "Artifact", DEFAULT_ARTIFACT);
		version = createText(composite, "Version", DEFAULT_VERSION);
		desc = createText(composite, "Description", DEFAULT_DESCRIPTION);
		pkg = createText(composite, "Package", DEFAULT_PACKAGE);

		Dialog.applyDialogFont(composite);
	}

	private final void createProjectNameGroup(Composite parent) {
		// project specification group
		Composite projectGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new project label
		Label projectLabel = new Label(projectGroup, SWT.NONE);
		projectLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_nameLabel);
		projectLabel.setFont(parent.getFont());

		// new project name entry field
		projectName = new InputText(projectGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		projectName.setLayoutData(data);
		projectName.setFont(parent.getFont());

		// Set the initial value first before listener
		// to avoid handling an event during the creation.
		projectName.addListener(SWT.Modify, nameModifyListener);
		BidiUtils.applyBidiProcessing(projectName, BidiUtils.BTD_DEFAULT);
	}

	private IErrorMessageReporter getErrorReporter() {
		return new IErrorMessageReporter() {
			@Override
			public void reportError(String errorMessage, boolean infoOnly) {
				if (infoOnly) {
					setMessage(errorMessage, IStatus.INFO);
					setErrorMessage(null);
				} else
					setErrorMessage(errorMessage);
				boolean valid = errorMessage == null;
				if (valid) {
					valid = validatePage();
				}

				setPageComplete(valid);
			}
		};
	}

	private InputText createText(Composite parent, String labelText, String initialValue) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		label.setFont(parent.getFont());

		InputText text = new InputText(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		text.setLayoutData(data);
		text.setFont(parent.getFont());

		// Set the initial value first before listener
		// to avoid handling an event during the creation.
		if (text != null) {
			text.setText(initialValue);
		}
		text.addListener(SWT.Modify, textModifyListener);
		BidiUtils.applyBidiProcessing(text, BidiUtils.BTD_DEFAULT);
		return text;
	}

	private boolean validateMavenDetails() {
		setErrorMessage(null);
		if (getGroup().isEmpty()) {
			setMessage("Group is required");
			return false;
		}
		if (getArtifact().isEmpty()) {
			setMessage("Artifact is required");
			return false;
		}
		if (getArtifact().endsWith("-")) {
			setMessage("Artifact must not end with '-'");
			return false;
		}
		if (getVersion().isEmpty()) {
			setMessage("Version is required");
			return false;
		}
		if (getPkg().isEmpty()) {
			setMessage("Package is required");
			return false;
		}
		if (getPkg().endsWith(".")) {
			setMessage("Package must not end with '.'");
			return false;
		}

		setMessage(null);
		return true;
	}

	public String getGroup() {
		return group.getValue();
	}

	public String getArtifact() {
		return artifact.getValue();
	}

	public String getVersion() {
		return version.getValue();
	}

	public String getDesc() {
		return desc.getValue();
	}

	public String getPkg() {
		return pkg.getValue();
	}

	/**
	 * 將 package 的 <code>'.'</code> 轉換成 <code>'/'</code>
	 * 
	 * @return
	 */
	public String getPkgPath() {
		return getPkg().replace(".", "/");
	}

	public URI getLocationURI() {
		return locationURI;
	}

	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
	}

	public String getProjectName() {
		return projectName.getValue();
	}

	void setLocationForSelection() {
		locationArea.updateProjectName(getProjectName());
	}

	protected boolean validatePage() {
		locationURI = locationArea.getProjectLocationURI();
		return validateProjectDetails() && validateMavenDetails();
	}

	protected boolean validateProjectDetails() {
		IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

		String projectFieldContents = getProjectName();
		if (projectFieldContents.equals("")) { //$NON-NLS-1$
			setErrorMessage(null);
			setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
			return false;
		}

		IStatus nameStatus = workspace.validateName(projectFieldContents, IResource.PROJECT);
		if (!nameStatus.isOK()) {
			setErrorMessage(nameStatus.getMessage());
			return false;
		}

		IProject handle = getProjectHandle();
		if (handle.exists()) {
			setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
			return false;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
		locationArea.setExistingProject(project);

		String validLocationMessage = locationArea.checkValidLocation();
		if (validLocationMessage != null) { // there is no destination location
											// given
			setErrorMessage(validLocationMessage);
			return false;
		}

		setErrorMessage(null);
		setMessage(null);
		return true;
	}

}
