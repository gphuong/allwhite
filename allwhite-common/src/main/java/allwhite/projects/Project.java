package allwhite.projects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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

    public Project(String id, String name, String repoUrl, String siteUrl, List<ProjectRelease> releaseList, String category) {
        this.id = id;
        this.name = name;
        this.repoUrl = repoUrl;
        this.siteUrl = siteUrl;
        this.category = category;
        this.releaseList = releaseList;
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
}
