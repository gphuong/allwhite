package allwhite.tools.support;

import allwhite.support.Fixtures;
import allwhite.support.cache.CachedRestClient;
import allwhite.tools.DownloadLink;
import allwhite.tools.EclipseVersion;
import allwhite.tools.ToolSuiteDownloads;
import allwhite.tools.ToolSuitePlatform;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ToolsServiceTests {
    private ToolsService service;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CachedRestClient restClient;

    @Before
    public void setUp() {
        XmlMapper serializer = new XmlMapper();
        service = new ToolsService(restClient, restTemplate, serializer);
        String responseXml = Fixtures.load("/fixtures/tools/sts_downloads.xml");
        when(restClient.get(eq(restTemplate), anyString(), anyObject())).thenReturn(responseXml);
    }

    @Test
    public void testGetStsDownloads() throws IOException {
        ToolSuiteDownloads toolSuite = service.getStsGaDownloads();
        assertThat(toolSuite, notNullValue());

        assertThat(toolSuite.getWhatsNew(), equalTo("http://static.springsource.org/sts/nan/v360/NewAndNoteworthy.html"));

        List<ToolSuitePlatform> platforms = toolSuite.getPlatformList();
        assertThat(platforms.size(), equalTo(3));

        ToolSuitePlatform windows = platforms.get(0);
        ToolSuitePlatform mac = platforms.get(1);

        assertThat(windows.getName(), equalTo("Windows"));
        assertThat(mac.getName(), equalTo("Mac"));
        assertThat(platforms.get(2).getName(),equalTo("Linux"));

        List<EclipseVersion> eclipseVersions = windows.getEclipseVersions();
        assertThat(eclipseVersions.size(),equalTo(1));
        assertThat(eclipseVersions.get(0).getName(), equalTo("4.4"));

        assertThat(windows.getEclipseVersions().get(0).getArchitectures().get(0).getDownloadLinks().size(), equalTo(1));
        DownloadLink downloadLink = windows.getEclipseVersions().get(0).getArchitectures().get(0).getDownloadLinks().get(0);
        assertThat(downloadLink.getUrl(), equalTo("http://download.springsource.com/release/STS/3.6.0/dist/e4.4/spring-tool-suite-3.6.0.RELEASE-e4.4-win32.zip"));

        assertThat(toolSuite.getArchives().size(),equalTo(5));
        assertThat(toolSuite.getArchives().get(0).getVersion(),equalTo("4.4"));
        assertThat(toolSuite.getArchives().get(1).getVersion(),equalTo("4.3.2"));
        assertThat(toolSuite.getArchives().get(2).getVersion(),equalTo("4.2.2"));
        assertThat(toolSuite.getArchives().get(3).getVersion(),equalTo("3.8.2"));
        assertThat(toolSuite.getArchives().get(4).getVersion(),equalTo("3.7.2"));
    }

}
