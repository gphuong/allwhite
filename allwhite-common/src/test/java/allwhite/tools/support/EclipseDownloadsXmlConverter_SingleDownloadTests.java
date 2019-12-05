package allwhite.tools.support;

import allwhite.tools.DownloadLink;
import allwhite.tools.EclipseDownloads;
import allwhite.tools.EclipsePackage;
import allwhite.tools.EclipseRelease;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EclipseDownloadsXmlConverter_SingleDownloadTests {

    private EclipseDownloads eclipseDownloads;

    @Before
    public void setup() {
        EclipseXmlDownload eclipseXmlDownload = new EclipseXmlDownload();
        eclipseXmlDownload.setOs("mac");
        eclipseXmlDownload.setEclipseVersion("4.3.0");
        eclipseXmlDownload.setSize("196MB");
        eclipseXmlDownload.setDescription("Mac OS X (Cocoa)");
        eclipseXmlDownload.setFile("release/ECLIPSE/kepler/R/eclipse-standard-kepler-R-macosx-cocoa.tar.gz");
        eclipseXmlDownload.setBucket("http://eclipseXmlDownload.springsource.com/");

        EclipseXmlPackage eclipseXmlPackage = new EclipseXmlPackage();
        eclipseXmlPackage.setName("Eclipse Standard 4.3 (Win32, 0MB)");
        eclipseXmlPackage.setEclipseXmlDownloads(Collections.singletonList(eclipseXmlDownload));

        EclipseXmlProduct eclipseXmlProduct= new EclipseXmlProduct();
        eclipseXmlProduct.setName("Eclipse Kepler Package Downloads (based on Eclipse 4.3)");
        eclipseXmlProduct.setPackages(Collections.singletonList(eclipseXmlPackage));
        EclipseXml eclipseXml = new EclipseXml();
        eclipseXml.setEclipseXmlProducts(Collections.singletonList(eclipseXmlProduct));

        EclipseDownloadsXmlConverter converter = new EclipseDownloadsXmlConverter();
        eclipseDownloads = converter.convert(eclipseXml);
    }

    @Test
    public void addsAdPlatform() {
        assertThat(eclipseDownloads.getPlatforms().size(), equalTo(1));
        assertThat(eclipseDownloads.getPlatforms().get("mac").getName(), equalTo("Mac"));
    }

    @Test
    public void addsAProduct() {
        List<EclipseRelease> products = eclipseDownloads.getPlatforms().get("mac").getReleases();
        assertThat(products.size(), equalTo(1));
        assertThat(products.get(0).getName(), equalTo("Eclipse Kepler"));
        assertThat(products.get(0).getEclipseVersion(), equalTo("Eclipse 4.3"));
    }

    @Test
    public void addsAPackage() {
        List<EclipsePackage> packages = eclipseDownloads.getPlatforms().get("mac").getReleases().get(0).getPackages();
        assertThat(packages.size(), equalTo(1));
        assertThat(packages.get(0).getName(), equalTo("Eclipse Standard 4.3"));
    }

    @Test
    public void addsAnArchitecture() {
        EclipsePackage eclipsePackage = eclipseDownloads.getPlatforms().get("mac").getReleases().get(0).getPackages().get(0);
        assertThat(eclipsePackage.getArchitectures().size(), equalTo(1));
        assertThat(eclipsePackage.getArchitectures().get(0).getName(), equalTo("Mac OS X (Cocoa)"));
    }

    @Test
    public void addsADownloadLink() {
        EclipsePackage eclipsePackage =
                eclipseDownloads.getPlatforms().get("mac").getReleases().get(0).getPackages().get(0);
        List<DownloadLink> downloadLinks = eclipsePackage.getArchitectures().get(0).getDownloadLinks();
        assertThat(downloadLinks.size(), equalTo(1));
        assertThat(downloadLinks.get(0).getOs(), equalTo("mac"));
        assertThat(downloadLinks.get(0).getArchitecture(), equalTo("32"));
        assertThat(downloadLinks.get(0).getUrl(),
                equalTo("http://eclipseXmlDownload.springsource.com/release/ECLIPSE/kepler/R/eclipse-standard-kepler-R-macosx-cocoa.tar.gz"));
        assertThat(downloadLinks.get(0).getFileSize(), equalTo("196MB"));
        assertThat(downloadLinks.get(0).getFileType(), equalTo("tar.gz"));

    }
}
