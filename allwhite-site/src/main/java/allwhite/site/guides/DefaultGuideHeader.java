package allwhite.site.guides;

import allwhite.site.renderer.GuideMetadata;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashSet;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class DefaultGuideHeader implements GuideHeader {

    private final static String REPO_ZIP_URL = "https://github.com/%s/archive/master.zip";

    private final static String CI_STATUS_IMAGE_URL = "https://travis-ci.org/%s.svg?branch=master";

    private final static String CI_LATEST_URL = "https://travis-ci.org/%s";

    private String name;

    private String repositoryName;

    private String title;

    private String description;

    private String githubUrl;

    private String gitUrl;

    private String sshUrl;

    private String cloneUrl;

    private Set<String> projects = new HashSet<>();

    public DefaultGuideHeader() {
    }

    public DefaultGuideHeader(String name, String repositoryName, String title, String description, String githubUrl,
                              String gitUrl, String sshUrl, String cloneUrl, Set<String> projects) {
        this.name = name;
        this.repositoryName = repositoryName;
        this.title = title;
        this.description = description;
        this.githubUrl = githubUrl;
        this.gitUrl = gitUrl;
        this.sshUrl = sshUrl;
        this.cloneUrl = cloneUrl;
        this.projects = projects;
    }

    public DefaultGuideHeader(GuideMetadata metadata) {
        this.name = metadata.getName();
        this.repositoryName = metadata.getRepositoryName();
        this.title = metadata.getTitle();
        this.description = metadata.getDescription();
        this.githubUrl = metadata.getGithubUrl();
        this.gitUrl = metadata.getGitUrl();
        this.sshUrl = metadata.getSshUrl();
        this.cloneUrl = metadata.getCloneUrl();
        this.projects.addAll(metadata.getProjects());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getRepositoryName() {
        return this.repositoryName;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getGithubUrl() {
        return this.githubUrl;
    }

    @Override
    public String getGitUrl() {
        return this.gitUrl;
    }

    @Override
    public String getSshUrl() {
        return this.sshUrl;
    }

    @Override
    public String getCloneUrl() {
        return this.cloneUrl;
    }

    @Override
    public Set<String> getProjects() {
        return this.projects;
    }

    @Override
    @JsonIgnore
    public String getZipUrl() {
        return String.format(REPO_ZIP_URL, this.repositoryName);
    }

    @Override
    @JsonIgnore
    public String getCiStatusImageUrl() {
        return String.format(CI_STATUS_IMAGE_URL, this.repositoryName);
    }

    @Override
    @JsonIgnore
    public String getCiLatestUrl() {
        return String.format(CI_LATEST_URL, this.repositoryName);
    }
}
