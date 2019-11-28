package allwhite.projects;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProjectReleaseVersionTests {
    @Test
    public void snapshotDetected() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("1.0.0.BUILD-SNAPSHOT").build();
        assertThat(version.isSnapshot(), equalTo(true));
        assertThat(version.getRepository().getUrl(), containsString("snapshot"));
    }


}
