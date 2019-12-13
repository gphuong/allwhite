package allwhite.site.guides;

import allwhite.projects.Project;

public interface GuidesRepository<T extends Guide> {
    GuideHeader[] findByProject(Project project);

    GuideHeader[] findAll();
}
