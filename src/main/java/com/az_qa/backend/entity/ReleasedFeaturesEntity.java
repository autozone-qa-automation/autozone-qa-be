package com.az_qa.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA entity that links a release with one of the features included in it.
 * This entity maps the many-to-many release-feature relationship through the
 * {@code released_features} table.
 */
@Entity
@Table(name = "released_features")
public class ReleasedFeaturesEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idRelease")
  private ReleaseEntity release;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idFeature")
  private FeatureEntity feature;

  public ReleasedFeaturesEntity() {}

  /**
   * Constructs a released-feature relationship with the specified release and
   * feature.
   *
   * @param release the release that includes the feature
   * @param feature the feature included in the release
   */
  public ReleasedFeaturesEntity(ReleaseEntity release, FeatureEntity feature) {
    this.release = release;
    this.feature = feature;
  }

  public Long getId() {
    return id;
  }

  public ReleaseEntity getRelease() {
    return release;
  }

  public void setRelease(ReleaseEntity release) {
    this.release = release;
  }

  public FeatureEntity getFeature() {
    return feature;
  }

  public void setFeature(FeatureEntity feature) {
    this.feature = feature;
  }
}
