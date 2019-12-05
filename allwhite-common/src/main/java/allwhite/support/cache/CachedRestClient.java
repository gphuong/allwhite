package allwhite.support.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class CachedRestClient {

    public static final String CACHE_NAME = "cache.network";
    public static final String CACHE_TTL_KEY = "cache.network.timetolive";
    public static final String CACHE_TTL = "${cache.network.timetolive:300}";

    @Cacheable(value = CACHE_NAME, key = "#url")
    public <T> T get(RestOperations operations, String url, Class<T> clazz) {
        return operations.getForObject(url, clazz);
    }
}
