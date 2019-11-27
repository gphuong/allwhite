package allwhite.blog;

import allwhite.team.MemberProfile;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Post {

    private static final SimpleDateFormat SLUG_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private MemberProfile author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostFormat format;

    @Column(nullable = false)
    @Type(type = "text")
    private String rawContent;

    @Column(nullable = false)
    @Type(type = "text")
    private String renderedContent;

    @Column(nullable = false)
    @Type(type = "text")
    private String renderedSummary;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @Column(nullable = false)
    private boolean draft = true;

    @Column(nullable = false)
    private boolean broadcast = false;

    @Column(nullable = true)
    private Date publishAt;

    @Column(nullable = true)
    private String publicSlug;

    @ElementCollection
    private Set<String> publicSlugAliases = new HashSet<>();

    private Post() {

    }

    public Post(String title, String content, PostCategory category, PostFormat format) {
        this.title = title;
        this.category = category;
        this.format = format;
        this.rawContent = content;
    }

    public Post(Long id, String title, String content, PostCategory category, PostFormat format) {
        this(title, content, category, format);
        this.id = id;
    }
}
