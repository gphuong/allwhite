package allwhite.tools;

import java.util.*;
import java.util.stream.Collectors;

public class ToolSuiteDownloads {
    private final Map<String, ToolSuitePlatform> platforms;
    private final List<UpdateSiteArchive> archives;
    private final String shortname;
    private final String releaseName;
    private final String whatsNew;

    public ToolSuiteDownloads(String shortname, String releaseName,
                              String whatsNew, Map<String, ToolSuitePlatform> platforms, List<UpdateSiteArchive> archives) {
        this.platforms = platforms;
        this.archives = archives;
        this.shortname = shortname;
        this.releaseName = releaseName;
        this.whatsNew = whatsNew;
    }

    public List<ToolSuitePlatform> getPlatformList() {
        ArrayList<ToolSuitePlatform> platformList = new ArrayList<>();
        platformList.add(platforms.get("windows"));
        platformList.add(platforms.get("mac"));
        platformList.add(platforms.get("linux"));
        return platformList;
    }

    public String getWhatsNew() {
        return whatsNew;
    }

    public List<UpdateSiteArchive> getArchives() {
        return archives;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public Set<DownloadLink> getPreferredDownloadLinks() {
        Set<DownloadLink> links = new HashSet<>();
        addLinks(links, "windows", "zip");
        addLinks(links, "mac", "dmg");
        addLinks(links, "linux", "tar.gz");
        return links;
    }

    private void addLinks(Set<DownloadLink> links, String platformString, String fileType) {
        ToolSuitePlatform platform = platforms.get(platformString);
        if (platform == null)
            return;
        EclipseVersion eclipseVersion = platform.getEclipseVersions().get(0);
        for (Architecture architecture : eclipseVersion.getArchitectures()) {
            links.addAll(architecture.getDownloadLinks().stream()
                    .filter(link -> link.getFileType().equals(fileType))
                    .collect(Collectors.toList()));
        }
    }
}
