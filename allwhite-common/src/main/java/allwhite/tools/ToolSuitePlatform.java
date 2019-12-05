package allwhite.tools;

import java.util.List;

public class ToolSuitePlatform {
    private String name;
    private List<EclipseVersion> eclipseVersions;

    public ToolSuitePlatform(String name, List<EclipseVersion> eclipseVersions) {
        this.name = name;
        this.eclipseVersions = eclipseVersions;
    }

    public String getName() {
        return name;
    }

    public List<EclipseVersion> getEclipseVersions() {
        return eclipseVersions;
    }
}
