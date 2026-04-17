/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "features")
public class FeatureEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idFeature")
  long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "idServices")
  private long idServices;

  public FeatureEntity() {}

  public FeatureEntity(Long id, String name, String description, long idServices) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.idServices = idServices;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    FeatureEntity feature = (FeatureEntity) o;
    return id == feature.id
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
