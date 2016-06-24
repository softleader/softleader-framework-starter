package tw.com.softleader.starter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.xml.sax.SAXException;

import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class NewSoftLeaderWebappStarter extends Wizard implements INewWizard {

	private static final String ERROR_DIALOG = "%s\r\n\nNote that this wizard needs internet access.\r\nA more detailed error message may be found in the Eclipse errpr log.";
	private static final String TITLE = "New SoftLeader Webapp";
	private ProjectDetailsPage projectDetails;
	private DependencyPage dependency;
	private DatasourcePage datasource;
	private NewSoftLeaderWebappStarterModel model;

	public NewSoftLeaderWebappStarter() throws MalformedURLException {
		setDefaultPageImageDescriptor(
				ImageDescriptor.createFromURL(new URL("https://avatars2.githubusercontent.com/u/18475967?v=3&s=40")));
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// layout.forEach(System.out::println);

		projectDetails = new ProjectDetailsPage(TITLE);

		dependency = new DependencyPage(TITLE);
		dependency.setPreviousPage(projectDetails);

		datasource = new DatasourcePage(TITLE);
		datasource.setPreviousPage(dependency);

		try {
			model = new NewSoftLeaderWebappStarterModel(projectDetails, dependency, datasource);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MessageDialog.openError(getShell(), "Error opening the wizard",
					String.format(ERROR_DIALOG, e.getMessage()));
			throw new Error(e);
		}
	}

	@Override
	public boolean performFinish() {
		Job job = new Job("Import SoftLeader Webapp Content") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					model.performFinish(monitor);
					return Status.OK_STATUS;
				} catch (Throwable e) {
					e.printStackTrace();
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MessageDialog.openError(getShell(), "Error creating the project",
									String.format(ERROR_DIALOG, e.getMessage()));
						}
					});
					return Status.CANCEL_STATUS;
				}
			}
		};
		job.setPriority(Job.BUILD);
		job.setUser(true); // shows progress in default eclipse config
		job.schedule();
		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(projectDetails);
		addPage(dependency);
		addPage(datasource);
	}

}
