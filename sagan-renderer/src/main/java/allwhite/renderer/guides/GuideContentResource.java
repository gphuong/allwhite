package allwhite.renderer.guides;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class GuideContentResource extends ResourceSupport {
    private String name;

    private String tableOfContents;

    private String content;

    private String pushToPwsMetadata;

    private List<GuideImage> images;

    public GuideContentResource(String name, String content, String tableOfContents) {
        this.name = name;
        this.tableOfContents = tableOfContents;
        this.content = content;
    }
    GuideContentResource(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableOfContents() {
        return tableOfContents;
    }

    public void setTableOfContents(String tableOfContents) {
        this.tableOfContents = tableOfContents;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPushToPwsMetadata() {
        return pushToPwsMetadata;
    }

    public void setPushToPwsMetadata(String pushToPwsMetadata) {
        this.pushToPwsMetadata = pushToPwsMetadata;
    }

    public List<GuideImage> getImages() {
        return images;
    }

    public void setImages(List<GuideImage> images) {
        this.images = images;
    }
}
