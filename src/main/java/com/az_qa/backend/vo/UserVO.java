/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.vo;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Value object that represents a user in the API layer.
 */
public class UserVO {
  private Long id;

  @NotBlank(message = "User name is required")
  private String name;

  @NotBlank(message = "User last name is required")
  private String lastName;

  @NotBlank(message = "User email is required")
  private String email;

  @NotBlank(message = "User password is required")
  private String password;

  private Boolean isActive;

  private Long roleId;

  private RoleVO rolePermission;

  public UserVO() {}

  /**
   * Creates a user value object with all fields.
   *
   * @param id       user identifier
   * @param name     user name
   * @param lastName user last name
   * @param email    user email
   * @param password user password
   * @param isActive user status
   * @param roleId   user role identifier
   */
  public UserVO(
      Long id,
      String name,
      String lastName,
      String email,
      String password,
      Boolean isActive,
      Long roleId) {
    this.id = id;
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isActive = isActive;
    this.roleId = roleId;
  }

  /**
   * Creates a user value object with all fields, including a RoleVO.
   *
   * @param id             user identifier
   * @param name           user name
   * @param lastName       user last name
   * @param email          user email
   * @param password       user password
   * @param isActive       user status
   * @param roleId         user role identifier
   * @param rolePermission user role permissions
   */
  public UserVO(
      Long id,
      String name,
      String lastName,
      String email,
      String password,
      Boolean isActive,
      Long roleId,
      RoleVO rolePermission) {
    this.id = id;
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isActive = isActive;
    this.roleId = roleId;
    this.rolePermission = rolePermission;
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

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public RoleVO getRolePermission() {
    return rolePermission;
  }

  public void setRolePermission(RoleVO rolePermission) {
    this.rolePermission = rolePermission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserVO that = (UserVO) o;

    if (!Objects.equals(id, that.id)) {
      return false;
    }
    if (!Objects.equals(name, that.name)) {
      return false;
    }
    if (!Objects.equals(lastName, that.lastName)) {
      return false;
    }
    if (!Objects.equals(email, that.email)) {
      return false;
    }
    if (!Objects.equals(password, that.password)) {
      return false;
    }
    if (!Objects.equals(isActive, that.isActive)) {
      return false;
    }
    if (!Objects.equals(roleId, that.roleId)) {
      return false;
    }
    return Objects.equals(rolePermission, that.rolePermission);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
    result = 31 * result + (rolePermission != null ? rolePermission.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "UserVO{"
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
        + ", roleId="
        + roleId
        + ", rolePermission='"
        + rolePermission
        + '\''
        + '}';
  }
}
