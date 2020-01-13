package allwhite.site.guides;

import allwhite.site.renderer.GuideContent;

public class GettingStartedGuide extends AbstractGuide {
    private static final String TYPE_LABEL = "Getting Started";

    public GettingStartedGuide() {
        this.setTypeLabel(TYPE_LABEL);
    }

    public GettingStartedGuide(GuideHeader header, GuideContent content) {
        super(TYPE_LABEL, header, content);
    }
}
