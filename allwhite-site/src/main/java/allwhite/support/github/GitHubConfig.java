package allwhite.support.github;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.social.github.connect.GitHubConnectionFactory;

@Configuration
public class GitHubConfig {
    public static final Log logger = LogFactory.getLog(GitHubConfig.class);

    @Value("${github.client.id}")
    private String githubClientId;

    @Value("${github.client.secret}")
    private String githubClientSecret;

    @Value("${github.access.token}")
    private String githubAccessToken;

    @Bean
    public GitHubConnectionFactory gitHubConnectionFactory() {
        GitHubConnectionFactory factory = new GitHubConnectionFactory(githubClientId, githubClientSecret);
        factory.setScope("user");
        return factory;
    }

    @Bean
    public GitHub gitHubTemplate() {
        if (StringUtils.isEmpty(githubAccessToken)) {
            return new GuideGitHubTemplate();
        }
        return new GuideGitHubTemplate(githubAccessToken);
    }

    private static class GuideGitHubTemplate extends GitHubTemplate {
        private GuideGitHubTemplate() {
            super();
            logger.warn("GitHub API access will be rate-limited at 60 req/hour");
        }

        private GuideGitHubTemplate(String githubAccessToken) {
            super(githubAccessToken);
        }
    }
}
