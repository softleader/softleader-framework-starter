package tw.com.softleader.starter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.xml.sax.SAXException;

import tw.com.softleader.starter.files.F;
import tw.com.softleader.starter.files.JavaInputStream;
import tw.com.softleader.starter.files.PomInputStream;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class NewSoftLeaderProjectStarter extends Wizard implements INewWizard {

	private static final String ERROR_DIALOG = "%s\r\n\nNote that this wizard needs internet access.\r\nA more detailed error message may be found in the Eclipse errpr log.";
	public static String STARTER_URL = "https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/template/starter.xml";
	private static final String TITLE = "New SoftLeader Project";
	private WizardNewProjectCreationPage creation;
	private ProjectDetailsPage projectDetails;
	private DependencyPage dependency;
	private NewSoftLeaderProjectStarterModel model;

	public NewSoftLeaderProjectStarter() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		try {
			model = new NewSoftLeaderProjectStarterModel(STARTER_URL);
		} catch (ParserConfigurationException | SAXException | IOException | URISyntaxException e) {
			MessageDialog.openError(getShell(), "Error opening the wizard",
					String.format(ERROR_DIALOG, e.getClass().getName()));
			throw new Error(e);
		}
		// layout.forEach(System.out::println);

		creation = new WizardNewProjectCreationPage("Project Location");
		creation.setTitle(TITLE);

		projectDetails = new ProjectDetailsPage(TITLE);
		projectDetails.setPreviousPage(creation);

		dependency = new DependencyPage(TITLE, "{}");
		dependency.setPreviousPage(projectDetails);
	}

	@Override
	public boolean performFinish() {
		IProject project = null;
		try {
			project = createBaseProject(creation.getProjectName(), creation.getLocationURI());
			project.open(null);
			createMavenStructure(project);
			createFiles(project);
		} catch (Exception e) {
			MessageDialog.openError(getShell(), "Error creating the project",
					String.format(ERROR_DIALOG, e.getClass().getName()));
			try {
				project.delete(true, true, null);
			} catch (CoreException ce) {
			}
			throw new Error(e);
		}

		return true;
	}

	private IProject createBaseProject(String projectName, URI location) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!project.exists()) {
			URI projectLocation = location;
			IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				projectLocation = null;
			}

			desc.setLocationURI(projectLocation);
			project.create(desc, null);
		}
		return project;
	}

	private void createFiles(IProject project) {
		String pkg = projectDetails.getPkg();
		String pkgPath = projectDetails.getPkgPath();
		String pj = creation.getProjectName().substring(0, 1).toUpperCase() + creation.getProjectName().substring(1);
		model.stream().filter(F::isFile).forEach(f -> {
			String fullPath = f.getPath().replace("{pkg}", pkgPath).replace("{pj}", pj) + "/"
					+ f.getName().replace("{pj}", pj);
			IFile file = project.getFile(fullPath);
			String content = f.getContent().orElse("");
			try {
				if (f.isJava()) {
					file.create(new JavaInputStream(pkg, pj, content), true, null);
				} else if (f.isPOM()) {
					file.create(new PomInputStream(pj, projectDetails, dependency, content), true, null);
				} else {
					file.create(new ByteArrayInputStream(content.getBytes()), true, null);
				}
			} catch (Exception e) {
				throw new Error("Error creating " + f, e);
			}
		});
	}

	private void createMavenStructure(IProject project) {
		String pkg = projectDetails.getPkgPath();
		model.stream().filter(F::isFolder).forEach(f -> {
			try {
				String path = f.getPath().replace("{pkg}", pkg);
				createFolder(project.getFolder(path));
				System.out.println("Folder [" + path + "] created");
			} catch (Exception e) {
				throw new Error("Error creating " + f, e);
			}
		});
	}

	private void createFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(creation);
		addPage(projectDetails);
		addPage(dependency);
	}

}
