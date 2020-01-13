package allwhite.tools.suppot;

import allwhite.tools.*;
import allwhite.tools.support.ToolsController;
import allwhite.tools.support.ToolsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ToolsControllerTests {

    private ToolsController controller;
    private ExtendedModelMap model = new ExtendedModelMap();

    @Mock
    private ToolsService service;

    @Before
    public void setUp() {
        controller = new ToolsController(service);
    }

    @Test
    public void stsIndexHasDownloadLinks() throws IOException {
        Map<String, ToolSuitePlatform> platforms = new HashMap<>();
        List<DownloadLink> downloadLinks = Collections.singletonList(new DownloadLink(
                "http://example.com/download.dmg", "dmg", "323MB", "mac", "64"));
        List<Architecture> architectures = Collections.singletonList(new Architecture(
                "Mac OS X (Cocoa, 64bit)", downloadLinks));
        List<EclipseVersion> eclipseVersionss = Collections.singletonList(new EclipseVersion("1.2", architectures));

        ToolSuitePlatform windows = new ToolSuitePlatform("windows", eclipseVersionss);
        platforms.put("windows", windows);

        List<UpdateSiteArchive> archives = Collections.emptyList();
        ToolSuiteDownloads toolSuite = new ToolSuiteDownloads("STS", "3.1.2.RELEASE",
                "http://static.springsource.org/sts/nan/v312/NewAndNoteworthy.html", platforms, archives);
        when(service.getStsGaDownloads()).thenReturn(toolSuite);
        controller.stsIndex(model);

        Set<DownloadLink> actual = (Set<DownloadLink>) model.get("downloadLinks");
        assertThat(actual, equalTo(toolSuite.getPreferredDownloadLinks()));
        assertThat(model.get("version"), equalTo("3.1.2.RELEASE"));
    }

    @Test
    public void allEclipseDownloadsAddsDownloadsToModel() throws IOException {
        Map<String, EclipsePlatform> platforms = new HashMap<>();

        EclipsePlatform windows = new EclipsePlatform("windows", Collections.emptyList());
        platforms.put("windows", windows);
        EclipsePlatform mac = new EclipsePlatform("mac", Collections.emptyList());
        platforms.put("mac", mac);
        EclipsePlatform linux = new EclipsePlatform("linux", Collections.emptyList());
        platforms.put("linux", linux);

        EclipseDownloads eclipseDownloads = new EclipseDownloads(platforms);

        when(service.getEclipseDownloads()).thenReturn(eclipseDownloads);
        controller.eclipseIndex(model);

        assertThat((List<EclipsePlatform>) model.get("platforms"), contains(windows, mac, linux));
    }
}
