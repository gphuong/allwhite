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
        } else {
            if (releaseName == null) {
                releaseName = download.getVersion();
            }
            extractPlatformDownloadLink(download);
        }
    }

    private void extractPlatformDownloadLink(Download download) {
        ToolSuitePlatform platform = createOrFindPlatform(download.getOs(), download.getVersion());
        EclipseVersion eclipseVersion = createOrFindEclipseVersion(download.getEclipseVersion(), platform);
        Architecture architecture = createOrFindArchitecture(download.getDescription(), eclipseVersion, platform);

        DownloadLink link = downloadLinkExtractor.createDownloadLink(download);
        architecture.getDownloadLinks().add(link);
    }

    private Architecture createOrFindArchitecture(String architectureName, EclipseVersion eclipseVersion,
                                                  ToolSuitePlatform platform) {
        String key = platform.getName() + eclipseVersion.getName() + architectureName;

        Architecture architecture = architectureMap.get(key);
        if (architecture == null) {
            architecture = new Architecture(architectureName, new ArrayList<>());
            eclipseVersion.getArchitectures().add(architecture);
            architectureMap.put(key, architecture);
        }
        return architecture;
    }

    private EclipseVersion createOrFindEclipseVersion(String eclipseVersionName, ToolSuitePlatform platform) {
        String key = platform.getName() + eclipseVersionName;

        EclipseVersion eclipseVersion = eclipseVersionMap.get(key);
        if (eclipseVersion == null) {
            eclipseVersion = new EclipseVersion(eclipseVersionName, new ArrayList<>());
            platform.getEclipseVersions().add(eclipseVersion);
            eclipseVersionMap.put(key, eclipseVersion);
        }
        return eclipseVersion;
    }

    private ToolSuitePlatform createOrFindPlatform(String os, String name) {
        ToolSuitePlatform platform = platformMap.get(os);
        if (platform == null) {
            platform = new ToolSuitePlatform(os, new ArrayList<>());
            platformMap.put(os, platform);
        }
        return platform;
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
