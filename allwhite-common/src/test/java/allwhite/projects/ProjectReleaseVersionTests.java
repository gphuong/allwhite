package allwhite.projects;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class ProjectReleaseVersionTests {
    @Test
    public void snapshotDetected() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("1.0.0.BUILD-SNAPSHOT").build();
        assertThat(version.isSnapshot(), equalTo(true));
        assertThat(version.getRepository().getUrl(), containsString("snapshot"));
    }

    @Test
    public void snapshotDetectedCiStyle() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("1.0.0.CI-SNAPSHOT").build();
        assertThat(version.isSnapshot(), equalTo(true));
        assertThat(version.getRepository().getUrl(), containsString("snapshot"));
    }

    @Test
    public void snapshotDetectedMavenStyle() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("1.0.0-SNAPSHOT").build();
        assertThat(version.isSnapshot(), equalTo(true));
        assertThat(version.getRepository().getUrl(), containsString("snapshot"));
    }

    @Test
    public void releaseTrainSnapshotDetected() {
        ProjectRelease version =
                new ProjectReleaseBuilder().versionName("Angel.BUILD-SNAPSHOT").build();
        assertThat(version.isSnapshot(), equalTo(true));
        assertThat(version.getRepository().getUrl(), containsString("snapshot"));
    }

    @Test
    public void prereleasedDetected() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("1.0.0.RC1").build();
        assertThat(version.isPreRelease(), equalTo(true));
        assertThat(version.getRepository().getUrl(), containsString("milestone"));
    }

    @Test
    public void releaseTrainPrereleaseDetected() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("Angel.RC1").build();
        assertThat(version.isPreRelease(), equalTo(true));
        assertThat(version.getRepository().getUrl(), containsString("milestone"));
    }

    @Test
    public void gaDetected() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("1.0.0.RELEASE").build();
        assertThat(version.isGeneralAvailability(), equalTo(true));
        assertThat(version.getRepository(), is(nullValue()));
    }

    @Test
    public void releaseTrainGaDetected() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("Angel.RELEASE").build();
        assertThat(version.isGeneralAvailability(), equalTo(true));
        assertThat(version.getRepository(), is(nullValue()));
    }

    @Test
    public void releaseServiceReleaseTrainGaDetected() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("Angel.SR1").build();
        assertThat(version.isGeneralAvailability(), equalTo(true));
        assertThat(version.getRepository(), is(nullValue()));
    }

    @Test
    public void releaseDashSeparatorDetected() {
        ProjectRelease version = new ProjectReleaseBuilder().versionName("Gosling-RC1").build();
        assertThat(version.isPreRelease(), equalTo(true));
        assertThat(version.getRepository().getUrl(), containsString("milestone"));
    }
}
