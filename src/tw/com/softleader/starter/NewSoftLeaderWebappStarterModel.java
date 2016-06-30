package tw.com.softleader.starter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.core.internal.events.BuildCommand;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.swt.widgets.DependencyRadio;

import tw.com.softleader.starter.io.ComponentInputStream;
import tw.com.softleader.starter.io.DatasourceInputStream;
import tw.com.softleader.starter.io.JavaInputStream;
import tw.com.softleader.starter.io.PomInputStream;
import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;
import tw.com.softleader.starter.pojo.Snippet;
import tw.com.softleader.starter.pojo.Source;
import tw.com.softleader.starter.pojo.Starter;

public class NewSoftLeaderWebappStarterModel {

	private Starter starter;
	private ProjectDetailsPage projectDetails;
	private DependencyPage dependency;
	private DatasourcePage datasource;

	public NewSoftLeaderWebappStarterModel(Starter starter, ProjectDetailsPage projectDetails,
			DependencyPage dependency, DatasourcePage datasource) {
		super();
		this.starter = starter;
		this.projectDetails = projectDetails;
		this.dependency = dependency;
		this.datasource = datasource;
	}

	public void performFinish(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException, CoreException, MalformedURLException, IOException {
		IProject project = createProject(monitor);
		createSnippet(project, monitor);
		project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
	}

	private IProject createProject(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 2);
		subMonitor.setTaskName("Creating project");
		IProject project = projectDetails.getProjectHandle();
		if (!project.exists()) {
			URI projectLocation = projectDetails.getLocationURI();
			IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
			if (projectDetails.getLocationURI() != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI()
					.equals(projectDetails.getLocationURI())) {
				projectLocation = null;
			}
			desc.setLocationURI(projectLocation);

			desc.setName(projectDetails.getArtifact());

			desc.setBuildSpec(new ICommand[] { command("org.eclipse.wst.common.project.facet.core.builder"),
					command("org.eclipse.jdt.core.javabuilder"), command("org.eclipse.m2e.core.maven2Builder") });

			desc.setNatureIds(new String[] { "org.eclipse.wst.common.project.facet.core.nature",
					"org.eclipse.jdt.core.javanature", "org.eclipse.m2e.core.maven2Nature",
					"org.eclipse.wst.common.modulecore.ModuleCoreNature" });

			project.create(desc, subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE));
			project.open(subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE));
		}
		subMonitor.setWorkRemaining(0);
		return project;
	}

	@SuppressWarnings("restriction")
	private ICommand command(String builderName) {
		BuildCommand command = new BuildCommand();
		command.setBuilderName(builderName);
		return command;
	}

	private void createSnippet(IProject project, IProgressMonitor monitor)
			throws MalformedURLException, IOException, CoreException {
		Collection<DependencyRadio> selecteds = dependency.getModules().values().stream().flatMap(Collection::stream)
				.filter(DependencyRadio::isSelected).filter(DependencyRadio::hasAnySnippet)
				.collect(Collectors.toList());

		SubMonitor subMonitor = SubMonitor.convert(monitor, selecteds.size());
		subMonitor.setTaskName("Importing snippet");
		selecteds.stream().forEach(selected -> {
			try {
				String snippetUrl = starter.getBaseUrl() + "/" + selected.getSnippet();
				subMonitor.subTask("Downloading '" + snippetUrl + "'");
				Snippet snippet = Snippet.load(snippetUrl);
				subMonitor.subTask("Loading '" + selected.getSnippet() + "'");
				SubMonitor snippetMonitor = SubMonitor.convert(monitor,
						snippet.getFolders().size() + snippet.getSources().size());
				createFolders(project, snippet.getFolders(), snippetMonitor);
				createSources(project, snippet.getSources(), snippetMonitor);
			} catch (Exception e) {
				throw new Error(e);
			} finally {
				subMonitor.worked(1);
			}
		});
	}

	private void createSources(IProject project, Collection<Source> sources, SubMonitor monitor) throws CoreException {
		String pkgPath = projectDetails.getPkgPath();
		for (Source source : sources) {
			SubMonitor subMonitor = monitor.newChild(1, SubMonitor.SUPPRESS_NONE);
			String path = source.getFullPath().replace("{pkg}", pkgPath);
			IFile file = project.getFile(path);
			String content = source.getContent();
			if (content == null || content.isEmpty()) {
				subMonitor.worked(1);
			} else {
				if (source.isJava()) {
					file.create(new JavaInputStream(projectDetails.getPkg(), content), true, subMonitor);
				} else if (source.isPOM()) {
					file.create(new PomInputStream(projectDetails, dependency, datasource, content), true, subMonitor);
				} else if (source.isComponent()) {
					file.create(new ComponentInputStream(projectDetails.getProjectName(), dependency, content), true,
							subMonitor);
				} else if (source.isDatasource()) {
					file.create(new DatasourceInputStream(projectDetails, datasource, content), true, subMonitor);
				} else {
					file.create(new ByteArrayInputStream(content.getBytes()), true, subMonitor);
				}
				// System.out.println("File [" + path + "] created:");
			}
		}
	}

	private void createFolders(IProject project, Collection<String> folders, SubMonitor monitor) throws CoreException {
		String pkg = projectDetails.getPkgPath();
		for (String folder : folders) {
			String path = folder.replace("{pkg}", pkg);
			createFolder(project.getFolder(path), monitor.newChild(1, SubMonitor.SUPPRESS_NONE));
			// System.out.println("Folder [" + path + "] created");
		}
	}

	private void createFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent, new NullProgressMonitor());
		}
		if (!folder.exists()) {
			folder.create(false, true, monitor);
		}
	}

}