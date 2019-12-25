package allwhite.site.guides;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@JsonTest
public class GuidesJsonTest {
    @Autowired
    private JacksonTester<GettingStartedGuide> json;

    @Test
    public void serializeJson() throws Exception {
        Set<String> projects = new HashSet<>();
        projects.add("spring-boot");
        GuideHeader header = new DefaultGuideHeader("rest-service", "spring-guides/gs-rest-service",
                "Rest Service Title", "Description",
                "https://github.com/spring-guides/gs-rest-service",
                "git://github.com/spring-guides/gs-rest-service.git",
                "git@github.com:spring-guides/gs-rest-service.git",
                "https://github.com/spring-guides/gs-rest-service.git", projects);
        Guidecon
    }
}
