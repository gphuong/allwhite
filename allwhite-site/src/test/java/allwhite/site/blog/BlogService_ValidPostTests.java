package allwhite.site.blog;

import allwhite.blog.Post;
import allwhite.blog.PostBuilder;
import allwhite.search.SearchException;
import allwhite.search.support.SearchService;
import allwhite.support.DateFactory;
import allwhite.support.DateTestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlogService_ValidPostTests {

    private static final String AUTHOR_USERNAME = "username";
    private Post post;
    private PostForm postForm = new PostForm();
    private Date publishAt = DateTestUtils.getDate("2013-07-01 12:00");
    private Date now = DateTestUtils.getDate("2013-07-01 13:00");

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostFormAdapter postFormAdapter;

    @Mock
    private DateFactory dateFactory;

    @Mock
    private SearchService searchService;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private BlogService service;

    @Before
    public void setUp() {
        given(dateFactory.now()).willReturn(now);

        given(postRepository.save((Post) anyObject())).will(invocation -> {
            Post po = (Post) invocation.getArguments()[0];
            ReflectionTestUtils.setField(post, "id", 123L);
            return post;
        });

        post = PostBuilder.post().publishAt(publishAt).build();
        given(postFormAdapter.createPostFromPostForm(postForm, AUTHOR_USERNAME)).willReturn(post);

        service = new BlogService(postRepository, postFormAdapter, dateFactory, searchService);
        service.addPost(postForm, AUTHOR_USERNAME);
    }

    @Test
    public void createsAPost() {
        verify(postFormAdapter).createPostFromPostForm(postForm, AUTHOR_USERNAME);
    }

    @Test
    public void postIsPersisted() {
        verify(postRepository).save((Post) anyObject());
    }

    @Test
    public void creatingABlogPost_addsThatPostToTheSearchIndexifPublished() {
        verify(searchService).saveToIndex(anyObject());
    }

    @Test
    public void blogIsSaveWhenSearchServiceIsDown() {
        reset(postRepository);
        willThrow(SearchException.class).given(searchService).saveToIndex(anyObject());
        post = service.addPost(postForm, AUTHOR_USERNAME);
        verify(postRepository).save(post);
    }

    @Test
    public void creatingABlogPost_doesNotSaveToSearchIndexIfNotLive() {
        reset(searchService);

        PostForm draftPostForm = new PostForm();
        draftPostForm.setDraft(true);

        Post draft = PostBuilder.post().draft().build();
        given(postFormAdapter.createPostFromPostForm(draftPostForm, AUTHOR_USERNAME)).willReturn(draft);

        service.addPost(draftPostForm, AUTHOR_USERNAME);
        verifyZeroInteractions(searchService);
    }
}
