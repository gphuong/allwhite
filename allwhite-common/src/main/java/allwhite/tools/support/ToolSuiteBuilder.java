package allwhite.tools.support;

import allwhite.tools.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class ToolSuiteBuilder {
    private final DownloadLinkExtractor downloadLinkExtractor = new DownloadLinkExtractor();
    private final String shortname;

    private Map<String, ToolSuitePlatform> platformMap = new LinkedHashMap<>();
    private List<UpdateSiteArchive> updateSiteArchives = new ArrayList<>();
    private Map<String, EclipseVersion> eclipseVersionMap = new LinkedHashMap<>();
    private Map<String, Architecture> architectureMap = new LinkedHashMap<>();
    private String releaseName;
    private String whatsNew;

    public ToolSuiteBuilder(String shortname) {
        this.shortname = shortname;
    }

    public void setWhatsNew(String whatsNew) {
        this.whatsNew = whatsNew;
    }

    public void addDownload(Download download) {
        if (download.getOs().equals("all")) {
            extractArchive(download);
        }
    }
    public ToolSuiteDownloads build(){
        return new ToolSuiteDownloads(shortname, releaseName, whatsNew, platformMap, updateSiteArchives);
    }

    private void extractArchive(Download download) {
        String url = download.getBucket() + download.getFile();
        UpdateSiteArchive archive = new UpdateSiteArchive(download.getEclipseVersion(), url, download.getSize());
        if (!updateSiteArchives.contains(archive)) {
            updateSiteArchives.add(archive);
        }
    }
}
