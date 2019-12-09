package allwhite.site.blog;

import allwhite.blog.Post;
import allwhite.blog.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByDraftFalseAndPublishAtBeforeOrderByPublishAtDesc(Date publishedBefore, Pageable pageRequest);

    Page<Post> findByCategoryAndDraftFalseAndPublishAtBefore(PostCategory category, Date publishedBefore,
                                                             Pageable pageRequest);

    Page<Post> findByCategoryAndDraftFalseAndPublishAtBefore(boolean broadcast, Date publishedBefore,
                                                             Pageable pageRequest);

    Page<Post> findByDraftTrue(Pageable pageRequest);
}
