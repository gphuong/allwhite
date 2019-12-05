package allwhite.tools;

import java.util.List;

public class EclipsePlatform {
    private final List<EclipseRelease> products;
    private String name;

    public EclipsePlatform(String name, List<EclipseRelease> products) {
        this.products = products;
        this.name = name;
    }

    public List<EclipseRelease> getReleases() {
        return products;
    }

    public String getName() {
        return name;
    }
}
