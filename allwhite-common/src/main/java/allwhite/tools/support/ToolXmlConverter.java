package allwhite.tools.support;

import allwhite.tools.Release;
import allwhite.tools.ToolSuiteDownloads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class ToolXmlConverter {
    public ToolSuiteDownloads convert(ToolSuiteXml toolSuiteXml, String toolSuiteName, String shortname) {
        ToolSuiteBuilder state = new ToolSuiteBuilder(shortname);
        for (Release release : toolSuiteXml.getReleases()) {
            if (!release.getName().startsWith(toolSuiteName)) {
                continue;
            }
            state.setWhatsNew(release.getWhatsnew());
            release.getDownloads().forEach(state::addDownload);
        }
        return state.build();
    }

    public List<ToolSuiteDownloads> convertLegacy(ToolSuiteXml toolSuiteXml, String toolSuiteName, String shortname) {
        List<ToolSuiteDownloads> downloadsList = new ArrayList<>();

        toolSuiteXml.getOthers().stream()
                .filter(r -> r.getName().startsWith(toolSuiteName))
                .collect(Collectors.groupingBy(r -> r.getDownloads().get(0).getVersion()))
                .forEach((version, releases) -> {
                    ToolSuiteBuilder state = new ToolSuiteBuilder(shortname);
                    for (Release release : releases) {
                        state.setWhatsNew(release.getWhatsnew());
                        release.getDownloads().forEach(state::addDownload);
                    }
                    downloadsList.add(state.build());
                });
        Collections.sort(downloadsList, (i, j) -> j.getReleaseName().compareTo(i.getReleaseName()));
        return downloadsList;
    }
}
