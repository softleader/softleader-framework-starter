package tw.com.softleader.starter.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DependencyRadio;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.VersionRadio;

import tw.com.softleader.starter.pojo.Starter;

public class DependencyPage extends WizardPage implements SoftLeaderStarterPage {

	private Collection<VersionRadio> versions = new ArrayList<>();
	private Map<String, Collection<DependencyRadio>> modules = new HashMap<>();
	private Starter starter;

	public DependencyPage(String title, Starter starter) {
		super("Dependency Page");
		setTitle(title);
		this.starter = starter;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 1;
		setVersions(composite);
		setDependencies(composite);
		setControl(composite);
		setPageComplete(true);
	}

	private void setVersions(Composite parent) {
		Group group = new Group(parent, SWT.SHADOW_IN);
		group.setText(starter.getVersions().getText());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		group.setData(data);
		group.setLayout(new RowLayout(starter.getVersions().getLayout().swt));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		starter.getVersions().getData().stream().map(v -> new VersionRadio(group, v)).forEach(versions::add);
	}

	private void setDependencies(Composite parent) {
		starter.getModules().forEach(module -> {
			Group group = new Group(parent, SWT.SHADOW_IN);
			group.setText(module.getText());
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.widthHint = SIZING_TEXT_FIELD_WIDTH;
			group.setData(data);
			group.setLayout(new RowLayout(module.getLayout().swt));
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			modules.putIfAbsent(group.getText(), module.getData().stream()
					.map(d -> new DependencyRadio(group, module.getStyle(), d)).collect(Collectors.toList()));
		});
	}

	public Collection<VersionRadio> getVersions() {
		return versions;
	}

	public Map<String, Collection<DependencyRadio>> getModules() {
		return modules;
	}

}
