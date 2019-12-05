package allwhite.tools.support;

import allwhite.tools.Release;
import allwhite.tools.ToolSuiteDownloads;

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
}
