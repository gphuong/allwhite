package allwhite.site.blog;

import allwhite.blog.Post;
import allwhite.blog.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByDraftFalseAndPublishAtBeforeOrderByPublishAtDesc(Date publishedBefore, Pageable pageRequest);

    Page<Post> findByCategoryAndDraftFalseAndPublishAtBefore(PostCategory category, Date publishedBefore,
                                                             Pageable pageRequest);

    Page<Post> findByCategoryAndDraftFalseAndPublishAtBefore(boolean broadcast, Date publishedBefore,
                                                             Pageable pageRequest);

    Page<Post> findByDraftTrue(Pageable pageRequest);

    Post findByPublicSlugAndDraftFalseAndPublishAtBefore(String publicSlug, Date now);

    Post findByPublicSlugAliasesInAndDraftFalseAndPublishAtBefore(Set<String> publicSlugAlias, Date now);

    @Query("select p from Post p where YEAR(p.publishAt) = ?1 and MONTH(p.publishAt) = ?2")
    Page<Post> findByDate(int year, Pageable pageRequest);

    @Query("select p from Post p where YEAR(p.publishAt) = ?1")
    Page<Post> findByDate(int year, int month, Pageable pageRequest);

    @Query("select p from Post p where YEAR(p.publishAt) = ?1 and MONTH(p.publishAt) = ?2 and DAY(p.publishAt) = ?3")
    Page<Post> findByDate(int year, int month, int day, Pageable pageRequest);

}
