package allwhite.tools.support;

import allwhite.tools.Architecture;
import allwhite.tools.Download;
import allwhite.tools.Release;
import allwhite.tools.ToolSuiteDownloads;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ToolXmlConverter_TwoDifferentToolSuitesTests {

    private ToolSuiteDownloads toolSuite;
    private ToolXmlConverter toolXmlConverter;

    @Before
    public void setUp() {
        ToolSuiteXml toolSuiteXml = new ToolSuiteXml();
        List<Release> releases = new ArrayList<>();

        Download download = new Download();
        download.setDescription("Mac OS X (Cocoa)");
        download.setOs("mac");
        download.setFile("release/STS/3.3.0/dist/e4.3/spring-tool-suite-3.3.0.RELEASE-e4.3-macosx-cocoa-installer.dmg");
        download.setBucket("http://dist.springsource.com/");
        download.setEclipseVersion("4.3");
        download.setSize("373MB");
        download.setVersion("3.3.0.RELEASE");

        Release stsRelease = new Release();
        stsRelease.setDownloads(Collections.singletonList(download));
        stsRelease.setName("Spring Tool Suite 3.3.0.RELEASE - based on Eclipse Kepler 4.3");
        releases.add(stsRelease);

        download = new Download();
        download.setDescription("Mac OS X (Cocoa)");
        download.setOs("mac");
        download.setFile("release/STS/3.3.0/dist/e3.8/groovy-grails-tool-suite-3.3.0.RELEASE-e3.8.2-macosx-cocoa-installer.dmg");
        download.setBucket("http://dist.springsource.com/");
        download.setEclipseVersion("4.3");
        download.setSize("463MB");
        download.setVersion("3.3.0.RELEASE");

        Release ggtsRelease = new Release();
        ggtsRelease.setDownloads(Collections.singletonList(download));
        ggtsRelease.setName("Groovy/Grails Tool Suite 3.3.0.RELEASE - based on Eclipse Kepler 4.3");
        releases.add(ggtsRelease);

        toolSuiteXml.setReleases(releases);

        toolXmlConverter = new ToolXmlConverter();
        toolSuite = toolXmlConverter.convert(toolSuiteXml, "Spring Tool Suite", "STS");
    }

    @Test
    public void addsADownloadLinkForStsOnly() {
        Architecture macArchitecture =
                toolSuite.getPlatformList().get(1).getEclipseVersions().get(0).getArchitectures().get(0);
        assertThat(macArchitecture.getDownloadLinks().size(), equalTo(1));
        assertThat(macArchitecture.getDownloadLinks().get(0).getUrl(),
                equalTo("http://dist.springsource.com/release/STS/3.3.0/dist/e4.3/spring-tool-suite-3.3.0.RELEASE-e4.3-macosx-cocoa-installer.dmg"));
    }
}
