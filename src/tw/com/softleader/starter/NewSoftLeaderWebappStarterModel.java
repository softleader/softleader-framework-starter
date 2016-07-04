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

import tw.com.softleader.starter.io.Component;
import tw.com.softleader.starter.io.Datasource;
import tw.com.softleader.starter.io.Pom;
import tw.com.softleader.starter.io.SnippetSource;
import tw.com.softleader.starter.io.WebApplicationInitializer;
import tw.com.softleader.starter.page.DatasourcePage;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;
import tw.com.softleader.starter.page.SiteInfoPage;
import tw.com.softleader.starter.pojo.Snippet;
import tw.com.softleader.starter.pojo.Source;

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
				.filter(DependencyRadio::isSelected).filter(DependencyRadio::hasAnySnippet)
				.collect(Collectors.toList());

		SubMonitor subMonitor = SubMonitor.convert(monitor, selecteds.size() * 2 + 1);
		subMonitor.setTaskName("Importing snippet");

		Collection<Snippet> snippets = selecteds.parallelStream().map(selected -> {
			try {
				String snippetUrl = siteInfo.getBaseUrl() + "/" + selected.getSnippet();
				subMonitor.subTask("Downloading " + snippetUrl);
				return Snippet.load(snippetUrl);
			} catch (Exception e) {
				throw new Error(e);
			} finally {
				subMonitor.worked(1);
			}
		}).collect(Collectors.toList());

		snippets.forEach(snippet -> {
			try {
				SubMonitor snippetMonitor = SubMonitor.convert(monitor,
						snippet.getFolders().size() + snippet.getSources().size());
				createFolders(project, snippet, snippetMonitor);
				createSources(project, snippet.getSources(), snippetMonitor);
			} catch (Exception e) {
				throw new Error(e);
			} finally {
				subMonitor.worked(1);
			}
		});

		try {
			String globalSnippetUrl = siteInfo.getBaseUrl() + "/" + projectDetails.getGlobalSnippet();
			subMonitor.subTask("Downloading " + globalSnippetUrl);
			Snippet globalSnippet = Snippet.load(globalSnippetUrl);
			createGlobalSnippet(project, globalSnippet, snippets, subMonitor);
		} finally {
			subMonitor.worked(1);
		}
	}

	private void createGlobalSnippet(IProject project, Snippet global, Collection<Snippet> snippets, SubMonitor monitor)
			throws CoreException {
		String pkgPath = projectDetails.getPkgPath();
		for (Source source : global.getSources()) {
			SubMonitor subMonitor = monitor.newChild(1, SubMonitor.SUPPRESS_NONE);
			String path = source.getFullPath().replaceAll("\\{pkgPath\\}", pkgPath);
			IFile file = project.getFile(path);
			String content = source.getContent();
			if (content == null || content.isEmpty()) {
				subMonitor.worked(1);
			} else {
				SnippetSource snippetSource;
				if (source.isPom()) {
					snippetSource = new Pom(projectDetails, dependency, datasource);
				} else if (source.isWebApplicationInitializer()) {
					snippetSource = new WebApplicationInitializer(projectDetails, snippets);
				} else if (source.isComponent()) {
					snippetSource = new Component(projectDetails, dependency);
				} else {
					snippetSource = new SnippetSource(projectDetails);
				}
				file.create(new ByteArrayInputStream(snippetSource.apply(content)), true, subMonitor);
			}
		}
	}

	private void createSources(IProject project, Collection<Source> sources, SubMonitor monitor) throws CoreException {
		String pkgPath = projectDetails.getPkgPath();
		for (Source source : sources) {
			SubMonitor subMonitor = monitor.newChild(1, SubMonitor.SUPPRESS_NONE);
			String path = source.getFullPath().replaceAll("\\{pkgPath\\}", pkgPath);
			IFile file = project.getFile(path);
			String content = source.getContent();
			if (content == null || content.isEmpty()) {
				subMonitor.worked(1);
			} else {
				SnippetSource snippetSource;
				if (source.isDatasource()) {
					snippetSource = new Datasource(projectDetails, datasource);
				} else {
					snippetSource = new SnippetSource(projectDetails);
				}
				file.create(new ByteArrayInputStream(snippetSource.apply(content)), true, subMonitor);
			}
		}
	}

	private void createFolders(IProject project, Snippet snippet, SubMonitor monitor) throws CoreException {
		Collection<String> folders = snippet.getFolders();
		String pkg = projectDetails.getPkgPath();
		for (String folder : folders) {
			String path = folder.replaceAll("\\{pkgPath\\}", pkg);
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