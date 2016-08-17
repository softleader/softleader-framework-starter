package tw.com.softleader.starter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
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

import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;
import tw.com.softleader.starter.page.SiteInfoPage;
import tw.com.softleader.starter.pojo.Snippet;

public class NewSoftLeaderWebappStarterModel {

	private ProjectDetailsPage projectDetails;
	private DependencyPage dependency;
	private DatasourcePage datasource;
	private SiteInfoPage siteInfo;

	public NewSoftLeaderWebappStarterModel(ProjectDetailsPage projectDetails, DependencyPage dependency,
			DatasourcePage datasource, SiteInfoPage siteInfo) {
		super();
		this.projectDetails = projectDetails;
		this.dependency = dependency;
		this.datasource = datasource;
		this.siteInfo = siteInfo;
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

			desc.setName(projectDetails.getArtifact().getValue());

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
				.filter(DependencyRadio::isSelected).collect(Collectors.toList());

		SubMonitor subMonitor = SubMonitor.convert(monitor, selecteds.size() * 2 + 1);
		subMonitor.setTaskName("Importing snippet");

		Snippet snippet;
		try {
			snippet = new Snippet(projectDetails, dependency, datasource, siteInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(e);
		} finally {
			subMonitor.worked(1);
		}
		SubMonitor snippetMonitor = SubMonitor.convert(monitor, snippet.getDirs().size() + snippet.getSrcs().size());
		createFolders(project, snippet.getDirs(), snippetMonitor);
		createSources(project, snippet.getSrcs(), snippetMonitor);
	}

	private void createSources(IProject project, Map<String, String> sources, SubMonitor monitor) throws CoreException {
		for (Entry<String, String> source : sources.entrySet()) {
			SubMonitor subMonitor = monitor.newChild(1, SubMonitor.SUPPRESS_NONE);
			String path = source.getKey();
			IFile file = project.getFile(path);
			String content = source.getValue();
			if (content == null || content.isEmpty()) {
				subMonitor.worked(1);
			} else {
				file.create(new ByteArrayInputStream(content.getBytes()), true, subMonitor);
			}
		}
	}

	private void createFolders(IProject project, Collection<String> folders, SubMonitor monitor) throws CoreException {
		for (String folder : folders) {
			createFolder(project.getFolder(folder), monitor.newChild(1, SubMonitor.SUPPRESS_NONE));
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