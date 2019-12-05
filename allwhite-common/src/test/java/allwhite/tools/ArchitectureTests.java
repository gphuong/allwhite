package allwhite.tools;

import allwhite.tools.Architecture;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class ArchitectureTests {

    @Test
    public void hasADisplayNameForCocoa32Bit() throws Exception {
        DownloadLink link = new DownloadLink("", "", "", "", "32");
        Architecture architecture = new Architecture("Mac OS X (Cocoa)", Arrays.asList(link));
        assertThat(architecture.getDisplayName(), is("COCOA, 32BIT"));
    }

    @Test
    public void hasADisplayNameForCocoa64Bit() {
        DownloadLink link = new DownloadLink("", "", "", "", "64");
        Architecture architecture = new Architecture("Mac OS X (Cocoa, 64Bit)", Arrays.asList(link));
        assertThat(architecture.getDisplayName(), is("COCOA, 64BIT"));
    }

    @Test
    public void hasADisplayNameForGTK32Bit() {
        DownloadLink link = new DownloadLink("", "", "", "", "32");
        Architecture architecture = new Architecture("Linux (GTK)", Arrays.asList(link));
        assertThat(architecture.getDisplayName(), is("GTK, 32BIT"));
    }

    @Test
    public void hasADisplayNameForGTK64Bit() {
        DownloadLink link = new DownloadLink("", "", "", "", "64");
        Architecture architecture = new Architecture("Linux (GTK, 64Bit)", Arrays.asList(link));
        assertThat(architecture.getDisplayName(), is("GTK, 64BIT"));
    }

    @Test
    public void hasADisplayNameForWIN32Bit() {
        DownloadLink link = new DownloadLink("", "", "", "", "32");
        Architecture architecture = new Architecture("Windows", Arrays.asList(link));
        assertThat(architecture.getDisplayName(), is("WIN, 32BIT"));
    }

    @Test
    public void hasADisplayNameForWIN64Bit() {
        DownloadLink link = new DownloadLink("", "", "", "", "64");
        Architecture architecture = new Architecture("Windows (64bit)", Arrays.asList(link));
        assertThat(architecture.getDisplayName(), is("WIN, 64BIT"));
    }
}
