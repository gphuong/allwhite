package allwhite.tools;

public class UpdateSiteArchive {
    private String version;
    private String url;
    private String fileSize;

    public UpdateSiteArchive(String version, String url, String fileSize) {
        this.version = version;
        this.url = url;
        this.fileSize = fileSize;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public String getFileName(){
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
