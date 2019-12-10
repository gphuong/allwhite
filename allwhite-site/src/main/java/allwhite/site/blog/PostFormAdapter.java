package allwhite.site.blog;

import allwhite.blog.Post;
import allwhite.support.DateFactory;
import allwhite.team.MemberProfile;
import allwhite.team.support.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class PostFormAdapter {
    private static final int SUMMARY_LENGTH = 500;

    private final PostContentRenderer renderer;
    private final PostSummary postSummary;
    private final TeamRepository teamRepository;
    private final DateFactory dateFactory;

    public PostFormAdapter(PostContentRenderer renderer, PostSummary postSummary,
                           DateFactory dateFactory, TeamRepository teamRepository) {
        this.renderer = renderer;
        this.postSummary = postSummary;
        this.teamRepository = teamRepository;
        this.dateFactory = dateFactory;
    }

    public Post createPostFromPostForm(PostForm postForm, String username) {
        String content = postForm.getContent();
        Post post = new Post(postForm.getTitle(), content, postForm.getCategory(), postForm.getFormat());
        MemberProfile profile = teamRepository.findByUsername(username);
        post.setAuthor(profile);
        post.setCreatedAt(createdDate(postForm, dateFactory.now()));

        setPostProperties(postForm, content, post);
        return post;
    }

    private void setPostProperties(PostForm postForm, String content, Post post) {
        String rendered = null;
        rendered = renderer.render(content, post.getFormat());
    }

    private Date createdDate(PostForm postForm, Date defaultDate) {
        Date createdAt = postForm.getCreatedAt();
        if (createdAt == null) {
            createdAt = defaultDate;
        }
        return createdAt;
    }

    public void refreshPost(Post post) {
        post.setRenderedContent(renderer.render(post.getRawContent(), post.getFormat()));
        summarize(post);
    }

    private void summarize(Post post) {
        String summary = postSummary.forContent(post.getRenderedContent(), SUMMARY_LENGTH);
        post.setRenderedContent(summary);
    }
}
