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
public class Tutorials implements GuidesRepository<Tutorial> {
    private static final String CACHE_TUTORIALS = "cache.tutorials";
    private static final String CACHE_TUTORIAL = "cache.tutorial";
    private final AllwhiteRendererClient client;
    private static Logger logger = LoggerFactory.getLogger(Tutorials.class);

    public Tutorials(AllwhiteRendererClient client) {
        this.client = client;
    }

    @Override
    @Cacheable(cacheNames = CACHE_TUTORIALS, key = "#project.id")
    public GuideHeader[] findByProject(Project project) {
        return Arrays.stream(findAll())
                .filter(guide -> guide.getProjects().contains(project.getId()))
                .toArray(GuideHeader[]::new);
    }

    @Cacheable(CACHE_TUTORIALS)
    public GuideHeader[] findAll() {
        return Arrays.stream(this.client.fetchTutorialGuides())
                .map(DefaultGuideHeader::new)
                .toArray(DefaultGuideHeader[]::new);
    }

    @CacheEvict(CACHE_TUTORIAL)
    public void evictFromCache(String guide) {
        logger.info("Tutorial evicted from cache: {}", guide);
    }
}
