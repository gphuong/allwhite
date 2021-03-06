package allwhite.renderer.guides.content;

import allwhite.renderer.guides.GuideContentResource;
import allwhite.renderer.guides.GuideImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class ImagesGuideContentContributor implements GuideContentContributor {
    private final Logger logger = LoggerFactory.getLogger(ImagesGuideContentContributor.class);

    private static final String IMAGES_DIRECTORY = "images";

    @Override
    public void contribute(GuideContentResource guideContent, File repositoryRoot) {
        File imagesDir = new File(repositoryRoot.getAbsolutePath() + File.separator + IMAGES_DIRECTORY);
        if (imagesDir.isDirectory()) {
            Base64.Encoder encoder = Base64.getEncoder();
            List<GuideImage> images = new ArrayList<>();
            File[] imageFiles = imagesDir.listFiles();
            if (imageFiles == null) {
                return;
            }
            for (File image : imageFiles) {
                if (image.isFile()){
                    try{
                        byte[] content = FileCopyUtils.copyToByteArray(image);
                        GuideImage guideImage = new GuideImage();
                        guideImage.setName(image.getName());
                        guideImage.setEncodedContent(encoder.encodeToString(content));
                        images.add(guideImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                guideContent.setImages(images);;
            }
        }
    }
}
