package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.blog.PostBuilder;
import allwhite.blog.PostCategory;
import allwhite.site.blog.BlogService;
import allwhite.support.DateFactory;
import allwhite.support.nav.PageableFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;

import static allwhite.blog.PostCategory.ENGINEERING;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;

public class BlogController_PublishedPostsForCategoryTests {
    private static final int TEST_PAGE = 1;
    public static final PostCategory TEST_CATEGORY = ENGINEERING;

    @Mock
    private BlogService blogService;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    private BlogController controller;
    private DateFactory dateFactory = new DateFactory();
    private ExtendedModelMap model = new ExtendedModelMap();
    private List<PostView> posts = new ArrayList<>();
    private Page<PostView> page;
    private String viewName;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new BlogController(blogService, dateFactory);

        List<Post> posts = new ArrayList<>();
        posts.add(PostBuilder.post().title("post title").build());
        Page<Post> postsPage = new PageImpl<>(posts, new PageRequest(TEST_PAGE, 10), 20);
        Pageable testPageable = PageableFactory.forLists(TEST_PAGE);

        page = new PageImpl<>(new ArrayList<>(), testPageable, 1);

        given(blogService.getPublishedPosts(eq(TEST_CATEGORY), eq(testPageable))).willReturn(postsPage);

        request.setServletPath("/blog");

        viewName = controller.listPublishedPostsForCategory(TEST_CATEGORY, model, TEST_PAGE);
    }

    @Test
    public void providesAllCategoriesInModel() {
        assertThat(model.get("categories"), is(PostCategory.values()));
    }

    @Test
    public void providesPaginationInfoInModel() {
        assertThat(model.get("paginationInfo"), notNullValue());
    }

    @Test
    public void viewNameIsIndex() {
        assertThat(viewName, is("blog/index"));
    }

    @Test
    public void postsInModel() {
        controller.listPublishedPostsForCategory(TEST_CATEGORY, model, TEST_PAGE);
        assertThat(((List<PostView>) model.get("posts")).get(0).getTitle(), is("post title"));
    }
}
