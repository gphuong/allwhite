package allwhite.site.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum GuideType {
    GETTING_STARTED("getting-started"), TUTORIAL("tutorial"),
    TOPICAL("topical"), UNKNOWN("unknown");
    private final String name;

    GuideType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @JsonCreator
    public static GuideType fromName(String name) {
        return Arrays.stream(GuideType.values())
                .filter(type -> type.getName().equals(name))
                .findFirst().orElse(GuideType.UNKNOWN);
    }
}
