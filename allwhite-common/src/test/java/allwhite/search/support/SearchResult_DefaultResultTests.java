package allwhite.search.support;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchResult_DefaultResultTests {

    @Test
    public void resultText_returnsSummary_whenNoHighlight() {
        SearchResult result =
                new SearchResult("id", "title", "subTitle", "summary", "path", "site",
                        null, "original search term");
        assertThat(result.getDisplayText(), equalTo("summary"));
    }

    @Test
    public void resultText_returnsHighlight_whenPresent() {
        SearchResult result =
                new SearchResult("id", "title", "subTitle", "summary", "path", "site",
                        "highlight", "original search term");
        assertThat(result.getDisplayText(), equalTo("highlight"));
    }
}
