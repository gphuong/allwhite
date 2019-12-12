package allwhite.projects.support;

import allwhite.projects.Project;
import allwhite.projects.ProjectRelease;
import allwhite.projects.ProjectRelease.ReleaseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

@RequestMapping("/badges")
@Controller
public class BadgeController {

    private final ProjectMetadataService service;
    private VersionBadgeService versionBadgeService;

    @Autowired
    public BadgeController(ProjectMetadataService service, VersionBadgeService versionBadgeService) {
        this.service = service;
        this.versionBadgeService = versionBadgeService;
    }

    @RequestMapping(value = {"/{projectId}/ga.svg", "/{projectId}.svg"}, method = {GET, HEAD}, produces = "image/svg+xml")
    public ResponseEntity<byte[]> releaseBadge(@PathVariable("projectId") String projectId) throws IOException {
        return badgeFor(projectId, ReleaseStatus.GENERAL_AVAILABILITY);
    }

    private ResponseEntity<byte[]> badgeFor(String projectId, ReleaseStatus releaseStatus) throws IOException {

        Project project = service.getProject(projectId);

        if (project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<ProjectRelease> gaRelease = getRelease(project.getProjectReleases(),
                projectRelease -> projectRelease.getReleaseStatus() == releaseStatus);
        if (!gaRelease.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] svgBadge = versionBadgeService.createSvgBadge(project, gaRelease.get());
        return ResponseEntity.ok().eTag(gaRelease.get().getVersion()).cacheControl(CacheControl.maxAge(1L, TimeUnit.HOURS))
                .body(svgBadge);
    }

    private Optional<ProjectRelease> getRelease(Collection<ProjectRelease> projectReleases,
                                                Predicate<ProjectRelease> predicate) {
        Optional<ProjectRelease> first = projectReleases
                .stream()
                .filter(projectRelease -> predicate.test(projectRelease) && projectRelease.isCurrent())
                .findFirst();

        if (first.isPresent()) {
            return first;
        }
        return projectReleases.stream()
                .filter(projectRelease -> predicate.test(projectRelease))
                .findFirst();
    }
}
