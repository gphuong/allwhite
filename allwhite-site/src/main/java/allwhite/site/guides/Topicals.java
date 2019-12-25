package allwhite.site.guides;

import allwhite.projects.Project;
import allwhite.site.renderer.AllwhiteRendererClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class Topicals implements GuidesRepository<Topical> {
    private static final String CACHE_TOPICALS = "cache.topicals";
    public static final String CACHE_TOPICAL = "cache.topical";
    private static Logger logger = LoggerFactory.getLogger(Topicals.class);
    private final AllwhiteRendererClient client;

    @Autowired
    public Topicals(AllwhiteRendererClient client) {
        this.client = client;
    }

    @Cacheable(cacheNames = CACHE_TOPICALS, key = "#project.id")
    public GuideHeader[] findByProject(Project project) {
        return Arrays.stream(findAll())
                .filter(guide -> guide.getProjects().contains(project.getId()))
                .toArray(GuideHeader[]::new);
    }

    @Cacheable(CACHE_TOPICALS)
    public GuideHeader[] findAll() {
        return Arrays.stream(this.client.fetchAllGuides())
                .map(DefaultGuideHeader::new)
                .toArray(DefaultGuideHeader[]::new);
    }

    @CacheEvict(CACHE_TOPICAL)
    public void evictFromCache(String guide) {
        logger.info("Tutorial evicted from cache: {}", guide);
    }

}
