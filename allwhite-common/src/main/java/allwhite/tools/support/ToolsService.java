package allwhite.tools.support;

import allwhite.support.cache.CachedRestClient;
import allwhite.tools.EclipseDownloads;
import allwhite.tools.ToolSuiteDownloads;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class ToolsService {
    private final ToolXmlConverter toolXmlConverter = new ToolXmlConverter();
    private final CachedRestClient restClient;
    private final RestTemplate restTemplate;
    private final XmlMapper serializer;

    @Autowired
    public ToolsService(CachedRestClient restClient, RestTemplate restTemplate, XmlMapper serializer) {
        this.restClient = restClient;
        this.restTemplate = restTemplate;
        this.serializer = serializer;
    }

    public ToolSuiteDownloads getStsGaDownloads() throws IOException {
        return getToolSuiteDownloads("Spring Tool Suite", "STS");
    }

    private ToolSuiteDownloads getToolSuiteDownloads(String toolSuiteName, String shortName) throws IOException {
        String responseXml = restClient.get(restTemplate, "http://dist.springsource.com/release/STS/index-new.xml", String.class);
        ToolSuiteXml toolSuiteXml = serializer.readValue(responseXml, ToolSuiteXml.class);
        return toolXmlConverter.convert(toolSuiteXml, toolSuiteName, shortName);
    }

    public EclipseDownloads getEclipseDownloads() throws IOException {
        String responseXml =
                restClient.get(restTemplate, "http://dist.springsource.com/release/STS/eclipse.xml", String.class);
        EclipseXml eclipseXml = serializer.readValue(responseXml, EclipseXml.class);
        return new EclipseDownloadsXmlConverter().convert(eclipseXml);

    }
}
