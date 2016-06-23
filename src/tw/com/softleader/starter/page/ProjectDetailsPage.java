package tw.com.softleader.starter.page;

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

public class ProjectDetailsPage extends WizardPage {

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	private InputText group;
	private InputText artifact;
	private InputText version;
	private InputText desc;
	private InputText pkg;

	private Listener textModifyListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			setPageComplete(validatePage());
		}
	};

	public ProjectDetailsPage(String title) {
		super("Project details page");
		setPageComplete(false);
		setTitle(title);
		setControl(group);
		setControl(artifact);
		setControl(version);
		setControl(desc);
		setControl(pkg);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		group = createText(composite, "Group", "tw.com.softleader");
		artifact = createText(composite, "Artifact", "softleader-project");
		version = createText(composite, "Version", "0.0.1-SNAPSHOT");
		desc = createText(composite, "Description", "SoftLeader project");
		pkg = createText(composite, "Package", "tw.com.softleader");

		setControl(composite);
		setPageComplete(false);
		setErrorMessage(null);
		setMessage(null);
		Dialog.applyDialogFont(composite);
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

	private boolean validatePage() {
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

	public String getPkgPath() {
		return getPkg().replace(".", "/");
	}

}
