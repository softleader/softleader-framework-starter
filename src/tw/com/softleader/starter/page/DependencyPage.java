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

import tw.com.softleader.starter.pojo.Webapp;

public class DependencyPage extends WizardPage implements SoftLeaderStarterPage {

	private Collection<VersionRadio> versions = new ArrayList<>();
	private Map<String, Collection<DependencyRadio>> modules = new HashMap<>();
	private Webapp webapp;

	public DependencyPage(String title, Webapp webapp) {
		super("Dependency Page");
		setTitle(title);
		this.webapp = webapp;
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
		group.setText(webapp.getVersionText());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = TEXT_WIDTH;
		group.setData(data);
		group.setLayout(new RowLayout(webapp.getVersionLayout().swt));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		webapp.getVersions().stream().map(v -> new VersionRadio(group, v)).forEach(versions::add);
	}

	private void setDependencies(Composite parent) {
		webapp.getModules().forEach(module -> {
			Group group = new Group(parent, SWT.SHADOW_IN);
			group.setText(module.getDependencyText());
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.widthHint = TEXT_WIDTH;
			group.setData(data);
			group.setLayout(new RowLayout(module.getDependencyStyle().swt));
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			modules.putIfAbsent(group.getText(), module.getDependencies().stream()
					.map(d -> new DependencyRadio(group, module.getDependencyStyle(), d)).collect(Collectors.toList()));
		});
	}

	public Collection<VersionRadio> getVersions() {
		return versions;
	}

	public Map<String, Collection<DependencyRadio>> getModules() {
		return modules;
	}

}
