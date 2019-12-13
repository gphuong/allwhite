package allwhite.site.guides;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface GuideHeader {

    String getName();

    String getRepositoryName();

    String getTitle();

    String getDescription();

    String getGithubUrl();

    String getGitUrl();

    String getSshUrl();

    String getCloneUrl();

    Set<String> getProjects();

    String getZipUrl();

    String getCiStatusImageUrl();

    String getCiLatestUrl();
}
