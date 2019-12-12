package allwhite.projects.support;

import allwhite.site.blog.PostContentRenderer;
import allwhite.support.nav.Navigation;
import allwhite.support.nav.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/admin/projects")
@Navigation(Section.PROJECTS)
class ProjectAdminController {
    private static final List<String> CATEGORIES =
            Collections.unmodifiableList(Arrays.asList("inculator", "active", "attic", "community"));
    private ProjectMetadataService service;
    private final PostContentRenderer renderer;

    @Autowired
    public ProjectAdminController(ProjectMetadataService service, PostContentRenderer renderer) {
        this.service = service;
        this.renderer = renderer;
    }

    @RequestMapping(value = "", method = GET)
    public String list(Model model) {
        model.addAttribute("projects", service.getProjects());
        return "admin/project/index";
    }
}
