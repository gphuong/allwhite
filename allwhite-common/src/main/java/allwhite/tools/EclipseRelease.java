package allwhite.tools;

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

    public String getName() {
        return name;
    }

    public List<EclipsePackage> getPackages() {
        return packages;
    }

    public String getEclipseVersion() {
        return eclipseVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        EclipseRelease that = (EclipseRelease) obj;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
