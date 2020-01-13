package allwhite.tools;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SpringToolsPlatform {
    @Id
    private String id;

    @ElementCollection
    private List<SpringToolsDownload> downloads = new ArrayList<>();

    public SpringToolsPlatform() {
    }

    public SpringToolsPlatform(String id) {
        this.id = id;
    }

    public SpringToolsPlatform(String id, List<SpringToolsDownload> downloads) {
        this.id = id;
        this.downloads = downloads;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SpringToolsDownload> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<SpringToolsDownload> downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return "SpringToolsPlatform{" +
                "id='" + id + '\'' +
                ", downloads=" + downloads +
                '}';
    }
}
