package allwhite.site.blog;

import allwhite.blog.Post;
import allwhite.blog.PostBuilder;
import allwhite.blog.PostCategory;
import allwhite.blog.PostFormat;
import allwhite.support.DateFactory;
import allwhite.support.DateTestUtils;
import allwhite.team.support.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class PostFormAdapter_UpdatePostTests {

    public static final String RENDERED_HTML = "<p>Rendered HTML</p><p>from Markdown</p>";
    public static final String SUMMARY = "<p>Rendered HTML</p>";
    private Post post;
    private String title = "Title";
    private String content = "Rendered HTML\n\nfrom Markdown";
    private PostCategory category = PostCategory.ENGINEERING;
    private boolean broadcast = true;
    private boolean draft = false;
    private Date publishedAt = DateTestUtils.getDate("2013-07-01 12:00");
    private Date now = DateTestUtils.getDate("2013-07-01 13:00");
    private PostForm postForm;
    private String ORIGINAL_AUTHOR = "original author";

    @Mock
    private DateFactory dateFactory;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PostContentRenderer renderer;

    @Mock
    private PostSummary postSummary;

    private PostFormAdapter postFormAdapter;

    @Before
    public void setUp() {
        given(dateFactory.now()).willReturn(now);
        given(postSummary.forContent(anyString(), anyInt())).willReturn(SUMMARY);
        given(renderer.render(content, PostFormat.MARKDOWN)).willReturn(RENDERED_HTML);

        post = PostBuilder.post().id(123L).author("author_id", ORIGINAL_AUTHOR).build();

        postForm = new PostForm(post);
        postForm.setTitle(title);
        postForm.setContent(content);
        postForm.setCategory(category);
        postForm.setBroadcast(broadcast);
        postForm.setPublishAt(publishedAt);

        postFormAdapter = new PostFormAdapter(renderer, postSummary, dateFactory, teamRepository);
        postFormAdapter.updatePostFromPostForm(post, postForm);
    }

    @Test
    public void postRetainsOriginalAuthor() {
        assertThat(post.getAuthor().getName(), equalTo(ORIGINAL_AUTHOR));
    }

    @Test
    public void postHasRenderedContent() {
        assertThat(post.getRenderedContent(), equalTo(RENDERED_HTML));
    }

    @Test
    public void postHasRenderedSummary() {
        assertThat(post.getRenderedSummary(), equalTo(SUMMARY));
    }

    @Test
    public void draftWithNullPublishDate() {
        postForm.setDraft(true);
        postForm.setPublishAt(null);
        postFormAdapter.updatePostFromPostForm(post, postForm);
        assertThat(post.getPublishAt(), is(nullValue()));
    }

    @Test
    public void postWithNullPublishDateSetsPublishAtToNow() {
        postForm.setDraft(false);
        postForm.setPublishAt(null);
        postFormAdapter.updatePostFromPostForm(post, postForm);
        assertThat(post.getPublishAt(), equalTo(now));
    }

    @Test
    public void updatingABlogPost_doesNotChangeItsCreatedDateByDefault() {
        Date originalDate = DateTestUtils.getDate("2009-11-20 07:00");
        Post post = PostBuilder.post().createdAt(originalDate).build();
        postFormAdapter.updatePostFromPostForm(post, postForm);
        assertThat(post.getCreatedAt(), is(originalDate));
    }

    @Test
    public void updatingABlogPost_usesTheCreatedDateFromThePostFormIfPresent() {
        Date originalDate = DateTestUtils.getDate("2009-11-20 07:00");
        Post post = PostBuilder.post().createdAt(originalDate).build();

        Date newDate = DateTestUtils.getDate("2010-01-11 03:00");
        postForm.setCreatedAt(newDate);

        postFormAdapter.updatePostFromPostForm(post, postForm);
        assertThat(post.getCreatedAt(), is(newDate));
    }
}
