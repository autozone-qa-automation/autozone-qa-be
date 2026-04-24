/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "features")
public class FeatureEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idFeature")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "idServices", insertable = false, updatable = false)
  private long idServices;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idServices")
  private ServicesEntity services;

  @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL)
  private List<TestCasesEntity> testCases = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "ReleasedFeatures",
      joinColumns = @JoinColumn(name = "feature_id"),
      inverseJoinColumns = @JoinColumn(name = "release_id"))
  private List<ReleaseEntity> releases = new ArrayList<>();

  public FeatureEntity() {}

  public FeatureEntity(String name, String description, long idServices) {
    this.name = name;
    this.description = description;
    this.idServices = idServices;
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

  public long getIdServices() {
    return idServices;
  }

  public void setIdServices(long idServices) {
    this.idServices = idServices;
  }

  public List<TestCasesEntity> getTestCases() {
    return testCases;
  }

  public void setTestCases(List<TestCasesEntity> testCases) {
    this.testCases = testCases;
  }

  public List<ReleaseEntity> getReleases() {
    return releases;
  }

  public void setReleases(List<ReleaseEntity> releases) {
    this.releases = releases;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    FeatureEntity feature = (FeatureEntity) o;
    return Objects.equals(id, feature.id)
        && Objects.equals(name, feature.name)
        && Objects.equals(description, feature.description)
        && idServices == feature.idServices;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, idServices);
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
        + ", idService='"
        + idServices
        + '\''
        + '}';
  }
}
