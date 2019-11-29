package allwhite.search.support;

import io.searchbox.core.Search;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AllwhiteQueryBuildersTest {

    @Test
    public void deleteUnsupportedProjectEntries() {
        String projectId = "spring-framework";
        List<String> supportedVersions = Arrays.asList("4.1.0.RELEASE", "4.0.0.RELEASE");
        String expected = "{" +
                "\"query\":{" +
                "\"filtered\":{" +
                "\"query\":{\"match_all\":{}}," +
                "\"filter\":{" +
                "\"and\":{\"filters\":[" +
                "{\"term\":{\"projectId\":\"spring-framework\"}}," +
                "{\"not\":{\"filter\":{" +
                "\"or\":{\"filters\":[" +
                "{\"term\":{\"version\":\"4.1.0.RELEASE\"}}," +
                "{\"term\":{\"version\":\"4.0.0.RELEASE\"}}" +
                "]}" +
                "}}}" +
                "]}" +
                "}" +
                "}" +
                "}}";
        FilteredQueryBuilder builder = AllwhiteQueryBuilders.matchUnsupportedProjectEntries(projectId, supportedVersions);
        String result = AllwhiteQueryBuilders.wrapQuery(builder.toString());
        assertThat(result.replaceAll("[\\s|\\r|\\n]", ""), equalTo(expected));
    }

    @Test
    public void fullTextSearch() {
        String query = "spring boot";
        List<String> filters = Arrays.asList("Projects/Api", "Project/Reference", "Blog/Engineering", "Projects/Reactor Project/1.1.0.RELEASE");


        Search.Builder builder = AllwhiteQueryBuilders.fullTextSearch(query, new PageRequest(0, 10), filters);
    }
}
