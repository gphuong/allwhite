package allwhite.site.renderer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GuideContent {

    private String repositoryName;

    private String tableOfContents;

    private String content;

    private String pushToPwsMetadata;

    private List<GuideImage> images;

    public GuideContent(@JsonProperty("repositoryName") String repositoryName,
                        @JsonProperty("tableOfContents") String tableOfContents,
                        @JsonProperty("content") String content,
                        @JsonProperty("pushToPwsMetadata") String pushToPwsMetadata,
                        @JsonProperty("images") List<GuideImage> images) {
        this.repositoryName = repositoryName;
        this.tableOfContents = tableOfContents;
        this.content = content;
        this.pushToPwsMetadata = pushToPwsMetadata;
        this.images = images;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getTableOfContents() {
        return tableOfContents;
    }

    public String getContent() {
        return content;
    }

    public String getPushToPwsMetadata() {
        return pushToPwsMetadata;
    }

    public List<GuideImage> getImages() {
        return images;
    }
}
