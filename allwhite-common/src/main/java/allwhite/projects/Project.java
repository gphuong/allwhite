package allwhite.projects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NamedEntityGraph(name = "Project.tree", attributeNodes = @NamedAttributeNode("childProjectList"))
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Project {

    @Id
    private String id;
    private String name;
    private String repoUrl;
    private String siteUrl;
    private String category;
    private String rawBootConfig;
    private String renderedBootConfig;
    private String rawOverview;
    private String renderedOverview;
    private int displayOrder = Integer.MAX_VALUE;

    @ManyToOne
    @JsonIgnore
    private Project parentProject;

    @OneToMany(mappedBy = "parentProject")
    @OrderBy("displayOrder")
    private List<Project> childProjectList;

    @ElementCollection
    private List<ProjectRelease> releaseList = new ArrayList<>();
    private String stackOverflowTags;

    @ElementCollection
    private List<ProjectSample> sampleList = new ArrayList<>();

    public Project() {
    }

    public Project(String id, String name, String repoUrl, String siteUrl, int displayOrder, List<ProjectRelease> releaseList,
                   String category, String stackOverflowTags, String bootconfig) {
        this(id, name, repoUrl, siteUrl, releaseList, category);
        this.setDisplayOrder(displayOrder);
        this.setStackOverflowTags(stackOverflowTags);
        this.setRawBootConfig(bootconfig);
    }

    public Project(String id, String name, String repoUrl, String siteUrl,
                   List<ProjectRelease> releaseList, String category) {
        this.id = id;
        this.name = name;
        this.repoUrl = repoUrl;
        this.siteUrl = siteUrl;
        this.category = category;
        this.releaseList = releaseList;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }


    public void setStackOverflowTags(String stackOverflowTags) {
        this.stackOverflowTags = stackOverflowTags;
    }

    public Optional<ProjectRelease> getMostCurrentRelease() {
        return this.getProjectReleases().stream()
                .filter(ProjectRelease::isCurrent)
                .findFirst();
    }

    public List<ProjectRelease> getProjectReleases() {
        if (releaseList == null) {
            return new ArrayList<>();
        }
        releaseList.sort(Collections.reverseOrder(ProjectRelease::compareTo));
        return releaseList;
    }

    public List<ProjectRelease> getNonMostCurrentReleases() {
        Optional<ProjectRelease> mostCurrentRelease = this.getMostCurrentRelease();
        if (mostCurrentRelease.isPresent()) {
            return this.getProjectReleases().stream()
                    .filter(projectRelease -> !projectRelease.equals(mostCurrentRelease.get()))
                    .collect(Collectors.toList());
        } else {
            return this.getProjectReleases();
        }
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public void setChildProjectList(List<Project> childProjectList) {
        this.childProjectList = childProjectList;
    }

    public boolean isTopLevelProject() {
        return parentProject == null;
    }

    public void setProjectSamples(List<ProjectSample> sampleList) {
        this.sampleList = sampleList;
    }

    public List<ProjectSample> getProjectSamples() {
        sampleList.sort(Comparator.comparingInt(ProjectSample::getDisplayOrder));
        return sampleList;
    }

    public String getName() {
        return name;
    }

    public void setProjectReleases(List<ProjectRelease> releases) {
        this.releaseList = releases;
    }

    public void setRawBootConfig(String rawBootConfig) {
        this.rawBootConfig = rawBootConfig;
    }

    public void setRawOverview(String rawOverview) {
        this.rawOverview = rawOverview;
    }

    public String getRawBootConfig() {
        return rawBootConfig;
    }

    public void setRenderedBootConfig(String renderedBootConfig) {
        this.renderedBootConfig = renderedBootConfig;
    }

    public String getRawOverview() {
        return rawOverview;
    }

    public void setRenderedOverview(String renderedOverview) {
        this.renderedOverview = renderedOverview;
    }

    public String getId() {
        return id;
    }

    public String getRenderedBootConfig() {
        return renderedBootConfig;
    }

    public String getRenderedOverview() {
        return renderedOverview;
    }


    public ProjectRelease getProjectRelease(String version) {
        List<ProjectRelease> releases = getProjectReleases();
        for (ProjectRelease release : releases) {
            if (release.getVersion().equals(version)) {
                return release;
            }
        }
        return null;
    }

    public boolean updateProjectRelease(ProjectRelease release) {
        boolean found = false;
        List<ProjectRelease> releases = getProjectReleases();
        for (int i = 0; i < releases.size(); i++) {
            ProjectRelease projectRelease = releases.get(i);
            if (release.getRepository() != null && release.getRepository().equals(projectRelease.getRepository())) {
                release.setRepository(projectRelease.getRepository());
            }
            if (projectRelease.getVersion().equals(release.getVersion())) {
                releases.set(i, release);
                found = true;
                break;
            }
        }
        if (!found) {
            releases.add(release);
        }
        release.replaceVersionPattern();
        return found;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public Set<String> getStackOverflowTagList() {
        return StringUtils.commaDelimitedListToSet(this.stackOverflowTags);
    }
}
