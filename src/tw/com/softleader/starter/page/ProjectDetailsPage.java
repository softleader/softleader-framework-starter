package tw.com.softleader.starter.page;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.BidiUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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

import tw.com.softleader.starter.pojo.Webapp;
import tw.com.softleader.starter.util.Packages;

public class ProjectDetailsPage extends WizardPage implements SoftLeaderStarterPage {

	private final Webapp webapp;
	private InputText projectName;
	private InputText group;
	private InputText artifact;
	private InputText version;
	private InputText desc;
	private InputText pkg;
	private URI locationURI;
	private ProjectContentsLocationArea locationArea;
	private WorkingSetGroup workingSetGroup;
	private Button userDefaultMavenBasics;

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
			setDefaultMavenBasics();
			setPageComplete(validatePage());
		}
	};
	private SelectionListener defaultMavenBasicsListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			useDefaultsMaven(userDefaultMavenBasics.getSelection());
		}
	};

	public ProjectDetailsPage(String title, Webapp webapp) {
		super("Project details page");
		setTitle(title);
		this.webapp = webapp;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createProjectDetails(composite);
		createMavenBasics(composite);
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

	private void createMavenBasics(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		userDefaultMavenBasics = new Button(composite, SWT.CHECK | SWT.RIGHT);
		userDefaultMavenBasics.setText("Use default maven basics");
		userDefaultMavenBasics.setSelection(true);
		userDefaultMavenBasics.addSelectionListener(defaultMavenBasicsListener);

		composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		group = createText(composite, "Group").width(TEXT_WIDTH).listener(SWT.Modify, textModifyListener);
		artifact = createText(composite, "Artifact").width(TEXT_WIDTH).listener(SWT.Modify, textModifyListener);
		version = createText(composite, "Version").width(TEXT_WIDTH).listener(SWT.Modify, textModifyListener);
		pkg = createText(composite, "Package").width(TEXT_WIDTH).listener(SWT.Modify, textModifyListener);
		desc = createText(composite, "Description").width(TEXT_WIDTH).listener(SWT.Modify, textModifyListener);
		useDefaultsMaven(userDefaultMavenBasics.getSelection());

		Dialog.applyDialogFont(composite);
	}

	public void useDefaultsMaven(boolean useDefaults) {
		group.enabled(!useDefaults);
		artifact.enabled(!useDefaults);
		version.enabled(!useDefaults);
		// desc.enabled(!useDefaults); // desc always enabled
		pkg.enabled(!useDefaults);
		setDefaultMavenBasics();
	}

	private void setDefaultMavenBasics() {
		if (userDefaultMavenBasics.getSelection()) {
			String projectName = Optional.ofNullable(getProjectName().getValue()).orElse("");
			if (projectName.startsWith("softleader-")) {
				projectName = projectName.replaceFirst("softleader-", "");
			}
			projectName = projectName.trim();
			group.setText(webapp.getProjectGroupId());
			artifact.setText(webapp.getProjectArtifactId() + projectName.toLowerCase());
			version.setText(webapp.getProjectVersion());
			desc.setText(webapp.getProjectDesc() + projectName);
			pkg.setText(webapp.getProjectPkg() + getProjectPackage(projectName));
		}
	}

	private String getProjectPackage(String projectName) {
		if (projectName == null || projectName.trim().isEmpty()) {
			return "";
		}
		return Stream.of(projectName.replaceAll("[\\-|\\s]", "_").toLowerCase().split("\\.")).map(p -> {
			if (!Packages.test(p)) {
				p = "_" + p;
			}
			return p;
		}).collect(Collectors.joining(".", ".", ""));
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
		data.widthHint = TEXT_WIDTH;
		projectName.setLayoutData(data);
		projectName.setFont(parent.getFont());
		projectName.setSelection(0);

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

	private boolean validateMavenBasics() {
		setErrorMessage(null);
		if (getGroup().getValue() == null || getGroup().getValue().isEmpty()) {
			setMessage("Group is required");
			return false;
		}
		if (getArtifact().getValue() == null || getArtifact().getValue().isEmpty()) {
			setMessage("Artifact is required");
			return false;
		}
		if (getArtifact().getValue().endsWith("-")) {
			setMessage("Artifact must not end with '-'");
			return false;
		}
		if (getVersion().getValue() == null || getVersion().getValue().isEmpty()) {
			setMessage("Version is required");
			return false;
		}
		if (getPkg().getValue() == null || getPkg().getValue().isEmpty()) {
			setMessage("Package is required");
			return false;
		}
		if (getPkg().getValue().endsWith(".")) {
			setMessage("Package must not end with '.'");
			return false;
		}
		if (!Packages.test(getPkg().getValue())) {
			setMessage("'" + getPkg().getValue() + "' is not a valid package");
			return false;
		}

		setMessage(null);
		return true;
	}

	public InputText getGroup() {
		return group;
	}

	public InputText getArtifact() {
		return artifact;
	}

	public InputText getVersion() {
		return version;
	}

	public InputText getDesc() {
		return desc;
	}

	public InputText getPkg() {
		return pkg;
	}

	/**
	 * 將 package 的 <code>'.'</code> 轉換成 <code>'/'</code>
	 * 
	 * @return
	 */
	public String getPkgPath() {
		return getPkg().getValue().replace(".", "/");
	}

	public URI getLocationURI() {
		return locationURI;
	}

	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName().getValue());
	}

	public InputText getProjectName() {
		return projectName;
	}

	void setLocationForSelection() {
		locationArea.updateProjectName(getProjectName().getValue());
	}

	protected boolean validatePage() {
		locationURI = locationArea.getProjectLocationURI();
		return validateProjectDetails() && validateMavenBasics();
	}

	protected boolean validateProjectDetails() {
		IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

		String projectFieldContents = getProjectName().getValue();
		if (projectFieldContents == null || projectFieldContents.equals("")) { //$NON-NLS-1$
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

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName().getValue());
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
