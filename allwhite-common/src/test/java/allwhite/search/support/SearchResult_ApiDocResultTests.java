package allwhite.search.support;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchResult_ApiDocResultTests {

    @Test
    public void resultText_returnsSummary_whenNoHighlight() {
        SearchResult result = new SearchResult("id", "title", "subTitle", "summary", "path", "apiDoc", null, "original search term");
        assertThat(result.getDisplayText(), equalTo("summary"));
    }

    @Test
    public void resultText_returnsHightlight_whenPresent() {
        SearchResult result =
                new SearchResult("id", "title", "subTitle", "summary", "path", "apiDoc",
                        "highlight", "original search term");
        assertThat(result.getDisplayText(), equalTo("highlight"));
    }

    @Test
    public void resultText_returnsSummary_whenTermMatchesTitle_ignoringHighlight() {
        SearchResult result =
                new SearchResult("id", "title", "subTitle", "summary", "path", "apiDoc",
                        "highlight", "title");
        assertThat(result.getDisplayText(), equalTo("summary"));
    }
}
