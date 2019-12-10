package allwhite.site.blog;

import allwhite.blog.Post;
import allwhite.blog.PostCategory;
import allwhite.blog.PostNotFoundException;
import allwhite.search.support.SearchService;
import allwhite.support.DateFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BlogService {
    private static final Log logger = LogFactory.getLog(BlogService.class);

    private final PostRepository postRepository;
    private final DateFactory dateFactory;
    private final PostFormAdapter postFormAdapter;
    private final SearchService searchService;
    private final PostSearchEntryMapper mapper = new PostSearchEntryMapper();

    public BlogService(PostRepository postRepository, PostFormAdapter postFormAdapter, DateFactory dateFactory,
                       SearchService searchService) {
        this.postRepository = postRepository;
        this.dateFactory = dateFactory;
        this.postFormAdapter = postFormAdapter;
        this.searchService = searchService;
    }

    public Page<Post> getScheduledPosts(Pageable pageRequest) {
        return postRepository.findByDraftFalseAndPublishAtBeforeOrderByPublishAtDesc(dateFactory.now(), pageRequest);
    }

    public Page<Post> getDraftPosts(Pageable pageRequest) {
        return postRepository.findByDraftTrue(pageRequest);
    }

    public Page<Post> getPublishedPosts(Pageable pageRequest) {
        return postRepository.findByDraftFalseAndPublishAtBeforeOrderByPublishAtDesc(dateFactory.now(), pageRequest);
    }


    public Page<Post> getPublishedPosts(PostCategory category, Pageable pageRequest) {
        return postRepository.findByCategoryAndDraftFalseAndPublishAtBefore(category, dateFactory.now(), pageRequest);
    }

    public Page<Post> getPublishedBroadcastPosts(Pageable pageRequest) {
        return postRepository.findByCategoryAndDraftFalseAndPublishAtBefore(true, dateFactory.now(), pageRequest);
    }

    public Post getPost(Long postId) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException(postId);
        }
        return post;
    }

    public Post addPost(PostForm postForm, String username) {
        Post post = postFormAdapter.createPostFromPostForm(postForm, username);
        postRepository.save(post);
        saveToIndex(post);
        return post;
    }

    private void saveToIndex(Post post) {
        if (post.isLiveOn(dateFactory.now())) {
            try {
                searchService.saveToIndex(mapper.map(post));
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public Page<Post> refreshPosts(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.DESC, "id");
        Page<Post> posts = postRepository.findAll(pageRequest);
        for (Post post : posts) {
            postFormAdapter.refreshPost(post);
            postRepository.save(post);
        }
        return posts;
    }
}
