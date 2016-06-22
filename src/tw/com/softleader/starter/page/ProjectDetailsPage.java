package tw.com.softleader.starter.page;

import java.util.Optional;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.BidiUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class ProjectDetailsPage extends WizardPage {

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	private Text group;
	private Text artifact;
	private Text version;
	private Text desc;
	private Text pkg;

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

	private Text createText(Composite parent, String labelText, String initialValue) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		label.setFont(parent.getFont());

		Text text = new Text(parent, SWT.BORDER);
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
		return Optional.ofNullable(group).map(Text::getText).map(String::trim).orElse("");
	}

	public String getArtifact() {
		return Optional.ofNullable(artifact).map(Text::getText).map(String::trim).orElse("");
	}

	public String getVersion() {
		return Optional.ofNullable(version).map(Text::getText).map(String::trim).orElse("");
	}

	public String getDesc() {
		return Optional.ofNullable(desc).map(Text::getText).map(String::trim).orElse("");
	}

	public String getPkg() {
		return Optional.ofNullable(pkg).map(Text::getText).map(String::trim).orElse("");
	}

	public String getPkgPath() {
		return getPkg().replace(".", "/");
	}

}
