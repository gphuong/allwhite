package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.blog.PostMovedException;
import allwhite.site.blog.BlogService;
import allwhite.support.DateFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;

public class BlogControllerTests {

    @Mock
    private BlogService blogService;
    private BlogController blogController;

    private DateFactory dateFactory = new DateFactory();
    private Locale defaultLocale;

    private ExtendedModelMap model = new ExtendedModelMap();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PageImpl<Post> page = new PageImpl<>(Collections.emptyList(), new PageRequest(1, 1), 1);
        given(blogService.getPublishedPostsByDate(anyInt(), any(Pageable.class))).willReturn(page);
        given(blogService.getPublishedPostsByDate(anyInt(), anyInt(), any(Pageable.class))).willReturn(page);
        given(blogService.getPublishedPostsByDate(anyInt(), anyInt(), anyInt(), any(Pageable.class))).willReturn(page);

        blogController = new BlogController(blogService, dateFactory);

        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @After
    public void tearDown() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void titleForBlogYearPage() {
        blogController.listPublishedPostsForYear(2013, 1, model);
        String title = (String) model.get("title");
        assertThat(title, equalTo("Archive for 2013"));
    }

    @Test
    public void titleForBlogYearMonthPage() {
        blogController.listPublishedPostsForYearAndMonth(2013, 1, 1, model);
        String title = (String) model.get("title");
        assertThat(title, equalTo("Archive for January 2013"));
    }

    @Test
    public void titleForBlogYearMonthDayPage() {
        blogController.listPublishedPostsForDate(2011, 3, 23, 1, model);
        String title = (String) model.get("title");
        assertThat(title, equalTo("Archive for March 23, 2011"));
    }

    @Test
    public void handleBlogPostMovedExceptionRedirects() {
        String publicSlug = "slug";
        RedirectView result = blogController.handle(new PostMovedException(publicSlug));
        assertThat(result.getUrl(), equalTo("/blog/" + publicSlug));
    }
}
