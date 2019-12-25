package allwhite.site.guides;

import allwhite.projects.Project;
import allwhite.site.renderer.AllwhiteRendererClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GettingStartedGuides implements GuidesRepository<GettingStartedGuide> {

    private static Logger logger = LoggerFactory.getLogger(GettingStartedGuides.class);

    private static final String CACHE_GUIDES = "cache.guides";
    private static final String CACHE_GUIDE = "cache.guide";
    private final AllwhiteRendererClient client;

    public GettingStartedGuides(AllwhiteRendererClient client) {
        this.client = client;
    }

    @Cacheable(cacheNames = CACHE_GUIDES, key = "#project.id")
    public GuideHeader[] findByProject(Project project) {
        return Arrays.stream(findAll())
                .filter(guide -> guide.getProjects().contains(project.getId()))
                .toArray(GuideHeader[]::new);
    }

    @Cacheable(CACHE_GUIDES)
    public GuideHeader[] findAll() {
        return Arrays.stream(this.client.fetchGettingStartedGuides())
                .map(DefaultGuideHeader::new)
                .toArray(DefaultGuideHeader[]::new);
    }

    @CacheEvict(CACHE_GUIDE)
    public void evictFromCache(String guide) {
        logger.info("Getting Started guide evicted from cache: {}", guide);
    }
}
