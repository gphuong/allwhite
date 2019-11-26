package allwhite.renderer.guides;

import allwhite.renderer.RendererProperties;
import allwhite.renderer.github.GithubClient;
import allwhite.renderer.guides.content.GuideContentContributor;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
public class GuideRenderer {

    private final GithubClient githubClient;

    private final RendererProperties properties;

    private final List<GuideContentContributor> contributors;

    public GuideRenderer(GithubClient githubClient, RendererProperties properties,
                         List<GuideContentContributor> contributors) {
        this.githubClient = githubClient;
        this.properties = properties;
        this.contributors = contributors;
    }

    GuideContentResource render(GuideType type, String guideName) {
        GuideContentResource guideContent = new GuideContentResource();
        guideContent.setName(guideName);
        String repositoryName = type.getPrefix() + guideName;
        String org = this.properties.getGuides().getOrganization();
        String tempFilePrefix = org + "-" + repositoryName;

        File unzippedRoot = null;
        File zipball = null;

        try {
            byte[] download = this.githubClient.downloadRepositoryAsZipball(org, repositoryName);
            zipball = File.createTempFile(tempFilePrefix, ".zip");
            zipball.deleteOnExit();
            FileOutputStream zipOut = new FileOutputStream(zipball);
            zipOut.write(download);
            zipOut.close();

            try (ZipFile zipFile = new ZipFile(zipball)) {
                unzippedRoot = null;
                for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
                    ZipEntry entry = e.nextElement();
                    if (entry.isDirectory()) {
                        File dir = new File(zipball.getParent() + File.separator + entry.getName());
                        dir.mkdir();
                        if (unzippedRoot == null)
                            unzippedRoot = dir;
                    } else {
                        StreamUtils.copy(zipFile.getInputStream(entry), new FileOutputStream(zipball.getParent() + File.separator
                                + entry.getName()));
                    }
                }
            }
            for (GuideContentContributor contentContributor : this.contributors) {
                contentContributor.contribute(guideContent, unzippedRoot);
            }

            return guideContent;
        } catch (IOException e) {
            throw new IllegalStateException("Could not create temp file for source: " + tempFilePrefix, e);
        } finally {
            FileSystemUtils.deleteRecursively(zipball);
            FileSystemUtils.deleteRecursively(unzippedRoot);
        }
    }
}
