package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.blog.PostBuilder;
import allwhite.blog.PostCategory;
import allwhite.blog.PostFormat;
import allwhite.site.blog.BlogService;
import allwhite.site.blog.PostForm;
import allwhite.support.DateFactory;
import allwhite.support.nav.PageableFactory;
import allwhite.team.MemberProfile;
import allwhite.team.support.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.MapBindingResult;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class BlogAdminControllerTests {
    private static final Post TEST_POST = PostBuilder.post()
            .id(123L)
            .publishYesterday()
            .build();
    private BlogAdminController controller;

    @Mock
    private BlogService blogService;

    private DateFactory dateFactory = new DateFactory();
    private ExtendedModelMap model = new ExtendedModelMap();
    private Principal principal;
    private MapBindingResult bindingResult;

    @Mock
    private TeamRepository teamRepository;

    @Before
    public void setup() {
        bindingResult = new MapBindingResult(new HashMap<>(), "postForm");
        principal = () -> "12345";

        controller = new BlogAdminController(blogService, teamRepository, dateFactory);
    }

    @Test
    public void dashboardShowsUsersPosts() {
        controller = new BlogAdminController(blogService, teamRepository, dateFactory);

        Page<Post> drafts = new PageImpl<>(
                Arrays.asList(new Post("draft post", "body", PostCategory.ENGINEERING, PostFormat.MARKDOWN)),
                PageableFactory.forDashboard(1), 1);
        Page<Post> scheduled = new PageImpl<>(
                Arrays.asList(new Post("scheduled post", "body", PostCategory.ENGINEERING, PostFormat.MARKDOWN)),
                PageableFactory.forDashboard(1), 1);
        Page<Post> published = new PageImpl<>(
                Arrays.asList(new Post("published post", "body", PostCategory.ENGINEERING, PostFormat.MARKDOWN)),
                PageableFactory.forDashboard(1), 1);

        given(blogService.getPublishedPosts(anyObject())).willReturn(published);
        given(blogService.getDraftPosts(anyObject())).willReturn(drafts);
        given(blogService.getScheduledPosts(anyObject())).willReturn(scheduled);

        ExtendedModelMap model = new ExtendedModelMap();
        controller.dashboard(model, 1);

        assertThat(((Page<PostView>) model.get("drafts")).getContent().get(0).getTitle(), equalTo("draft post"));
        assertThat(((Page<PostView>) model.get("scheduled")).getContent().get(0).getTitle(), equalTo("scheduled post"));
        assertThat(((Page<PostView>) model.get("posts")).getContent().get(0).getTitle(), equalTo("published post"));
    }

    @Test
    public void showPostModel() {
        Post post = PostBuilder.post().build();
        given(blogService.getPost(post.getId())).willReturn(post);
        controller.showPost(post.getId(), "1-post-title", model);
        PostView view = (PostView) model.get(("post"));
        assertThat(view, is(notNullValue()));
    }

    @Test
    public void showPostView() {
        assertThat(controller.showPost(1L, "not important", model), is("admin/blog/show"));
    }

    @Test
    public void creatingABlogPostRecordsTheUser() {
        String username = "username";

        MemberProfile member = new MemberProfile();
        member.setUsername(username);

        given(teamRepository.findById(12345L)).willReturn(member);
        PostForm postForm = new PostForm();

        given(blogService.addPost(eq(postForm), anyString())).willReturn(TEST_POST);
        controller.createPost
    }
}
