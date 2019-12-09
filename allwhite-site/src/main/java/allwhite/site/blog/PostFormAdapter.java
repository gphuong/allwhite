package allwhite.site.blog;

import allwhite.blog.Post;
import allwhite.team.MemberProfile;
import allwhite.team.support.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class PostFormAdapter {

    private static final int SUMMARY_LENGTH = 500;
    private final PostContent
    private final TeamRepository teamRepository;

    public Post createPostFromPostForm(PostForm postForm, String username) {
        String content = postForm.getContent();
        Post post = new Post(postForm.getTitle(), content, postForm.getCategory(), postForm.getFormat());
        MemberProfile profile = teamRe
    }
}
