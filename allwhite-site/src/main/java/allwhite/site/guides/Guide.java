package allwhite.site.guides;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface Guide extends GuideHeader {
    String getContent();

    String getTableOfContents();

    Optional<byte[]> getImageContent(String imageName);

    String getPushToPwsUrl();
}
