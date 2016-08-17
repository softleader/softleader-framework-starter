package tw.com.softleader.starter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;

import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;
import tw.com.softleader.starter.page.SiteInfoPage;
import tw.com.softleader.starter.pojo.Webapp;

public class NewSoftLeaderWebappStarter extends Wizard implements INewWizard {

	// public static final String STARTER = "https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/resources/starter.json";
	public static final String STARTER = "http://118.163.91.249/starter/webapp";
	private static final String RELEASES = "https://github.com/softleader/softleader-framework-starter/releases";
	private static final String ERROR_DIALOG = "%s\r\n\nNote that this wizard needs internet access.\r\nA more detailed error message may be found in the Eclipse errpr log.";
	private static final String TITLE = "New SoftLeader Webapp";
	private ProjectDetailsPage projectDetails;
	private DependencyPage dependency;
	private DatasourcePage datasource;
	private SiteInfoPage siteInfo;
	private NewSoftLeaderWebappStarterModel model;

	public NewSoftLeaderWebappStarter() throws MalformedURLException {
		setDefaultPageImageDescriptor(
				ImageDescriptor.createFromURL(new URL("https://avatars2.githubusercontent.com/u/18475967?v=3&s=40")));
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// layout.forEach(System.out::println);

		Webapp webapp;
		try {
			webapp = Webapp.fromUrl(STARTER);
		} catch (IOException e) {
			MessageDialog.openError(getShell(), "Error opening the wizard",
					String.format(ERROR_DIALOG, e.getMessage()));
			throw new Error(e);
		}

		if (!webapp.isUpToDate()) {
			boolean ok = MessageDialog.openConfirm(getShell(), "Update Available",
					"A new version of SoftLeader Starter is available, press OK to download.");
			if (ok) {
				try {
					openBrowser(RELEASES);
				} catch (PartInitException | MalformedURLException e) {
					MessageDialog.openInformation(getShell(), "Could not open browser",
							"Please get the latest version from '" + RELEASES + "'");
				}
			} else {
				MessageDialog.openWarning(getShell(), "Caution", "Project creates may fail with an older version!");
			}
		}

		projectDetails = new ProjectDetailsPage(TITLE, webapp);

		dependency = new DependencyPage(TITLE, webapp);
		dependency.setPreviousPage(projectDetails);

		datasource = new DatasourcePage(TITLE, webapp);
		datasource.setPreviousPage(dependency);

		siteInfo = new SiteInfoPage(TITLE, webapp);
		siteInfo.setPreviousPage(datasource);

		model = new NewSoftLeaderWebappStarterModel(projectDetails, dependency, datasource, siteInfo);
	}

	@SuppressWarnings("restriction")
	private void openBrowser(String location) throws MalformedURLException, PartInitException {
		URL url = new URL(location);
		try {
			PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url);
		} catch (PartInitException e) {
			int flags = IWorkbenchBrowserSupport.AS_EDITOR | IWorkbenchBrowserSupport.LOCATION_BAR
					| IWorkbenchBrowserSupport.NAVIGATION_BAR;
			if (!WorkbenchBrowserSupport.getInstance().isInternalWebBrowserAvailable()) {
				flags |= IWorkbenchBrowserSupport.AS_EXTERNAL | IWorkbenchBrowserSupport.LOCATION_BAR
						| IWorkbenchBrowserSupport.NAVIGATION_BAR;
			}
			WorkbenchBrowserSupport.getInstance().createBrowser(flags, UUID.randomUUID().toString(), null, null)
					.openURL(url);
		}
	}

	@Override
	public boolean performFinish() {
		Job job = new Job("Importing SoftLeader Webapp Project") {
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
		addPage(siteInfo);
	}

}
