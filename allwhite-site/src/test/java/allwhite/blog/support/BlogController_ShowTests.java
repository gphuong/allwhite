package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.blog.PostBuilder;
import allwhite.blog.PostCategory;
import allwhite.site.blog.BlogService;
import allwhite.support.DateFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

public class BlogController_ShowTests {

    @Mock
    private BlogService blogService;

    @Mock
    private HttpServletRequest request;

    private DateFactory dateFactory;
    private BlogController controller;
    private ExtendedModelMap model = new ExtendedModelMap();
    private String viewName;
    private Post post;

    @Before
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);

        controller = new BlogController(blogService, dateFactory);

        post = PostBuilder.post().publishAt("2012-02-11 11:00").build();
        given(blogService.getPublishedPost("2012/02/01/title")).willReturn(post);
        given(blogService.getDisqusShortname()).willReturn("spring-io-test");
        viewName = controller.showPost("2012", "02", "01", "title", model);
    }

    @Test
    public void proviesActiveCategoryInModel() {
        assertThat(model.get("activeCategory"), equalTo(post.getCategory().getDisplayName()));
    }

    @Test
    public void providesAllCategoriesInModel() {
        assertThat(model.get("categories"), is(PostCategory.values()));
    }

    @Test
    public void providesDisqusShortnameInModel() {
        assertThat(model.get("disqusShortname"), equalTo("spring-io-test"));
    }

    @Test
    public void viewNameShow() {
        assertThat(viewName, is("blog/show"));
    }

    @Test
    public void singlePostInModelForOnePost() {
        assertThat(((PostView) model.get("post")).getId(), is(post.getId()));
    }
}
