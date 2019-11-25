package allwhite.renderer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
@ConfigurationProperties("allwhite.renderer")
@Validated
public class RendererProperties {
    private final Github github = new Github();
    private final Guides guides = new Guides();

    public Github getGithub() {
        return github;
    }

    public Guides getGuides() {
        return guides;
    }

    public static class Github {
        @Pattern(regexp = "([0-9a-z]*)?")
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    public static class Guides{
        private String organization = "spring-guides";

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }
    }
}
