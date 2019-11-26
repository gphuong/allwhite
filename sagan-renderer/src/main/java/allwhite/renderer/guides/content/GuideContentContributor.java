package allwhite.renderer.guides.content;

import allwhite.renderer.guides.GuideContentResource;

import java.io.File;

public interface GuideContentContributor {
    void contribute(GuideContentResource guideContent, File repositoryRoot);
}
