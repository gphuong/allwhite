package allwhite.tools.support;

import allwhite.tool.FileDownload;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class EclipseXmlDownload implements FileDownload {
    private String os;
    private String version;
    @JacksonXmlProperty(isAttribute = true, localName = "eclipse-version")
    private String eclipseVersion;
    private String size;
    private String description;
    private String file;
    private String md5;
    private String sha1;
    private String bucket;

    @Override
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEclipseVersion() {
        return eclipseVersion;
    }

    public void setEclipseVersion(String eclipseVersion) {
        this.eclipseVersion = eclipseVersion;
    }

    @Override
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    @Override
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
