package allwhite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

abstract class MvcConfig extends WebMvcConfigurerAdapter {
    @Bean(name = {"uih", "viewRenderingHelper"})
    @Scope("request")
    public ViewRenderingHelper viewRenderingHelper() {
        return new ViewRenderingHelper();
    }

    static class ViewRenderingHelper {
        private UrlPathHelper urlPathHelper = new UrlPathHelper();

        private HttpServletRequest request;

        @Autowired
        public void setRequest(HttpServletRequest request) {
            this.request = request;
        }

        public String navClass(String active, String current) {
            if (active.equals(current)) {
                return "navbar-link active";
            } else {
                return "nav-link";
            }
        }

    }


}

@Configuration
@ControllerAdvice
@Profile("standalone")
class StandaloneMvcConfig extends MvcConfig {

}