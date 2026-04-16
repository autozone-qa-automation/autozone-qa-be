package com.az_qa.backend.vo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.az_qa.backend.enumeration.ReleaseStatus;

public class ReleaseVO {
    private Long releaseId;
    @NotBlank(message = "Release name is required")
    private String releaseName;
    @NotBlank(message = "Release description is required")
    private String releaseDescription;
    @NotNull(message = "Release creation date is required")
    private LocalDate releaseCreationDate;
    private LocalDate releaseLaunchDate;
    @NotBlank(message = "Release version is required")
    private String releaseVersion;
    private String releaseTags;
    @NotBlank(message = "Release status is required")
    private ReleaseStatus releaseStatus;
    private String releaseService;
    

    public ReleaseVO() {
    }

    public ReleaseVO(Long releaseId, String releaseName, String releaseDescription, LocalDate releaseCreationDate, LocalDate releaseLaunchDate, String releaseVersion, String releaseTags, ReleaseStatus releaseStatus, String releaseService) {
        this.releaseId = releaseId;
        this.releaseName = releaseName;
        this.releaseDescription = releaseDescription;
        this.releaseCreationDate = releaseCreationDate;
        this.releaseLaunchDate = releaseLaunchDate;
        this.releaseVersion = releaseVersion;
        this.releaseTags = releaseTags;
        this.releaseStatus = releaseStatus;
        this.releaseService = releaseService;
    }

    public Long getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Long releaseId) {
        this.releaseId = releaseId;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public String getReleaseDescription() {
        return releaseDescription;
    }

    public void setReleaseDescription(String releaseDescription) {
        this.releaseDescription = releaseDescription;
    }

    public LocalDate getReleaseCreationDate() {
        return releaseCreationDate;
    }

    public void setReleaseCreationDate(LocalDate releaseCreationDate) {
        this.releaseCreationDate = releaseCreationDate;
    }

    public LocalDate getReleaseLaunchDate() {
        return releaseLaunchDate;
    }

    public void setReleaseLaunchDate(LocalDate releaseLaunchDate) {
        this.releaseLaunchDate = releaseLaunchDate;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String getReleaseTags() {
        return releaseTags;
    }

    public void setReleaseTags(String releaseTags) {
        this.releaseTags = releaseTags;
    }

    public ReleaseStatus getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(ReleaseStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getReleaseService() {
        return releaseService;
    }

    public void setReleaseService(String releaseService) {
        this.releaseService = releaseService;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((releaseId == null) ? 0 : releaseId.hashCode());
        result = prime * result + ((releaseName == null) ? 0 : releaseName.hashCode());
        result = prime * result + ((releaseDescription == null) ? 0 : releaseDescription.hashCode());
        result = prime * result + ((releaseCreationDate == null) ? 0 : releaseCreationDate.hashCode());
        result = prime * result + ((releaseLaunchDate == null) ? 0 : releaseLaunchDate.hashCode());
        result = prime * result + ((releaseVersion == null) ? 0 : releaseVersion.hashCode());
        result = prime * result + ((releaseTags == null) ? 0 : releaseTags.hashCode());
        result = prime * result + ((releaseStatus == null) ? 0 : releaseStatus.hashCode());
        result = prime * result + ((releaseService == null) ? 0 : releaseService.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReleaseVO other = (ReleaseVO) obj;
        if (releaseId == null) {
            if (other.releaseId != null)
                return false;
        } else if (!releaseId.equals(other.releaseId))
            return false;
        if (releaseName == null) {
            if (other.releaseName != null)
                return false;
        } else if (!releaseName.equals(other.releaseName))
            return false;
        if (releaseDescription == null) {
            if (other.releaseDescription != null)
                return false;
        } else if (!releaseDescription.equals(other.releaseDescription))
            return false;
        if (releaseCreationDate == null) {
            if (other.releaseCreationDate != null)
                return false;
        } else if (!releaseCreationDate.equals(other.releaseCreationDate))
            return false;
        if (releaseLaunchDate == null) {
            if (other.releaseLaunchDate != null)
                return false;
        } else if (!releaseLaunchDate.equals(other.releaseLaunchDate))
            return false;
        if (releaseVersion == null) {
            if (other.releaseVersion != null)
                return false;
        } else if (!releaseVersion.equals(other.releaseVersion))
            return false;
        if (releaseTags == null) {
            if (other.releaseTags != null)
                return false;
        } else if (!releaseTags.equals(other.releaseTags))
            return false;
        if (releaseStatus == null) {
            if (other.releaseStatus != null)
                return false;
        } else if (!releaseStatus.equals(other.releaseStatus))
            return false;
        if (releaseService == null) {
            if (other.releaseService != null)
                return false;
        } else if (!releaseService.equals(other.releaseService))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CreateReleaseVO [releaseId=" + releaseId + ", releaseName=" + releaseName + ", releaseDescription="
                + releaseDescription + ", releaseCreationDate=" + releaseCreationDate + ", releaseLaunchDate="
                + releaseLaunchDate + ", releaseVersion=" + releaseVersion + ", releaseTags=" + releaseTags
                + ", releaseStatus=" + releaseStatus + ", releaseService=" + releaseService + "]";
    }
}
