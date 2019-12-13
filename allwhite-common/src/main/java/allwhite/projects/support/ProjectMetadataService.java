package allwhite.projects.support;

import allwhite.projects.Project;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMetadataService {
    private ProjectMetadataRepository repository;
    private static final Sort sortByDisplayOrderAndId = new Sort("displayOrder", "id");

    public Project getProject(String id) {
        return repository.findOne(id);
    }

    public List<Project> getProjects() {
        return repository.findAll(sortByDisplayOrderAndId);
    }

    public Project save(Project project) {
        return repository.save(project);
    }

    public List<Project> getActiveTopLevelProjects() {
        return repository.findDistinctByCategoryAndParentProjectIsNull("active", sortByDisplayOrderAndId);
    }
}
