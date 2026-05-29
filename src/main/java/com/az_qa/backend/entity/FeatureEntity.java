/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "features")
public class FeatureEntity {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idServices")
  private ServicesEntity service;

  @OneToMany(mappedBy = "feature", fetch = FetchType.LAZY)
  private java.util.List<TestCasesEntity> testCases;

  @OneToMany(mappedBy = "feature", fetch = FetchType.LAZY)
  private java.util.List<ReleasedFeaturesEntity> releases;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idFeature")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "isActive")
  private Boolean isActive = true;

  public FeatureEntity() {}

  public FeatureEntity(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ServicesEntity getService() {
    return service;
  }

  public void setService(ServicesEntity service) {
    this.service = service;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    FeatureEntity feature = (FeatureEntity) o;
    return Objects.equals(id, feature.id)
        && Objects.equals(name, feature.name)
        && Objects.equals(description, feature.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description);
  }

  @Override
  public String toString() {
    return "Feature{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }

  public boolean isActive() {
    return isActive != null && isActive;
  }

  public void setActive(boolean active) {
    this.isActive = active;
  }
  
}
