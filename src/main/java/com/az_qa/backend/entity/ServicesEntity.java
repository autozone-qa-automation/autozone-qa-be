/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Persistable;

/**
 * Persistence entity that represents a service record stored in the database.
 */
@Entity
@Table(name = "services")
public class ServicesEntity implements Serializable, Persistable<Long> {

  /** Serializable version identifier. */
  private static final long serialVersionUID = 1739356800001L;

  @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
  private List<FeatureEntity> features;

  /** Database identifier for the service. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Service name. */
  @Column(nullable = false)
  private String name;

  /** Service description. */
  @Column(name = "description")
  private String description;

  /** URLs associated with this service. */
  @OneToMany(
      mappedBy = "servicio",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<UrlEntity> urls;

  /** Entity new-state flag used by Spring Data persistence semantics. */
  @Transient private boolean isNew = false;

  /**
   * Creates an empty service entity.
   */
  public ServicesEntity() {}

  /**
   * Creates a service entity with all supported fields.
   *
   * @param id          service identifier
   * @param name        service name
   * @param description service description
   */
  public ServicesEntity(Long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  /**
   * Returns the service identifier.
   *
   * @return service id
   */
  @Override
  public Long getId() {
    return id;
  }

  /**
   * Indicates whether this entity should be treated as new by Spring Data.
   *
   * @return {@code true} when the entity is new; otherwise {@code false}
   */
  @Override
  public boolean isNew() {
    return isNew;
  }

  /**
   * Updates the new-state flag used by Spring Data.
   *
   * @param isNew {@code true} when the entity should be treated as new
   */
  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  /** Marks the entity as not new after it is persisted or loaded. */
  @PostPersist
  @PostLoad
  public void markNotNew() {
    this.isNew = false;
  }

  /**
   * Sets the service identifier.
   *
   * @param id service id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the service name.
   *
   * @return service name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the service name.
   *
   * @param name service name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the service description.
   *
   * @return service description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the service description.
   *
   * @param description service description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the URLs associated with this service.
   *
   * @return list of URLs
   */
  public List<UrlEntity> getUrls() {
    return urls;
  }

  /**
   * Sets the URLs associated with this service.
   *
   * @param urls list of URLs
   */
  public void setUrls(List<UrlEntity> urls) {
    this.urls = urls;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ServicesEntity that = (ServicesEntity) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    return description != null ? description.equals(that.description) : that.description == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ServicesEntity{"
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
}
