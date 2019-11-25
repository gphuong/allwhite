package allwhite.renderer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RendererProperties.class)
public class RendererApplication {
    public static void main(String[] args) {
        SpringApplication.run(RendererApplication.class, args);
    }
}
