package allwhite.projects.support;

import allwhite.projects.Project;
import allwhite.projects.ProjectRelease;
import allwhite.projects.ProjectRelease.ReleaseStatus;
import allwhite.site.blog.PostContentRenderer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectAdminControllerTests {
    @Mock
    private ProjectMetadataService projectMetadataService;

    @Mock
    private PostContentRenderer renderer;

    private List<ProjectRelease> releases = new ArrayList<>();
    Project project = new Project("spring-framework", "spring", "http://example.com", "http://examples.com", releases,
            "project");
    private ExtendedModelMap model = new ExtendedModelMap();
    private ProjectAdminController controller;

    @Before
    public void setUp() {
        controller = new ProjectAdminController(this.projectMetadataService, this.renderer);
    }

    @Test
    public void listProjects_providesProjectMetadataServiceInModel(){
        releases.add(new ProjectRelease("1.2.3", ReleaseStatus.GENERAL_AVAILABILITY, false, "http://example.com/1.2.3",
                "http://example.com/1.2.3", "org.springframework","spring-core"));
        when(projectMetadataService.getProject("spring-framework")).thenReturn(project);
        List<Project> list = Arrays.asList(project);

    }
}
