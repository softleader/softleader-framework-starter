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
		NodeList vs = doc.getElementsByTagName("vs");
		IntStream.range(0, vs.getLength()).forEach(i -> {
			Node node = vs.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;
				String defaultSelect = ele.getAttribute("default");
				versions.add(new VersionRadio(vgroup, versions, ele.getAttribute("n"), ele.getAttribute("n"),
						defaultSelect != null && Boolean.parseBoolean(defaultSelect)));
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
