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
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.xml.sax.SAXException;

import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class NewSoftLeaderProjectStarter extends Wizard implements INewWizard {

	private static final String ERROR_DIALOG = "%s\r\n\nNote that this wizard needs internet access.\r\nA more detailed error message may be found in the Eclipse errpr log.";
	private static final String TITLE = "New SoftLeader Project";
	private WizardNewProjectCreationPage creation;
	private ProjectDetailsPage projectDetails;
	private DependencyPage dependency;
	private NewSoftLeaderProjectStarterModel model;

	public NewSoftLeaderProjectStarter() throws MalformedURLException {
		setDefaultPageImageDescriptor(
				ImageDescriptor.createFromURL(new URL("https://avatars2.githubusercontent.com/u/18475967?v=3&s=40")));
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// layout.forEach(System.out::println);

		creation = new WizardNewProjectCreationPage("Project Location");
		creation.setTitle(TITLE);

		projectDetails = new ProjectDetailsPage(TITLE);
		projectDetails.setPreviousPage(creation);

		dependency = new DependencyPage(TITLE);
		dependency.setPreviousPage(projectDetails);

		try {
			model = new NewSoftLeaderProjectStarterModel(projectDetails, dependency);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MessageDialog.openError(getShell(), "Error opening the wizard",
					String.format(ERROR_DIALOG, e.getMessage()));
			throw new Error(e);
		}
	}

	@Override
	public boolean performFinish() {
		String projectName = creation.getProjectName();
		URI locationURI = creation.getLocationURI();
		Job job = new Job("Import Getting Started Content") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					model.performFinish(projectName, locationURI, monitor);
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
		addPage(creation);
		addPage(projectDetails);
		addPage(dependency);
	}

}
