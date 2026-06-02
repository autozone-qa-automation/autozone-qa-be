/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.vo;

import com.az_qa.backend.enumeration.ReleaseStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Value Object representing a software release in the Autozone QA system.
 * This class encapsulates the data related to a release, including its
 * metadata,
 * status, and associated service information.
 */
public class ReleaseVO {
  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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

  private List<String> releaseTags;

  @NotNull(message = "Release status is required")
  private ReleaseStatus releaseStatus;

  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<String> releaseServices;

  @NotNull(message = "Release service id is required")
  private Long releaseServiceId;

  @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private List<Long> releaseFeatureIds;

  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<FeatureVO> releaseFeatures;

  public ReleaseVO() {}

  /**
   * Constructs a new ReleaseVO with the specified parameters.
   *
   * @param releaseId           the unique identifier for the release
   * @param releaseName         the name of the release
   * @param releaseDescription  the description of the release
   * @param releaseCreationDate the creation date of the release
   * @param releaseLaunchDate   the launch date of the release
   * @param releaseVersion      the version number of the release
   * @param releaseTags         the tags associated with the release
   * @param releaseStatus       the status of the release
   */
  public ReleaseVO(
      Long releaseId,
      String releaseName,
      String releaseDescription,
      LocalDate releaseCreationDate,
      LocalDate releaseLaunchDate,
      String releaseVersion,
      List<String> releaseTags,
      ReleaseStatus releaseStatus,
      List<String> releaseServices,
      Long releaseServiceId,
      List<Long> releaseFeatureIds,
      List<FeatureVO> releaseFeatures) {
    this.releaseId = releaseId;
    this.releaseName = releaseName;
    this.releaseDescription = releaseDescription;
    this.releaseCreationDate = releaseCreationDate;
    this.releaseLaunchDate = releaseLaunchDate;
    this.releaseVersion = releaseVersion;
    this.releaseTags = releaseTags;
    this.releaseStatus = releaseStatus;
    this.releaseServices = releaseServices;
    this.releaseServiceId = releaseServiceId;
    this.releaseFeatureIds = releaseFeatureIds;
    this.releaseFeatures = releaseFeatures;
  }

  public ReleaseVO(
      Long releaseId,
      String releaseName,
      String releaseDescription,
      LocalDate releaseCreationDate,
      LocalDate releaseLaunchDate,
      String releaseVersion,
      List<String> releaseTags,
      ReleaseStatus releaseStatus,
      List<String> releaseServices,
      Long releaseServiceId) {
    this(
        releaseId,
        releaseName,
        releaseDescription,
        releaseCreationDate,
        releaseLaunchDate,
        releaseVersion,
        releaseTags,
        releaseStatus,
        releaseServices,
        releaseServiceId,
        null,
        null);
  }

  public List<String> getReleaseServices() {
    return releaseServices;
  }

  public void setReleaseServices(List<String> releaseServices) {
    this.releaseServices = releaseServices;
  }

  public Long getReleaseServiceId() {
    return releaseServiceId;
  }

  public void setReleaseServiceId(Long releaseServiceId) {
    this.releaseServiceId = releaseServiceId;
  }

  public List<Long> getReleaseFeatureIds() {
    return releaseFeatureIds;
  }

  public void setReleaseFeatureIds(List<Long> releaseFeatureIds) {
    this.releaseFeatureIds = releaseFeatureIds;
  }

  public List<FeatureVO> getReleaseFeatures() {
    return releaseFeatures;
  }

  public void setReleaseFeatures(List<FeatureVO> releaseFeatures) {
    this.releaseFeatures = releaseFeatures;
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

  public List<String> getReleaseTags() {
    return releaseTags;
  }

  public void setReleaseTags(List<String> releaseTags) {
    this.releaseTags = releaseTags;
  }

  public ReleaseStatus getReleaseStatus() {
    return releaseStatus;
  }

  public void setReleaseStatus(ReleaseStatus releaseStatus) {
    this.releaseStatus = releaseStatus;
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
    result = prime * result + ((releaseServices == null) ? 0 : releaseServices.hashCode());
    result = prime * result + ((releaseServiceId == null) ? 0 : releaseServiceId.hashCode());
    result = prime * result + ((releaseFeatures == null) ? 0 : releaseFeatures.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ReleaseVO other = (ReleaseVO) obj;
    if (releaseId == null) {
      if (other.releaseId != null) return false;
    } else if (!releaseId.equals(other.releaseId)) return false;
    if (releaseName == null) {
      if (other.releaseName != null) return false;
    } else if (!releaseName.equals(other.releaseName)) return false;
    if (releaseDescription == null) {
      if (other.releaseDescription != null) return false;
    } else if (!releaseDescription.equals(other.releaseDescription)) return false;
    if (releaseCreationDate == null) {
      if (other.releaseCreationDate != null) return false;
    } else if (!releaseCreationDate.equals(other.releaseCreationDate)) return false;
    if (releaseLaunchDate == null) {
      if (other.releaseLaunchDate != null) return false;
    } else if (!releaseLaunchDate.equals(other.releaseLaunchDate)) return false;
    if (releaseVersion == null) {
      if (other.releaseVersion != null) return false;
    } else if (!releaseVersion.equals(other.releaseVersion)) return false;
    if (releaseTags == null) {
      if (other.releaseTags != null) return false;
    } else if (!releaseTags.equals(other.releaseTags)) return false;
    if (releaseStatus == null) {
      if (other.releaseStatus != null) return false;
    } else if (!releaseStatus.equals(other.releaseStatus)) return false;
    if (releaseServices == null) {
      if (other.releaseServices != null) return false;
    } else if (!releaseServices.equals(other.releaseServices)) return false;
    if (releaseServiceId == null) {
      if (other.releaseServiceId != null) return false;
    } else if (!releaseServiceId.equals(other.releaseServiceId)) return false;
    if (releaseFeatures == null) {
      if (other.releaseFeatures != null) return false;
    } else if (!releaseFeatures.equals(other.releaseFeatures)) return false;
    return true;
  }

  @Override
  public String toString() {
    return "CreateReleaseVO [releaseId="
        + releaseId
        + ", releaseName="
        + releaseName
        + ", releaseDescription="
        + releaseDescription
        + ", releaseCreationDate="
        + releaseCreationDate
        + ", releaseLaunchDate="
        + releaseLaunchDate
        + ", releaseVersion="
        + releaseVersion
        + ", releaseTags="
        + releaseTags
        + ", releaseStatus="
        + releaseStatus
        + ", releaseServices="
        + releaseServices
        + ", releaseServiceId="
        + releaseServiceId
        + ", releaseFeatures="
        + releaseFeatures
        + "]";
  }
}
