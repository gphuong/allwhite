package allwhite;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("allwhite.site")
public class SiteProperties {
    private Renderer renderer = new Renderer();

    public Renderer getRenderer() {
        return this.renderer;
    }

    public static class Renderer {
        private String serviceUrl = "http://localhost:8081";

        public String getServiceUrl() {
            return serviceUrl;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }
    }
}
