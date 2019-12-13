package allwhite.projects.support;



import java.util.Arrays;
import java.util.List;

import allwhite.projects.Project;
import allwhite.site.guides.GettingStartedGuides;
import allwhite.site.guides.Topicals;
import allwhite.site.guides.Tutorials;
import allwhite.support.ResourceNotFoundException;
import allwhite.support.nav.Navigation;
import allwhite.support.nav.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Joiner;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Controller that handles requests for the projects overview page at /projects.
 */
@Controller
@RequestMapping("/templates/projects")
@Navigation(Section.PROJECTS)
class ProjectsController {

    private ProjectMetadataService projectMetadataService;
    private GettingStartedGuides gsGuides;
    private Tutorials tutorials;
    private Topicals topicals;

    @Autowired
    public ProjectsController(ProjectMetadataService projectMetadataService,
                              GettingStartedGuides gsGuides, Tutorials tutorials, Topicals topicals) {
        this.projectMetadataService = projectMetadataService;
        this.gsGuides = gsGuides;
        this.tutorials = tutorials;
        this.topicals = topicals;
    }

    @RequestMapping(method = { GET, HEAD })
    public String listProjects(Model model) {
        this.projectMetadataService.getProjects()
                .forEach(project -> model.addAttribute(project.getId().replaceAll("-", ""), project));
        return "projects/index";
    }

    @RequestMapping(value = "/{projectName}", method = { GET, HEAD })
    public String showProject(Model model, @PathVariable String projectName) {
        Project project = projectMetadataService.getProject(projectName);
        if (project == null) {
            throw new ResourceNotFoundException("project " + projectName);
        }
        List<Project> projects = this.projectMetadataService.getActiveTopLevelProjects();
        model.addAttribute("selectedProject", project);
        model.addAttribute("projectStackOverflow", stackOverflowUrl(project));
        model.addAttribute("projects", projects);
        model.addAttribute("currentRelease", project.getMostCurrentRelease());
        model.addAttribute("otherReleases", project.getNonMostCurrentReleases());

        model.addAttribute("guides", Arrays.asList(gsGuides.findByProject(project)));
        model.addAttribute("topicals", Arrays.asList(topicals.findByProject(project)));
        model.addAttribute("tutorials", Arrays.asList(tutorials.findByProject(project)));

        return "projects/show";
    }

    private String stackOverflowUrl(Project project) {
        return "https://stackoverflow.com/questions/tagged/"
                + Joiner.on("+or+").join(project.getStackOverflowTagList());
    }

}
