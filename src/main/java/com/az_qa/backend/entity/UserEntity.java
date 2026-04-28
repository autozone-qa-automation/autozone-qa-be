/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.entity;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * JPA entity that maps the users table.
 */
@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idUser")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "lastName")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "hashPassword")
  private String hashPassword;

  @Column(name = "isActive")
  private Boolean isActive;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "idRoles", nullable = false)
  private RoleEntity role;

  public UserEntity() {}

  public UserEntity(
      String name,
      String lastName,
      String email,
      String hashPassword,
      Boolean isActive,
      RoleEntity role) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.hashPassword = hashPassword;
    this.isActive = isActive;
    this.role = role;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHashPassword() {
    return hashPassword;
  }

  public void setHashPassword(String hashPassword) {
    this.hashPassword = hashPassword;
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
        && Objects.equals(hashPassword, user.hashPassword)
        && Objects.equals(isActive, user.isActive)
        && Objects.equals(role, user.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, lastName, email, hashPassword, isActive, role);
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
        + ", hashPassword='"
        + hashPassword
        + '\''
        + ", isActive="
        + isActive
        + ", role="
        + role
        + '}';
  }
}
