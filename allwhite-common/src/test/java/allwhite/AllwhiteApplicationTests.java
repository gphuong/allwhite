package allwhite;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import static allwhite.AllwhiteProfiles.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

public class AllwhiteApplicationTests {
    @BeforeClass
    public static void assertClear() {
        assertThat(System.getProperty(ACTIVE_PROFILES_PROPERTY_NAME), nullValue());
    }

    @Test
    public void unknownProfileSpecified() {
        activeProfiles("bogus");
        runApp();
        assertThat(runApp().getEnvironment().acceptsProfiles(STANDALONE), is(true));
        assertThat(runApp().getEnvironment().acceptsProfiles("bogus"), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void bothStagingAndProductionSpecified() {
        activeProfiles(STAGING, PRODUCTION);
        runApp();
    }

    @Test
    public void stagingSpecified() {
        activeProfiles(STAGING);
        assertThat(runApp().getEnvironment().acceptsProfiles(CLOUDFOUNDRY), is(true));
    }

    @Test
    public void productionSpecified() {
        activeProfiles(PRODUCTION);
        assertThat(runApp().getEnvironment().acceptsProfiles(CLOUDFOUNDRY), is(true));
    }

    @Test
    public void noProfileSpecified() {
        Environment env = runApp().getEnvironment();
        assertThat(env.acceptsProfiles(env.getDefaultProfiles()), is(false));
        assertThat(env.acceptsProfiles(CLOUDFOUNDRY), is(false));
        assertThat(env.acceptsProfiles(STANDALONE), is(true));
    }

    @After
    public void clearProperty() {
        System.clearProperty(ACTIVE_PROFILES_PROPERTY_NAME);
    }

    private ConfigurableApplicationContext runApp() {
        return new AllwhiteApplication(Dummy.class).run();
    }

    private void activeProfiles(String... profiles) {
        System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, arrayToCommaDelimitedString(profiles));
    }
}

class Dummy {

}
