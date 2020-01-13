package allwhite.team;

public class TeamLocation {
    private String name;
    private float latitude;
    private float longtitude;
    private Long memberId;

    public TeamLocation() {
    }

    public TeamLocation(String name, float latitude, float longtitude, Long memberId) {
        this.name = name;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public Long getMemberId() {
        return memberId;
    }
}

