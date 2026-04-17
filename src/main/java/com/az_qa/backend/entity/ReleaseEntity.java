/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.entity;

import com.az_qa.backend.enumeration.ReleaseStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * JPA entity representing a software release in the Autozone QA system.
 * This entity is stored in the database and maps release metadata to the
 * underlying {@code releases} table.
 */
@Entity
@Table(name = "releases")
public class ReleaseEntity {

  /** Primary key for the release record. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "release_id")
  private Long releaseId;

  /** The release name. Required. */
  @Column(name = "release_name", nullable = false)
  private String releaseName;

  /** A description of the release. Required. */
  @Column(name = "release_description", nullable = false, columnDefinition = "TEXT")
  private String releaseDescription;

  /** Creation date for the release. Required. */
  @Column(name = "release_creation_date", nullable = false)
  private LocalDate releaseCreationDate;

  /** Planned launch date for the release. */
  @Column(name = "release_launch_date")
  private LocalDate releaseLaunchDate;

  /** Release version identifier. Required. */
  @Column(name = "release_version", nullable = false)
  private String releaseVersion;

  /** Optional tags used to categorize the release. */
  @Column(name = "release_tags")
  private String releaseTags;

  /** Current status of the release. */
  @Enumerated(EnumType.STRING)
  @Column(name = "release_status", nullable = false)
  private ReleaseStatus releaseStatus;

  /** Associated service for the release. */
  @Column(name = "release_service")
  private String releaseService;

  /**
   * Default constructor required by JPA.
   */
  public ReleaseEntity() {}

  /**
   * Constructs a new {@code ReleaseEntity} with the specified values.
   *
   * @param releaseName         the release name
   * @param releaseDescription  the release description
   * @param releaseCreationDate the release creation date
   * @param releaseLaunchDate   the release launch date
   * @param releaseVersion      the release version
   * @param releaseTags         optional release tags
   * @param releaseStatus       the release status
   * @param releaseService      optional service associated with the release
   */
  public ReleaseEntity(
      String releaseName,
      String releaseDescription,
      LocalDate releaseCreationDate,
      LocalDate releaseLaunchDate,
      String releaseVersion,
      String releaseTags,
      ReleaseStatus releaseStatus,
      String releaseService) {
    this.releaseName = releaseName;
    this.releaseDescription = releaseDescription;
    this.releaseCreationDate = releaseCreationDate;
    this.releaseLaunchDate = releaseLaunchDate;
    this.releaseVersion = releaseVersion;
    this.releaseTags = releaseTags;
    this.releaseStatus = releaseStatus;
    this.releaseService = releaseService;
  }

  /**
   * Returns the primary key of the release.
   *
   * @return the release identifier
   */
  public Long getReleaseId() {
    return releaseId;
  }

  /**
   * Sets the primary key for the release.
   *
   * @param releaseId the release identifier
   */
  public void setReleaseId(Long releaseId) {
    this.releaseId = releaseId;
  }

  /**
   * Returns the name of the release.
   *
   * @return the release name
   */
  public String getReleaseName() {
    return releaseName;
  }

  /**
   * Sets the name of the release.
   *
   * @param releaseName the release name
   */
  public void setReleaseName(String releaseName) {
    this.releaseName = releaseName;
  }

  /**
   * Returns the release description.
   *
   * @return the release description
   */
  public String getReleaseDescription() {
    return releaseDescription;
  }

  /**
   * Sets the release description.
   *
   * @param releaseDescription the release description
   */
  public void setReleaseDescription(String releaseDescription) {
    this.releaseDescription = releaseDescription;
  }

  /**
   * Returns the release creation date.
   *
   * @return the release creation date
   */
  public LocalDate getReleaseCreationDate() {
    return releaseCreationDate;
  }

  /**
   * Sets the release creation date.
   *
   * @param releaseCreationDate the release creation date
   */
  public void setReleaseCreationDate(LocalDate releaseCreationDate) {
    this.releaseCreationDate = releaseCreationDate;
  }

  /**
   * Returns the planned launch date for the release.
   *
   * @return the release launch date
   */
  public LocalDate getReleaseLaunchDate() {
    return releaseLaunchDate;
  }

  /**
   * Sets the planned launch date for the release.
   *
   * @param releaseLaunchDate the release launch date
   */
  public void setReleaseLaunchDate(LocalDate releaseLaunchDate) {
    this.releaseLaunchDate = releaseLaunchDate;
  }

  /**
   * Returns the release version.
   *
   * @return the release version
   */
  public String getReleaseVersion() {
    return releaseVersion;
  }

  /**
   * Sets the release version.
   *
   * @param releaseVersion the release version
   */
  public void setReleaseVersion(String releaseVersion) {
    this.releaseVersion = releaseVersion;
  }

  /**
   * Returns the release tags.
   *
   * @return the release tags
   */
  public String getReleaseTags() {
    return releaseTags;
  }

  /**
   * Sets the release tags.
   *
   * @param releaseTags the release tags
   */
  public void setReleaseTags(String releaseTags) {
    this.releaseTags = releaseTags;
  }

  /**
   * Returns the current status of the release.
   *
   * @return the release status
   */
  public ReleaseStatus getReleaseStatus() {
    return releaseStatus;
  }

  /**
   * Sets the current status of the release.
   *
   * @param releaseStatus the release status
   */
  public void setReleaseStatus(ReleaseStatus releaseStatus) {
    this.releaseStatus = releaseStatus;
  }

  /**
   * Returns the associated service for the release.
   *
   * @return the release service
   */
  public String getReleaseService() {
    return releaseService;
  }

  /**
   * Sets the associated service for the release.
   *
   * @param releaseService the release service
   */
  public void setReleaseService(String releaseService) {
    this.releaseService = releaseService;
  }

  /**
   * Compares this release entity to another object for equality.
   * <p>
   * Two {@code ReleaseEntity} objects are considered equal when all of their
   * persistent fields have the same values.
   *
   * @param o the object to compare with
   * @return {@code true} if the specified object is equal to this entity;
   *         {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReleaseEntity that = (ReleaseEntity) o;
    return Objects.equals(releaseId, that.releaseId)
        && Objects.equals(releaseName, that.releaseName)
        && Objects.equals(releaseDescription, that.releaseDescription)
        && Objects.equals(releaseCreationDate, that.releaseCreationDate)
        && Objects.equals(releaseLaunchDate, that.releaseLaunchDate)
        && Objects.equals(releaseVersion, that.releaseVersion)
        && Objects.equals(releaseTags, that.releaseTags)
        && releaseStatus == that.releaseStatus
        && Objects.equals(releaseService, that.releaseService);
  }

  /**
   * Computes the hash code for this entity based on its fields.
   *
   * @return the hash code value for this release entity
   */
  @Override
  public int hashCode() {
    return Objects.hash(
        releaseId,
        releaseName,
        releaseDescription,
        releaseCreationDate,
        releaseLaunchDate,
        releaseVersion,
        releaseTags,
        releaseStatus,
        releaseService);
  }

  /**
   * Returns a string representation of this release entity.
   *
   * @return a string containing the entity field values
   */
  @Override
  public String toString() {
    return "ReleaseEntity{"
        + "releaseId="
        + releaseId
        + ", releaseName='"
        + releaseName
        + '\''
        + ", releaseDescription='"
        + releaseDescription
        + '\''
        + ", releaseCreationDate="
        + releaseCreationDate
        + ", releaseLaunchDate="
        + releaseLaunchDate
        + ", releaseVersion='"
        + releaseVersion
        + '\''
        + ", releaseTags='"
        + releaseTags
        + '\''
        + ", releaseStatus="
        + releaseStatus
        + ", releaseService='"
        + releaseService
        + '\''
        + '}';
  }
}
