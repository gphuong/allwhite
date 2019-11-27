package allwhite.team;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
public class MemberProfile {
    public static final MemberProfile NOT_FOUND = new MemberProfile();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String jobTitle;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true)
    @Type(type = "text")
    private String bio;

    @Column(nullable = true)
    private String avatarUrl;

    @Column(nullable = true)
    private String gravatarEmail;

    @Column(nullable = true)
    private String githubUsername;

    @Column(nullable = false)
    private String username;

    @Column(nullable = true)
    private String speakerdeckUsername;

    @Column(nullable = true)
    private String lanyrdUsername;

    @Column(nullable = true)
    private Long githubId;

    @Column
    private GeoLocation geoLocation;

    @Column
    @Type(type = "text")
    private String videoEmbeds;

    @Column
    private boolean hidden;

    public MemberProfile() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
