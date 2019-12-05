package allwhite.tool;

import java.util.List;

public class EclipseRelease {
    private final String name;
    private List<EclipsePackage> packages;
    private String eclipseVersion;

    public EclipseRelease(String name, String eclipseVersion, List<EclipsePackage> packages) {
        this.name = name;
        this.packages = packages;
        this.eclipseVersion = eclipseVersion;
    }
}
