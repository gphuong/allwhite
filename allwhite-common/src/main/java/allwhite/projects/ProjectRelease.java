package allwhite.projects;

import allwhite.projects.support.Version;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.util.regex.Pattern;

@Embeddable
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRelease implements Comparable<ProjectRelease> {
    private static final Pattern PRERELEASE_PATTERN = Pattern.compile("[A-Za-z0-9\\.\\-]+?(M|RC)\\d+");
    private static final String SNAPSHOT_SUFFIX = "SNAPSHOT";
    private static final String VERSION_PLACEHOLDER = "{version}";
    private static final String VERSION_PATTERN = Pattern.quote(VERSION_PLACEHOLDER);

    public enum ReleaseStatus {
        SNAPSHOT, PRERELEASE, GENERAL_AVAILABILITY;

        public static ReleaseStatus getFromVersion(String version) {
            Assert.notNull(version, "Version must not be null");
            if (version.endsWith(SNAPSHOT_SUFFIX)) {
                return SNAPSHOT;
            }
            if (PRERELEASE_PATTERN.matcher(version).matches()) {
                return PRERELEASE;
            }
            return GENERAL_AVAILABILITY;
        }
    }

    private String versionName;
    private ReleaseStatus releaseStatus;
    private boolean isCurrent;
    private String refDocUrl;
    private String apiDocUrl;
    private String groupId;
    private String artifactId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ProjectRepository repository;

    public ProjectRelease() {
    }

    public ProjectRelease(String versionName, ReleaseStatus releaseStatus, boolean isCurrent,
                          String refDocUrl, String apiDocUrl, String groupId, String artifactId) {
        setVersion(versionName);
        if (releaseStatus != null) {
            this.releaseStatus = releaseStatus;
        }
        this.isCurrent = isCurrent;
        this.refDocUrl = refDocUrl;
        this.apiDocUrl = apiDocUrl;
        this.groupId = groupId;
        this.artifactId = artifactId;
        repository = ProjectRepository.get(versionName, this.releaseStatus);
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public boolean isGeneralAvailability() {
        return releaseStatus == ReleaseStatus.GENERAL_AVAILABILITY;
    }

    public boolean isPreRelease() {
        return releaseStatus == ReleaseStatus.PRERELEASE;
    }

    public boolean isSnapshot() {
        return releaseStatus == ReleaseStatus.SNAPSHOT;
    }

    public ReleaseStatus getReleaseStatus() {
        return releaseStatus;
    }

    public String getVersion() {
        return versionName;
    }

    public String getVersionDisplayName() {
        return getVersionDisplayName(true);
    }

    public String getVersionDisplayName(boolean includePreReleaseDescription) {
        String versionNumber = versionName;
        String versionLabel = "";
        if (versionName.contains(".")) {
            versionNumber = versionName.substring(0, versionName.lastIndexOf("."));
            versionLabel = " " + versionName.substring(versionName.lastIndexOf(".") + 1);
            if (versionLabel.contains("SNAPSHOT") || versionLabel.equals(" RELEASE")) {
                versionLabel = "";
            }
        }
        return versionNumber + versionLabel;
    }

    public boolean hasRefDocUrl() {
        return refDocUrl != null && !refDocUrl.isEmpty();
    }

    public boolean hasApiDocUrl() {
        return apiDocUrl != null && !apiDocUrl.isEmpty();
    }

    public void setVersion(String versionName) {
        this.releaseStatus = ReleaseStatus.getFromVersion(versionName);
        repository = ProjectRepository.get(versionName, this.releaseStatus);
        this.versionName = versionName;
    }

    public String getRefDocUrl() {
        return refDocUrl;
    }

    public String getApiDocUrl() {
        return apiDocUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public ProjectRepository getRepository() {
        return repository;
    }

    public void setReleaseStatus(ReleaseStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public void setRefDocUrl(String refDocUrl) {
        this.refDocUrl = refDocUrl;
    }

    public void setApiDocUrl(String apiDocUrl) {
        this.apiDocUrl = apiDocUrl;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setRepository(ProjectRepository repository) {
        this.repository = repository;
    }

    public void replaceVersionPattern() {
        String version = getVersion();
        setApiDocUrl(getApiDocUrl().replaceAll(VERSION_PATTERN, version));
        setRefDocUrl(getApiDocUrl().replaceAll(VERSION_PATTERN, version));
    }

    public ProjectRelease createWithVersionPattern() {
        String version = getVersion();
        ProjectRelease release = new ProjectRelease(version, releaseStatus, isCurrent, refDocUrl, apiDocUrl, groupId,
                artifactId);
        release.setApiDocUrl(getApiDocUrl().replaceAll(version, VERSION_PLACEHOLDER));
        release.setRefDocUrl(getRefDocUrl().replaceAll(version, VERSION_PLACEHOLDER));
        return release;
    }

    @Override
    public int compareTo(ProjectRelease other) {
        if (other == null)
            return -1;
        Version thisVersion = Version.build(getVersion());
        Version otherVersion = Version.build(other.getVersion());

        if (thisVersion == null && otherVersion == null) {
            return 0;
        } else if (thisVersion == null) {
            return -1;
        } else if (otherVersion == null) {
            return 1;
        }
        if (thisVersion.equals(otherVersion)) {
            return 0;
        }

        String[] theseParts = thisVersion.getParts();
        String[] otherParts = otherVersion.getParts();

        int i = 0;
        for (; i < theseParts.length && i < otherParts.length; i++) {
            if (theseParts[i].equals(otherParts[i])) {
                continue;
            }
            boolean is1Number = isNumber(theseParts[i]);
            boolean is2Number = isNumber(otherParts[i]);
            if (is1Number && !is2Number) {
                return 1;
            }
            if (is2Number && !is1Number) {
                return -1;
            }
            if (is1Number) {
                return Long.valueOf(theseParts[i]).compareTo(Long.valueOf(otherParts[i]));
            }
            return theseParts[i].compareTo(otherParts[i]);
        }
        if (i < theseParts.length) {
            return isNumber(theseParts[i]) ? 1 : -1;
        }
        if (i < otherParts.length) {
            return isNumber(otherParts[i]) ? -1 : 1;
        }
        return 0;
    }

    private boolean isNumber(String str) {
        return str.matches("\\d+");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof ProjectRelease))
            return false;
        ProjectRelease that = (ProjectRelease) obj;
        if (!versionName.equals(that.versionName))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return versionName.hashCode();
    }

    @Override
    public String toString() {
        return "ProjectRelease{" + "versionname='" + versionName + '\'' + ", release=" + releaseStatus
                + ", refDocUrl='" + refDocUrl + '\'' + ", apiDocUrl='" + apiDocUrl + '\'' + '}';
    }
}
