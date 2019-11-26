package allwhite.renderer.guides;

import allwhite.renderer.github.Repository;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(collectionRelation = "guides")
class GuideResource extends ResourceSupport {

    private String name;

    private String repositoryName;

    private String title;

    private String description;

    private GuideType type;

    private String githubUrl;

    private String gitUrl;

    private String sshUrl;

    private String cloneUrl;

    private String[] projects;

    GuideResource(Repository repository) {
        this.type = GuideType.fromRepositoryName(repository.getName());
        this.name = this.type.stripPrefix(repository.getName());
        this.repositoryName = repository.getFullName();
        String description = repository.getDescription();
        if (description != null) {
            String[] split = repository.getDescription().split("::");
            this.title = split[0].trim();
            this.description = (split.length > 1) ? split[1].trim() : "";
        } else {
            this.title = "";
            this.description = "";
        }
        this.githubUrl = repository.getHtmlUrl();
        this.gitUrl = repository.getGitUrl();
        this.sshUrl = repository.getSshUrl();
        this.cloneUrl = repository.getCloneUrl();
        if (repository.getTopics() != null) {
            this.projects = repository.getTopics().toArray(new String[0]);
        } else {
            this.projects = new String[0];
        }
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

    public String[] getProjects() {
        return projects;
    }
}
