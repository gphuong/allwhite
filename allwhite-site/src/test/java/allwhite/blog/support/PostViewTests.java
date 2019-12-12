package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.blog.PostBuilder;
import allwhite.support.DateFactory;
import allwhite.support.DateTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

public class PostViewTests {
    @Mock
    private DateFactory dateFactory;
    private Locale defaultLocale;

    private Post post;
    private PostView postView;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @After
    public void tearDown() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void formattedPublishDateForUnscheduledDraft() {
        post = PostBuilder.post().draft().unscheduled().build();
        postView = PostView.of(post, dateFactory);

        assertThat(postView.getFormattedPublishDate(), equalTo("Unscheduled"));
    }

    @Test
    public void formattedPublishDateForPublishedPosts() throws ParseException {
        post = PostBuilder.post().publishAt("2012-07-02 13:42").build();
        postView = PostView.of(post, dateFactory);

        assertThat(postView.getFormattedPublishDate(), equalTo("July 02, 2012"));
    }

    @Test
    public void draftPath() {
        given(dateFactory.now()).willReturn(DateTestUtils.getDate("2012-07-02 13:42"));
        post = PostBuilder.post().id(123L).title("My Post").draft().build();
        postView = PostView.of(post, dateFactory);

        assertThat(postView.getPath(), equalTo("/admin/blog/123-my-post"));
    }

    @Test
    public void scheduledPost() throws ParseException {
        given(dateFactory.now()).willReturn(DateTestUtils.getDate("2012-07-02 13:42"));
        post = PostBuilder.post().id(123L).title("My Post").publishAt("2012-07-05 13:42").build();
        postView = PostView.of(post, dateFactory);

        assertThat(postView.getPath(), equalTo("/admin/blog/123-my-post"));
    }

    @Test
    public void publishedPost() throws ParseException {
        given(dateFactory.now()).willReturn(DateTestUtils.getDate("2012-07-02 13:42"));
        post = PostBuilder.post().id(123L).title("My Post").publishAt("2012-07-11 13:42").build();
        postView = PostView.of(post, dateFactory);

        assertThat(postView.getPath(), equalTo("/blog/2012/07/01/my-post"));
    }

    @Test
    public void knowsWhenSummaryAndContentDiffer() {
        Post post = PostBuilder.post().renderedContent("A string")
                .renderedSummary("A different string")
                .build();
        postView = PostView.of(post, dateFactory);

        assertThat(postView.showReadMore(), is(true));
    }

    @Test
    public void knowsWhenSummaryAndContentAreEqual() {
        String content = "Test content";
        Post post = PostBuilder.post().renderedContent(content)
                .renderedSummary(content)
                .build();
        postView = PostView.of(post, dateFactory);

        assertThat(postView.showReadMore(), is(false));
    }

}
