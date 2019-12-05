package allwhite.tools.support;

import allwhite.tools.DownloadLink;
import allwhite.tools.EclipseDownloads;
import allwhite.tools.EclipsePackage;
import allwhite.tools.EclipseRelease;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EclipseDownloadsXmlConverter_TwoDownloadsForSameReleaseWithDifferentPackagesTests {
    private EclipseDownloads eclipseDownloads;

    @Before
    public void setup() {
        EclipseXmlDownload eclipseStandardDownload = new EclipseXmlDownload();
        eclipseStandardDownload.setOs("windows");
        eclipseStandardDownload.setDescription("Windows");
        eclipseStandardDownload.setFile("release/ECLIPSE/kepler/R/eclipse-standard-kepler-R-win32.zip");
        eclipseStandardDownload.setBucket("http://eclipseXmlDownload.springsource.com/");

        EclipseXmlDownload eclipseJavaEEDownload = new EclipseXmlDownload();
        eclipseJavaEEDownload.setOs("windows");
        eclipseJavaEEDownload.setDescription("Windows");
        eclipseJavaEEDownload.setFile("release/ECLIPSE/kepler/R/eclipse-jee-kepler-R-win32.zip");
        eclipseJavaEEDownload.setBucket("http://eclipseXmlDownload.springsource.com/");

        EclipseXmlPackage eclipseStandardPackage = new EclipseXmlPackage();
        eclipseStandardPackage.setName("Eclipse Standard 4.3 (Win32, 0MB)");
        eclipseStandardPackage.setEclipseXmlDownloads(Arrays.asList(eclipseStandardDownload));

        EclipseXmlPackage eclipseJavaEEPackage = new EclipseXmlPackage();
        eclipseJavaEEPackage.setName("Eclipse IDE for Java EE Developers (Win32, 245MB)");
        eclipseJavaEEPackage.setEclipseXmlDownloads(Arrays.asList(eclipseJavaEEDownload));

        EclipseXmlProduct eclipseXmlProduct = new EclipseXmlProduct();
        eclipseXmlProduct.setName("Eclipse Kepler Package Downloads (based on Eclipse 4.3)");
        eclipseXmlProduct.setPackages(Arrays.asList(eclipseStandardPackage, eclipseJavaEEPackage));
        EclipseXml eclipseXml = new EclipseXml();
        eclipseXml.setEclipseXmlProducts(Collections.singletonList(eclipseXmlProduct));

        EclipseDownloadsXmlConverter converter = new EclipseDownloadsXmlConverter();
        eclipseDownloads = converter.convert(eclipseXml);
    }

    @Test
    public void addsAPlatform() {
        assertThat(eclipseDownloads.getPlatforms().size(), equalTo(1));
        assertThat(eclipseDownloads.getPlatforms().get("windows").getName(), equalTo("Windows"));
    }

    @Test
    public void addsReleaseToThePlatform() {
        List<EclipseRelease> windowsProducts = eclipseDownloads.getPlatforms().get("windows").getReleases();
        assertThat(windowsProducts.size(), equalTo(1));
        assertThat(windowsProducts.get(0).getName(), equalTo("Eclipse Kepler"));
    }


    @Test
    public void addsTwoPackages() {
        List<EclipsePackage> windowsPackages =
                eclipseDownloads.getPlatforms().get("windows").getReleases().get(0).getPackages();
        assertThat(windowsPackages.size(), equalTo(2));
        assertThat(windowsPackages.get(0).getName(), equalTo("Eclipse Standard 4.3"));
        assertThat(windowsPackages.get(1).getName(), equalTo("Eclipse IDE for Java EE Developers"));
    }

    @Test
    public void addsDownloadLinkToEachPackage() {
        List<EclipsePackage> windowsPackages =
                eclipseDownloads.getPlatforms().get("windows").getReleases().get(0).getPackages();
        List<DownloadLink> standardDownloadLinks = windowsPackages.get(0).getArchitectures().get(0).getDownloadLinks();
        List<DownloadLink> javaEEDownloadLinks = windowsPackages.get(1).getArchitectures().get(0).getDownloadLinks();

        assertThat(standardDownloadLinks.size(), equalTo(1));
        assertThat(standardDownloadLinks.get(0).getOs(), equalTo("windows"));
        assertThat(standardDownloadLinks.get(0).getUrl(),
                equalTo("http://eclipseXmlDownload.springsource.com/release/ECLIPSE/kepler/R/eclipse-standard-kepler-R-win32.zip"));

        assertThat(javaEEDownloadLinks.size(), equalTo(1));
        assertThat(javaEEDownloadLinks.get(0).getOs(), equalTo("windows"));
        assertThat(javaEEDownloadLinks.get(0).getUrl(),
                equalTo("http://eclipseXmlDownload.springsource.com/release/ECLIPSE/kepler/R/eclipse-jee-kepler-R-win32.zip"));
    }
}
