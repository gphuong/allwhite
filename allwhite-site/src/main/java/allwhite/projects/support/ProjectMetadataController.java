package allwhite.projects.support;

import allwhite.projects.Project;
import allwhite.projects.ProjectPatchingService;
import allwhite.projects.ProjectRelease;
import allwhite.support.JsonPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@JsonPController
@RequestMapping("/project_metadata")
class ProjectMetadataController {
    private final ProjectMetadataService service;
    private final ProjectPatchingService projectPatchingService;

    @Autowired
    public ProjectMetadataController(ProjectMetadataService service, ProjectPatchingService projectPatchingService) {
        this.service = service;
        this.projectPatchingService = projectPatchingService;
    }

    @RequestMapping(value = "/{projectId}", method = {GET, HEAD})
    public Project projectMetadata(@PathVariable("projectId") String projectId) {
        Project project = service.getProject(projectId);
        return project;
    }

    @RequestMapping(value = "/{projectId}/releases", method = PUT)
    public Project updateProjectMetadata(String projectId, List<ProjectRelease> releases) {
        Project project = service.getProject(projectId);
        for (ProjectRelease release : releases) {
            project.updateProjectRelease(release);
        }
        service.save(project);
        return project;
    }

    @RequestMapping(value = "/{projectId}/releases", method = POST)
    public ProjectRelease updateReleaseMetadata(@PathVariable("projectId") String projectId,
                                                @RequestBody ProjectRelease release) {
        Project project = service.getProject(projectId);
        if (project == null) {
            throw new MetadataNotFoundException("Cannot find project " + projectId);
        }
        boolean found = project.updateProjectRelease(release);
        if (found) {
            service.save(project);
        }
        return release;
    }

    @RequestMapping(value = "/{projectId}", method = PATCH)
    public Project updateProject(@PathVariable("projectId") String projectId, @RequestBody Project projectWithPatches) {
        Project project = service.getProject(projectId);
        if (project == null) {
            throw new MetadataNotFoundException("Cannot find project " + projectId);
        }
        Project patchedProject = projectPatchingService.patch(projectWithPatches, project);
        return service.save(patchedProject);
    }

    static class MetadataNotFoundException extends RuntimeException {
        public MetadataNotFoundException(String message) {
            super(message);
        }
    }
}
