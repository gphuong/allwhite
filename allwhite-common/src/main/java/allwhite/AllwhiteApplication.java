package allwhite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static allwhite.AllwhiteProfiles.*;
import static java.lang.String.format;
import static org.elasticsearch.common.Strings.arrayToCommaDelimitedString;

public class AllwhiteApplication extends SpringApplication {
    private static final Log logger = LogFactory.getLog(AllwhiteApplication.class);

    public AllwhiteApplication(Class<?> configClass) {
        super(configClass);
    }

    @Override
    protected void configureProfiles(ConfigurableEnvironment environment, String[] args) {
        super.configureProfiles(environment, args);

        boolean stagingActive = environment.acceptsProfiles(STAGING);
        boolean productionActive = environment.acceptsProfiles(PRODUCTION);

        if (stagingActive && productionActive) {
            throw new IllegalStateException(format("Only one of the following profiles may be specified: [%s]",
                    arrayToCommaDelimitedString(new String[]{STAGING, PRODUCTION})));
        }
        if (stagingActive || productionActive) {
            logger.info(format("Activating '%s' profile because one of '%s' or '%s' profiles have been specified.",
                    CLOUDFOUNDRY, STAGING, PRODUCTION));
            environment.addActiveProfile(CLOUDFOUNDRY);
        } else {
            logger.info("The default 'standalone' profiles is active because no other profiles have been specified.");
            environment.addActiveProfile(STANDALONE);
            Map<String, Object> map = new HashMap<>();
            map.put("client.dir", new File("../sagan-client").getAbsolutePath());
            environment.getPropertySources().addLast(new MapPropertySource("clientDir", map));
        }
    }
}
