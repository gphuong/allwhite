package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.support.DateFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public final class PostView {
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");
    private final Post post;
    private final DateFactory dateFactory;


    public PostView(Post post, DateFactory dateFactory) {
        this.post = post;
        this.dateFactory = dateFactory;
    }

    public static PostView of(Post post, DateFactory dateFactory) {
        return new PostView(post, dateFactory);
    }

    public static Page<PostView> pageOf(Page<Post> posts, DateFactory dateFactory) {
        List<PostView> postViews = posts.getContent().stream()
                .map(post -> of(post, dateFactory))
                .collect(Collectors.toList());
        PageRequest pageRequest = new PageRequest(posts.getNumber(), posts.getSize(), posts.getSort());
        return new PageImpl<>(postViews, pageRequest, posts.getTotalElements());
    }

    public String getTitle() {
        return post.getTitle();
    }

    public String getPath() {
        String path;
        if (post.isLiveOn(dateFactory.now())) {
            path = "/blog/" + post.getPublicSlug();
        } else {
            path = "/admin/blog/" + post.getAdminSlug();
        }
        return path;
    }

    public Long getId() {
        return post.getId();
    }

    public String getFormattedPublishDate() {
        return post.isScheduled() ? "Unscheduled" : DATE_FORMAT.format(post.getPublishAt());
    }

    public boolean showReadMore() {
        return !post.getRenderedContent().equals(post.getRenderedSummary());
    }
}
