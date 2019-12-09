package allwhite;

import allwhite.support.cache.CachedRestClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@EnableCaching(proxyTargetClass = true)
@Profile(AllwhiteProfiles.STANDALONE)
public class CacheConfig {

}
