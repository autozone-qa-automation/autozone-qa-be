/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.domain.Persistable;

/**
 * JPA entity that maps the users table.
 */
@Entity
@Table(name = "users")
public class UserEntity implements Serializable, Persistable<Long> {

  /**
   * Serializable version identifier.
   */
  private static final long serialVersionUID = 1739356800000L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idUser")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "lastName", nullable = false)
  private String lastName;

  @Email
  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "hashPassword", nullable = false)
  private String password;

  @Column(name = "isActive", nullable = false)
  private Boolean isActive;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "idRoles", nullable = false)
  private RoleEntity role;

  /**
   * Entity new-state flag used by Spring Data persistence semantics.
   * This field is not persisted to the database.
   */
  @Transient private boolean isNew = false;

  public UserEntity() {}

  /**
   * Creates a user entity with all supported fields.
   *
   * @param id       user identifier
   * @param name     user name
   * @param lastName user last name
   * @param email    user email
   * @param password user password
   * @param isActive user status
   * @param role     user role
   */
  public UserEntity(
      String name,
      String lastName,
      String email,
      String password,
      Boolean isActive,
      RoleEntity role) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isActive = isActive;
    this.role = role;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  /**
   * Marks the entity as not new after it is persisted or loaded.
   */
  @PostPersist
  @PostLoad
  public void markNotNew() {
    this.isNew = false;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public RoleEntity getRole() {
    return role;
  }

  public void setRole(RoleEntity role) {
    this.role = role;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserEntity user = (UserEntity) o;
    return id == user.id
        && Objects.equals(name, user.name)
        && Objects.equals(lastName, user.lastName)
        && Objects.equals(email, user.email)
        && Objects.equals(password, user.password)
        && Objects.equals(isActive, user.isActive)
        && Objects.equals(role, user.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, lastName, email, password, isActive, role);
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + ", isActive="
        + isActive
        + ", role="
        + role
        + '}';
  }
}
