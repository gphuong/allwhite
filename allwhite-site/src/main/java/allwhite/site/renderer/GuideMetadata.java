package allwhite.site.renderer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class GuideMetadata {
    private String name;

    private String repositoryName;

    private String title;

    private String description;

    private GuideType type;

    private String githubUrl;

    private String gitUrl;

    private String sshUrl;

    private String cloneUrl;

    private Set<String> projects;

    public GuideMetadata(@JsonProperty("name") String name,
                         @JsonProperty("repositoryName") String repositoryName, @JsonProperty("title") String title,
                         @JsonProperty("description") String description, @JsonProperty("type") GuideType type,
                         @JsonProperty("githubUrl") String githubUrl, @JsonProperty("gitUrl") String gitUrl,
                         @JsonProperty("sshUrl") String sshUrl, @JsonProperty("cloneUrl") String cloneUrl,
                         @JsonProperty("templates/projects") Set<String> projects) {
        this.name = name;
        this.repositoryName = repositoryName;
        this.title = title;
        this.description = description;
        this.type = type;
        this.githubUrl = githubUrl;
        this.gitUrl = gitUrl;
        this.sshUrl = sshUrl;
        this.cloneUrl = cloneUrl;
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public GuideType getType() {
        return type;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public Set<String> getProjects() {
        return projects;
    }
}
