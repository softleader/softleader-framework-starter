package tw.com.softleader.starter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.internal.events.BuildCommand;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tw.com.softleader.starter.files.F;
import tw.com.softleader.starter.files.JavaInputStream;
import tw.com.softleader.starter.files.PomInputStream;
import tw.com.softleader.starter.page.DependencyPage;
import tw.com.softleader.starter.page.ProjectDetailsPage;

public class NewSoftLeaderProjectStarterModel {

	public static String STARTER_URL = "https://raw.githubusercontent.com/softleader/softleader-framework-starter/master/template/starter.xml";
	private ProjectDetailsPage projectDetails;
	private DependencyPage dependency;
	private List<F> files = new ArrayList<F>();

	public NewSoftLeaderProjectStarterModel(ProjectDetailsPage projectDetails, DependencyPage dependency)
			throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		super();
		this.projectDetails = projectDetails;
		this.dependency = dependency;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(STARTER_URL);
		doc.getDocumentElement().normalize();
		NodeList nodes = doc.getElementsByTagName("f");
		IntStream.range(0, nodes.getLength()).parallel().forEach(i -> {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;
				F f = new F(ele.getAttribute("n"), ele.getAttribute("p"), ele.getAttribute("d"));
				String url = ele.getAttribute("u");
				if (url != null && !url.isEmpty()) {
					f.setContent(() -> {
						try (BufferedReader buffer = new BufferedReader(
								new InputStreamReader(new URL(url).openStream()))) {
							return buffer.lines().collect(Collectors.joining("\n"));
						} catch (Exception e) {
							throw new Error(e);
						}
					});
				}
				files.add(f);
			}
		});
	}

	public void performFinish(String projectName, URI locationURI, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException, CoreException {
		monitor.beginTask("Importing SoftLeader Project", 3);
		try {
			IProject project = createBaseProject(projectName, locationURI, new SubProgressMonitor(monitor, 1));
			createMavenArchetype(project, new SubProgressMonitor(monitor, 1));
			createFiles(projectName, project, new SubProgressMonitor(monitor, 1));
		} finally {
			monitor.done();
		}
	}

	@SuppressWarnings("restriction")
	private IProject createBaseProject(String projectName, URI location, IProgressMonitor monitor)
			throws CoreException {
		monitor.beginTask("Creating base project", 1);
		try {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			if (!project.exists()) {
				URI projectLocation = location;
				IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
				if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
					projectLocation = null;
				}
				monitor.setTaskName(projectLocation.toString());
				desc.setLocationURI(projectLocation);

				desc.setName(projectDetails.getArtifact());

				BuildCommand javascriptValidator = new BuildCommand();
				javascriptValidator.setName("org.eclipse.wst.jsdt.core.javascriptValidator");

				BuildCommand javabuilder = new BuildCommand();
				javabuilder.setName("org.eclipse.jdt.core.javabuilder");

				BuildCommand facetcorebuilder = new BuildCommand();
				facetcorebuilder.setName("org.eclipse.wst.common.project.facet.core.builder");

				BuildCommand maven2Builder = new BuildCommand();
				maven2Builder.setName("org.eclipse.m2e.core.maven2Builder");

				BuildCommand springbuilder = new BuildCommand();
				springbuilder.setName("org.springframework.ide.eclipse.core.springbuilder");

				desc.setBuildSpec(new ICommand[] { javascriptValidator, javabuilder, facetcorebuilder, maven2Builder,
						springbuilder });

				desc.setNatureIds(new String[] { "org.springframework.ide.eclipse.core.springnature",
						"org.eclipse.jem.workbench.JavaEMFNature", "org.eclipse.wst.common.modulecore.ModuleCoreNature",
						"org.eclipse.jdt.core.javanature", "org.eclipse.m2e.core.maven2Nature",
						"org.eclipse.wst.common.project.facet.core.nature", "org.eclipse.wst.jsdt.core.jsNature" });

				project.create(desc, monitor);
				project.open(monitor);
			}
			return project;
		} finally {
			monitor.done();
		}
	}

	private void createFiles(String projectName, IProject project, IProgressMonitor monitor) {
		monitor.beginTask("Creating files", (int) files.stream().filter(F::isFile).count());
		try {
			String pkg = projectDetails.getPkg();
			String pkgPath = projectDetails.getPkgPath();
			String pj = projectName.substring(0, 1).toUpperCase() + projectName.substring(1);
			files.stream().filter(F::isFile).forEach(f -> {
				try {
					String fullPath = f.getPath().replace("{pkg}", pkgPath).replace("{pj}", pj) + "/"
							+ f.getName().replace("{pj}", pj);
					IFile file = project.getFile(fullPath);
					String content = f.getContent().map(Supplier::get).orElse("");
					if (f.isJava()) {
						file.create(new JavaInputStream(pkg, pj, content), true, monitor);
					} else if (f.isPOM()) {
						file.create(new PomInputStream(pj, projectDetails, dependency, content), true, monitor);
					} else {
						file.create(new ByteArrayInputStream(content.getBytes()), true, monitor);
					}
				} catch (Exception e) {
					throw new Error("Error creating " + f, e);
				}
			});
		} finally {
			monitor.done();
		}
	}

	private void createMavenArchetype(IProject project, IProgressMonitor monitor) {
		monitor.beginTask("Creating maven archetype", (int) files.stream().filter(F::isFolder).count());
		try {
			String pkg = projectDetails.getPkgPath();
			files.stream().filter(F::isFolder).forEach(f -> {
				try {
					String path = f.getPath().replace("{pkg}", pkg);
					createFolder(project.getFolder(path), monitor);
					System.out.println("Folder [" + path + "] created");
				} catch (Exception e) {
					throw new Error("Error creating " + f, e);
				}
			});
		} finally {
			monitor.done();
		}
	}

	private void createFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent, new SubProgressMonitor(monitor, 1));
		}
		if (!folder.exists()) {
			folder.create(false, true, monitor);
		}
	}

}