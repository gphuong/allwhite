package allwhite.projects.support;

import allwhite.projects.Project;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMetadataRepository extends JpaRepository<Project, String> {
    @EntityGraph(value = "Project.tree", type = EntityGraph.EntityGraphType.LOAD)
    List<Project> findDistinctByCategoryAndParentProjectIsNull(String category, Sort sort);
}
