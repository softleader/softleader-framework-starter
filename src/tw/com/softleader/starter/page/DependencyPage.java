package tw.com.softleader.starter.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
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
import org.xml.sax.SAXException;

public class DependencyPage extends WizardPage {

	private static final String DEPENDENCIES = "https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/template/dependencies.xml";
	private Composite container;
	private Document doc;
	private Collection<VersionRadio> versions = new ArrayList<>();
	private Map<String, Collection<DependencyRadio>> dependencyGroups = new HashMap<>();

	public DependencyPage(String title) throws ParserConfigurationException, SAXException, IOException {
		super("Dependency Page");
		setTitle(title);
		getDependencies();
	}

	private void getDependencies() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(DEPENDENCIES);
		doc.getDocumentElement().normalize();
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		Group vgroup = new Group(container, SWT.SHADOW_IN);
		vgroup.setText("Version");
		vgroup.setLayout(new RowLayout(SWT.VERTICAL));
		NodeList vrns = doc.getElementsByTagName("vrns");
		IntStream.range(0, vrns.getLength()).forEach(vrnsIdx -> {
			Node vrnNode = vrns.item(vrnsIdx);
			if (vrnNode.getNodeType() == Node.ELEMENT_NODE) {
				Element vrn = (Element) vrnNode;
				String defaultSelect = vrn.getAttribute("default");
				versions.add(new VersionRadio(vgroup, versions, vrn.getAttribute("n"), vrn.getAttribute("n"),
						defaultSelect != null && Boolean.parseBoolean(defaultSelect)));
			}
		});

		NodeList modules = doc.getElementsByTagName("modules");
		IntStream.range(0, modules.getLength()).forEach(modulesIdx -> {
			Node moduleNode = modules.item(modulesIdx);
			if (moduleNode.getNodeType() == Node.ELEMENT_NODE) {
				Element module = (Element) moduleNode;

				Group m = new Group(container, SWT.SHADOW_IN);
				m.setText(module.getAttribute("id"));
				m.setLayout(new RowLayout(SWT.VERTICAL));
				String multiSelected = module.getAttribute("multi");

				Collection<DependencyRadio> dependencies = new ArrayList<>();
				dependencyGroups.put(m.getText(), dependencies);

				NodeList dList = module.getElementsByTagName("d");
				IntStream.range(0, dList.getLength()).forEach(dIdx -> {
					Node dNode = modules.item(dIdx);
					if (moduleNode.getNodeType() == Node.ELEMENT_NODE) {
						Element d = (Element) dNode;
						String ddefaultSelect = d.getAttribute("default");
						dependencies.add(new DependencyRadio(m, dependencies, d.getAttribute("g"),
								d.getAttribute("a"), multiSelected != null && Boolean.parseBoolean(multiSelected),
								ddefaultSelect != null && Boolean.parseBoolean(ddefaultSelect)));
					}
				});
			}
		});

		setControl(container);
		setPageComplete(true);
	}

	public Collection<VersionRadio> getVersions() {
		return versions;
	}

	public Map<String, Collection<DependencyRadio>> getDependencyGroups() {
		return dependencyGroups;
	}

}
