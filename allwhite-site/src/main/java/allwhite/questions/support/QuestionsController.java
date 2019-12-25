package allwhite.questions.support;

import allwhite.projects.Project;
import allwhite.projects.support.ProjectMetadataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class QuestionsController {
    private final ProjectMetadataService projectMetadata;
    private final StackOverflowClient stackOverflow;

    private final Log logger = LogFactory.getLog(QuestionsController.class);

    public QuestionsController(ProjectMetadataService projectMetadata, StackOverflowClient stackOverflow) {
        this.projectMetadata = projectMetadata;
        this.stackOverflow = stackOverflow;
    }

    @RequestMapping("/questions")
    public String show(Model model) {
        model.addAttribute("questions", stackOverflow.searchForQuestionsTagged("spring"));
        model.addAttribute("projects", getProjectList());
        return "questions/index";
    }

    private List<Project> getProjectList() {

        return projectMetadata.getProjects().stream()
                .filter(project -> !project.getCategory().equals("attic"))
                .filter(project -> !project.getStackOverflowTagList().isEmpty())
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .collect(Collectors.toList());
    }
}
