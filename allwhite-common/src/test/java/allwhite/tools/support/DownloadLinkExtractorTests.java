package allwhite.tools.support;

import allwhite.tool.Download;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DownloadLinkExtractorTests {

    private Download download;
    private DownloadLinkExtractor extractor;

    @Before
    public void setUp() {
        download = new Download();
        download.setDescription("Mac OS X (Cocoa)");
        download.setOs("mac");
        download.setFile("release/STS/3.3.0/dist/e4.3/spring-tool-suite-3.3.0.RELEASE-e4.3-macosx-cocoa-installer.dmg");
        download.setBucket("http://dist.springsource.com/");
        download.setEclipseVersion("4.3");
        download.setSize("373MB");
        download.setVersion("3.3.0.RELEASE");

        extractor = new DownloadLinkExtractor();
    }

    @Test
    public void extractsUrl() {
        assertThat(extractor.createDownloadLink(download).getUrl(),
                equalTo("http://dist.springsource.com/release/STS/3.3.0/dist/e4.3/spring-tool-suite-3.3.0.RELEASE-e4.3-macosx-cocoa-installer.dmg"));
    }

    @Test
    public void extractsFielSize() {
        assertThat(extractor.createDownloadLink(download).getFileSize(), equalTo("373MB"));
    }

    @Test
    public void extractSimpleFileType() {
        assertThat(extractor.createDownloadLink(download).getFileType(), equalTo("dmg"));
    }

    @Test
    public void extractsTarGzFileType() {
        download.setFile("release/STS/3.3.0/dist/e4.3/spring-tool-suite-3.3.0.RELEASE-e4.3-macosx-cocoa-installer.tar.gz");

        assertThat(extractor.createDownloadLink(download).getFileType(), equalTo("tar.gz"));
    }

    @Test
    public void extractsOs() {
        assertThat(extractor.createDownloadLink(download).getOs(), equalTo("mac"));
    }

    @Test
    public void extractsArchitecture() {
        assertThat(extractor.createDownloadLink(download).getArchitecture(), equalTo("32"));
    }
}
