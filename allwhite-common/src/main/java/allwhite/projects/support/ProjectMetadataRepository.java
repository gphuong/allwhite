package allwhite.projects.support;

import allwhite.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMetadataRepository extends JpaRepository<Project, String> {

}
