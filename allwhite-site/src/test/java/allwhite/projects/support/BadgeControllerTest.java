package allwhite.projects.support;

import allwhite.projects.Project;
import allwhite.projects.ProjectRelease;
import allwhite.projects.ProjectRelease.ReleaseStatus;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BadgeControllerTest {
    @Mock
    private ProjectMetadataService projectMetadataServiceMock;

    private VersionBadgeService versionBadgeService = new VersionBadgeService();

    private BadgeController controller;
    private Project project;
    private List<ProjectRelease> releases = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        versionBadgeService.postConstruct();
        controller = new BadgeController(projectMetadataServiceMock, versionBadgeService);
        project = new Project("spring-data-redis", "Spring Data Redis", "http", "http", releases, "data");
        when(projectMetadataServiceMock.getProject("spring-data-redis")).thenReturn(project);
    }

    @After
    public void tearDown() {
        versionBadgeService.preDestroy();
    }

    @Test
    public void badgeNotFound() throws IOException {
        ResponseEntity<byte[]> response = controller.releaseBadge("spring-data-redis");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.NOT_FOUND)));
    }

    @Test
    public void badgeShouldBeGenerated() throws IOException {

        releases.add(new ProjectRelease("1.0.RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, true, "", "", "", ""));
        ResponseEntity<byte[]> response = controller.releaseBadge("spring-data-redis");

        assertThat(response.getStatusCode(), CoreMatchers.is(equalTo(HttpStatus.OK)));
        assertThat(response.getHeaders().getETag(), CoreMatchers.is(equalTo("\"1.0.RELEASE\"")));
        assertThat(response.getHeaders().getCacheControl(), CoreMatchers.is(equalTo("max-age=3600")));

        String content = new String(response.getBody());
        assertThat(content, containsString("<svg"));
        assertThat(content, containsString("Spring Data Redis"));
        assertThat(content, containsString("1.0.RELEASE"));
    }

    @Test
    public void projectWithTwoReleasesShouldBeGenerated() throws IOException {
        releases.add(new ProjectRelease("1.0.RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        releases.add(new ProjectRelease("1.1.RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        ResponseEntity<byte[]> response = controller.releaseBadge("spring-data-redis");

        String content = new String(response.getBody());
        assertThat(content, containsString("1.1.RELEASE"));
    }

    @Test
    public void projectWithTwoReleasesWithoutCurrentFlagPicksHightestRelease() throws IOException {
        releases.add(new ProjectRelease("1.0.RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        releases.add(new ProjectRelease("1.1.RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        ResponseEntity<byte[]> response = controller.releaseBadge("spring-data-redis");

        String content = new String(response.getBody());
        assertThat(content, containsString("1.1.RELEASE"));
    }

    @Test
    public void projectWithTwoReleaseFlagPicksCurrentRelease() throws IOException {
        releases.add(new ProjectRelease("1.0.RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, true, "", "", "", ""));
        releases.add(new ProjectRelease("1.1.RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        ResponseEntity<byte[]> response = controller.releaseBadge("spring-data-redis");

        String content = new String(response.getBody());
        assertThat(content, containsString("1.0.RELEASE"));
    }

    @Test
    public void projectWithTwoReleasesUsingSymbolicNamesWithNumbersWithoutCurrentFlagPicksMostRecentRelease() throws IOException {
        releases.add(new ProjectRelease("Angel-SR6", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        releases.add(new ProjectRelease("Brixton-SR2", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        ResponseEntity<byte[]> response = controller.releaseBadge("spring-data-redis");

        String content = new String(response.getBody());
        assertThat(content, containsString("Brixton-SR2"));
    }

    @Test
    public void projectsWithTwoReleasesUsingSymbolicNamesWithoutCurrentFlagPicksFirstRelease() throws IOException {
        releases.add(new ProjectRelease("Angel-SR6", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        releases.add(new ProjectRelease("Brixton-RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        ResponseEntity<byte[]> response = controller.releaseBadge("spring-data-redis");

        String content = new String(response.getBody());
        assertThat(content, containsString("Brixton-RELEASE"));
    }

    @Test
    public void projectWithTwoReleasesUsingSymbolicNamesFlagPicksCurrentRelease() throws IOException {
        releases.add(new ProjectRelease("Angel-SR1", ReleaseStatus.GENERAL_AVAILABILITY, false, "", "", "", ""));
        releases.add(new ProjectRelease("Brixton-RELEASE", ReleaseStatus.GENERAL_AVAILABILITY, true, "", "", "", ""));
        ResponseEntity<byte[]> response = controller.releaseBadge("spring-data-redis");

        String content = new String(response.getBody());
        assertThat(content, containsString("Brixton-RELEASE"));
    }
}
