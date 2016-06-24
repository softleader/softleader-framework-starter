package tw.com.softleader.starter.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DependencyRadio;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.VersionRadio;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DependencyPage extends WizardPage implements SoftLeaderStarterPage {

	private static final String DEPENDENCIES = "https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/template/dependencies.xml";
	private Collection<VersionRadio> versions = new ArrayList<>();
	private Map<String, Collection<DependencyRadio>> dependencyGroups = new HashMap<>();

	public DependencyPage(String title) {
		super("Dependency Page");
		setTitle(title);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 1;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(DEPENDENCIES);
			doc.getDocumentElement().normalize();
			getDependencies(composite, doc);
		} catch (Exception e) {
			throw new Error(e);
		}
		setControl(composite);
		setPageComplete(true);
	}

	void getDependencies(Composite parent, Document doc) {
		Node vrnsNode = doc.getElementsByTagName("vrns").item(0);
		if (vrnsNode.getNodeType() == Node.ELEMENT_NODE) {
			Element vrnsEle = (Element) vrnsNode;
			Group vgroup = new Group(parent, SWT.SHADOW_IN);
			vgroup.setText(vrnsEle.getAttribute("id"));
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.widthHint = SIZING_TEXT_FIELD_WIDTH;
			vgroup.setData(data);
			String layout = vrnsEle.getAttribute("l");
			if (layout == null) {
				vgroup.setLayout(new RowLayout(SWT.VERTICAL));
			} else {
				vgroup.setLayout(new RowLayout(layout.equals("v") ? SWT.VERTICAL : SWT.HORIZONTAL));
			}
			vgroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			NodeList vrns = vrnsEle.getElementsByTagName("vrn");
			IntStream.range(0, vrns.getLength()).forEach(vrnsIdx -> {
				Node vrnNode = vrns.item(vrnsIdx);
				if (vrnNode.getNodeType() == Node.ELEMENT_NODE) {
					Element vrn = (Element) vrnNode;
					String defaultSelect = vrn.getAttribute("default");
					versions.add(new VersionRadio(vgroup, vrn.getAttribute("sl"), vrn.getAttribute("io"),
							!defaultSelect.isEmpty() && Boolean.parseBoolean(defaultSelect)));
				}
			});
		}

		NodeList modules = doc.getElementsByTagName("module");
		IntStream.range(0, modules.getLength()).forEach(modulesIdx -> {
			Node moduleNode = modules.item(modulesIdx);
			if (moduleNode.getNodeType() == Node.ELEMENT_NODE) {
				Element module = (Element) moduleNode;
				Group m = new Group(parent, SWT.SHADOW_IN);
				GridData data = new GridData(GridData.FILL_HORIZONTAL);
				data.widthHint = SIZING_TEXT_FIELD_WIDTH;
				m.setLayoutData(data);
				m.setText(module.getAttribute("id"));
				String layout = module.getAttribute("l");
				if (layout == null) {
					m.setLayout(new RowLayout(SWT.VERTICAL));
				} else {
					m.setLayout(new RowLayout(layout.equals("v") ? SWT.VERTICAL : SWT.HORIZONTAL));
				}
				String multiSelected = module.getAttribute("multi");

				Collection<DependencyRadio> dependencies = new ArrayList<>();
				dependencyGroups.put(m.getText(), dependencies);

				NodeList dList = module.getElementsByTagName("d");
				IntStream.range(0, dList.getLength()).forEach(dIdx -> {
					Node dNode = dList.item(dIdx);
					if (dNode.getNodeType() == Node.ELEMENT_NODE) {
						Element d = (Element) dNode;
						String dDefaultSelect = d.getAttribute("default");
						String dVersion = d.getAttribute("v");
						if (dVersion.isEmpty()) {
							dVersion = "${softleader-framework.version}";
						}
						String dScope = d.getAttribute("s");
						String dEnabled = d.getAttribute("e");
						String dArtifact = d.getAttribute("a");
						String dGroup = d.getAttribute("g");
						dependencies.add(new DependencyRadio(m, dArtifact, dGroup, dArtifact, dVersion, dScope,
								!multiSelected.isEmpty() && Boolean.parseBoolean(multiSelected),
								!dDefaultSelect.isEmpty() && Boolean.parseBoolean(dDefaultSelect),
								dEnabled.isEmpty() || Boolean.parseBoolean(dEnabled)));
					}
				});
			}
		});
	}

	public Collection<VersionRadio> getVersions() {
		return versions;
	}

	public Map<String, Collection<DependencyRadio>> getDependencyGroups() {
		return dependencyGroups;
	}

}
