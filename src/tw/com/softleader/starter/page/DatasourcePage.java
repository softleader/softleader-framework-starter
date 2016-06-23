package tw.com.softleader.starter.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.BidiUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DataSourceRadio;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.InputText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DatasourcePage extends WizardPage {

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;
	private static final String DATASOURCE = "https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/template/datasource.xml";
	private Collection<DataSourceRadio> datasources = new ArrayList<>();
	private InputText driverClass;
	private InputText url;
	private InputText username;
	private InputText password;

	private Listener textModifyListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			setPageComplete(validatePage());
		}
	};

	public DatasourcePage(String title) {
		super("Datasource Page");
		setTitle(title);
		setControl(driverClass);
		setControl(url);
		setControl(username);
		setControl(password);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(composite, SWT.NONE);
		label.setText("Database");
		label.setFont(parent.getFont());

		try {
			Group group = new Group(composite, SWT.SHADOW_IN);
			group.setLayout(new RowLayout(SWT.VERTICAL));

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(DATASOURCE);
			doc.getDocumentElement().normalize();
			NodeList d = doc.getElementsByTagName("d");
			IntStream.range(0, d.getLength()).forEach(i -> {
				Node node = d.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element ele = (Element) node;
					String defaultSelect = ele.getAttribute("default");
					String version = ele.getAttribute("v");
					String enabled = ele.getAttribute("e");
					String name = ele.getAttribute("n");
					String grp = ele.getAttribute("g");
					String artifact = ele.getAttribute("a");
					System.out.println(artifact);
					datasources.add(new DataSourceRadio(group, name, grp, artifact, version,
							!defaultSelect.isEmpty() && Boolean.parseBoolean(defaultSelect),
							enabled.isEmpty() || Boolean.parseBoolean(enabled)));
				}
			});
		} catch (Exception e) {
			throw new Error(e);
		}
		driverClass = createText(composite, "DriverClass", "");
		url = createText(composite, "Url", "");
		username = createText(composite, "Username", "");
		password = createText(composite, "Password", "");

		setControl(composite);
		setPageComplete(false);
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
		if (getDriverClass().isEmpty()) {
			setMessage("DriverClass is required");
			return false;
		}
		if (getUrl().isEmpty()) {
			setMessage("Url is required");
			return false;
		}
		if (getPassword().isEmpty()) {
			setMessage("Password is required");
			return false;
		}
		if (getUsername().isEmpty()) {
			setMessage("Username is required");
			return false;
		}
		setMessage(null);
		return true;
	}

	public Collection<DataSourceRadio> getDatasources() {
		return datasources;
	}

	public String getDriverClass() {
		return driverClass.getValue();
	}

	public String getUrl() {
		return url.getValue();
	}

	public String getPassword() {
		return password.getValue();
	}

	public String getUsername() {
		return username.getValue();
	}

}
