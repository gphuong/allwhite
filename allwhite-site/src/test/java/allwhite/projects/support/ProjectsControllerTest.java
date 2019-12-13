package allwhite.projects.support;

import allwhite.projects.Project;
import allwhite.projects.ProjectRelease;
import allwhite.site.guides.GettingStartedGuides;
import allwhite.site.guides.GuideHeader;
import allwhite.site.guides.Topicals;
import allwhite.site.guides.Tutorials;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static allwhite.projects.ProjectRelease.ReleaseStatus.GENERAL_AVAILABILITY;
import static allwhite.projects.ProjectRelease.ReleaseStatus.SNAPSHOT;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ProjectsController.class)
@TestPropertySource(properties = "spring.profiles.active=standalone")
public class ProjectsControllerTest {
    @MockBean
    private ProjectMetadataService projectMetadataService;

    @MockBean
    private GettingStartedGuides projectGuidesRepo;

    @MockBean
    private Tutorials projectTutorialRepo;

    @MockBean
    private Topicals projectTopicalRepo;

    private ProjectRelease currentRelease;

    private ProjectRelease anotherCurrentRelease;

    private ProjectRelease snapshotRelease;

    private List<ProjectRelease> releases;

    private Project springBoot;

    private Project springData;

    private Project springDataJpa;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        this.currentRelease = new ProjectRelease("2.1.0.RELEASE", GENERAL_AVAILABILITY,
                true, "", "", "", "");
        this.anotherCurrentRelease = new ProjectRelease("2.0.0.RELEASE", GENERAL_AVAILABILITY,
                true, "", "", "", "");
        this.snapshotRelease = new ProjectRelease("2.2.0.SNAPSHOT", SNAPSHOT,
                false, "", "", "", "");
        this.releases = Arrays.asList(currentRelease, anotherCurrentRelease, snapshotRelease);

        this.springBoot = new Project("spring-boot", "Spring Boot",
                "https://github.com/spring-projects/spring-boot", "/project/spring-boot", 0,
                releases, "project", "spring-boot", "");

        this.springData = new Project("spring-data", "Spring Data",
                "https://github.com/spring-projects/spring-data", "/project/spring-data", 0,
                releases, "project", "spring-data,spring-data-commons", "");
        this.springDataJpa = new Project("spring-data-jpa", "Spring Data JPA",
                "https://github.com/spring-projects/spring-data-jpa", "/project/spring-data-jpa", 0,
                releases, "project", "spring-data-jpa", "");

        this.springData.setChildProjectList(Arrays.asList(this.springDataJpa));
        this.springDataJpa.setParentProject(this.springData);

        GuideHeader[] guides = new GuideHeader[]{};
        given(this.projectGuidesRepo.findByProject(any())).willReturn(guides);
        given(this.projectTopicalRepo.findByProject(any())).willReturn(guides);
        given(this.projectTutorialRepo.findByProject(any())).willReturn(guides);

        given(projectMetadataService.getProjects()).willReturn(Arrays.asList(this.springBoot, this.springData));
        given(projectMetadataService.getProject("spring-boot")).willReturn(this.springBoot);
        given(projectMetadataService.getProject("spring-data")).willReturn(this.springData);
        given(projectMetadataService.getActiveTopLevelProjects()).willReturn(Arrays.asList(this.springBoot, this.springData));
    }

    @Test
    public void showProjectModelHasProjectData() throws Exception {
        this.mvc.perform(get("/templates/projects/spring-boot"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedProject", this.springBoot))
                .andExpect(model().attribute("templates/projects", Matchers.contains(this.springBoot, this.springData)))
                .andExpect(model().attribute("projectStackOverflow", "https://stackoverflow.com/questions/tagged/spring-boot"));
    }

    @Test
    public void showProjectHasStackOverflowLink() throws Exception {
        this.mvc.perform(get("/templates/projects/spring-data"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedProject", this.springData))
                .andExpect(model().attribute("templates/projects", Matchers.contains(this.springBoot, this.springData)))
                .andExpect(model().attribute("projectStackOverflow", "https://stackoverflow.com/questions/tagged/spring-data+or+spring-data-commons"));
    }

    @Test
    public void showProjectHasReleases() throws Exception {
        this.mvc.perform(get("/templates/projects/spring-boot"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentRelease", Optional.of(this.currentRelease)))
                .andExpect(model().attribute("otherReleases", Matchers.hasItems(this.anotherCurrentRelease, this.snapshotRelease)));
    }

    @Test
    public void showProjectWithoutReleases() throws Exception {
        Project projectWithoutReleases =
                new Project("spring-norelease", "spring-norelease", "http://example.com",
                        "/project/spring-norelease", 0, Collections.emptyList(),
                        "project", "spring-example", "");
        when(projectMetadataService.getProject("spring-norelease")).thenReturn(projectWithoutReleases);

        this.mvc.perform(get("/templates/projects/spring-norelease"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentRelease", Optional.empty()))
                .andExpect(model().attribute("otherReleases", Matchers.empty()));
    }

    @Test
    public void listProjectsProvidesProjectMetadata() throws Exception {
        this.mvc.perform(get("/templates/projects"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("springboot", this.springBoot))
                .andExpect(model().attribute("springdata", this.springData));
    }

}