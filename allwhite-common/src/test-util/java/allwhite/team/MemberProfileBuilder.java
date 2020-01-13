package allwhite.team;

public class MemberProfileBuilder {

    private String name;
    private GeoLocation geoLocation;
    private String username;
    private Long id;
    private String location;
    private String bio;
    private String avatarUrl;
    private boolean hidden;

    public static MemberProfileBuilder profile() {
        return new MemberProfileBuilder();
    }

    public MemberProfileBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MemberProfileBuilder geoLocation(float latitude, int longtitude) {
        geoLocation = new GeoLocation(latitude, longtitude);
        return this;
    }

    public MemberProfileBuilder username(String username) {
        this.username = username;
        return this;
    }

    public MemberProfileBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MemberProfile build() {
        MemberProfile profile = new MemberProfile(id);
        profile.setName(name);
        profile.setUsername(username);
        profile.setLocation(location);
        profile.setBio(bio);
        profile.setAvatarUrl(avatarUrl);
        profile.setGeoLocation(geoLocation);
        profile.setHidden(hidden);
        return profile;
    }
}
