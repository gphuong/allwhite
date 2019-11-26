package allwhite.renderer;

import org.asciidoctor.Asciidoctor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsciidoctorConfig {
    @Bean
    public Asciidoctor asciidoctor() {
        return Asciidoctor.Factory.create();
    }
}
