/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.vo;

import com.az_qa.backend.enumeration.ReleaseStatus;
import java.time.LocalDate;
import java.util.List;

/**
 * Value Object representing a release in a report response.
 * Contains the hierarchical structure: release → services → features → test cases.
 */
public class ReportReleaseVO {

  private Long releaseId;
  private String releaseName;
  private String releaseDescription;
  private String releaseVersion;
  private ReleaseStatus releaseStatus;
  private List<String> releaseTags;
  private LocalDate releaseCreationDate;
  private LocalDate releaseLaunchDate;
  private List<ReportServiceVO> services;

  public ReportReleaseVO() {}

  public ReportReleaseVO(
      Long releaseId,
      String releaseName,
      String releaseDescription,
      String releaseVersion,
      ReleaseStatus releaseStatus,
      List<String> releaseTags,
      LocalDate releaseCreationDate,
      LocalDate releaseLaunchDate,
      List<ReportServiceVO> services) {
    this.releaseId = releaseId;
    this.releaseName = releaseName;
    this.releaseDescription = releaseDescription;
    this.releaseVersion = releaseVersion;
    this.releaseStatus = releaseStatus;
    this.releaseTags = releaseTags;
    this.releaseCreationDate = releaseCreationDate;
    this.releaseLaunchDate = releaseLaunchDate;
    this.services = services;
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

  public String getReleaseVersion() {
    return releaseVersion;
  }

  public void setReleaseVersion(String releaseVersion) {
    this.releaseVersion = releaseVersion;
  }

  public ReleaseStatus getReleaseStatus() {
    return releaseStatus;
  }

  public void setReleaseStatus(ReleaseStatus releaseStatus) {
    this.releaseStatus = releaseStatus;
  }

  public List<String> getReleaseTags() {
    return releaseTags;
  }

  public void setReleaseTags(List<String> releaseTags) {
    this.releaseTags = releaseTags;
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

  public List<ReportServiceVO> getServices() {
    return services;
  }

  public void setServices(List<ReportServiceVO> services) {
    this.services = services;
  }
}
