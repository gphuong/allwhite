package allwhite.search.support;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;

public class SearchControllerTests {

    @Mock
    private SearchService searhService;

    private SearchController controller;
    private ExtendedModelMap model = new ExtendedModelMap();
    private Page<SearchResult> resultsPage;
    private List<SearchResult> entries = new ArrayList<>();
    private SearchForm searchForm = new SearchForm();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new SearchController(searhService);
        SearchResult entry = new SearchResult("", "", "", "", "", "", "",
                "original search term");
        entries.add(entry);
        resultsPage = new PageImpl<>(entries);
        given(searhService.search(anyString(), anyObject(), anyList())).willReturn(
                new SearchResults(resultsPage, Collections.emptyList()));
    }

    @Test
    public void search_providesQueryInModel() {
        searchForm.setQ("searchTerm");
        controller.search(searchForm, 1, model);
        assertThat(model.get("searchForm"), equalTo(searchForm));
    }

    @Test
    public void search_providesPaginationInfoInModel() {
        searchForm.setQ("searchTerm");
        controller.search(searchForm, 1, model);
        assertThat(model.get("paginationInfo"), is(notNullValue()));
    }

    @Test
    public void search_providesResultsInModel() {
        searchForm.setQ("searchTerm");
        controller.search(searchForm, 1, model);
        assertThat(model.get("results"), equalTo(entries));
    }

    @Test
    public void search_providedAllResultsForBlankQuery() {
        searchForm.setQ("");
        controller.search(searchForm, 1, model);
        assertThat(model.get("results"), equalTo(entries));
    }

}
