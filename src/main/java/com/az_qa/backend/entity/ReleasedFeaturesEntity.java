package com.az_qa.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


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