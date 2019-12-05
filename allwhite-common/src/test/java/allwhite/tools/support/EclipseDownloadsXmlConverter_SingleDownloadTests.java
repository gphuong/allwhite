package allwhite.tools.support;

import allwhite.tool.EclipseDownloads;
import org.junit.Before;

import java.util.Collections;

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
    }
}
