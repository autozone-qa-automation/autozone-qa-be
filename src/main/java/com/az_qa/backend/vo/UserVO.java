/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
  private String hashPassword;

  private Boolean isActive;

  @NotNull(message = "User role is required")
  private RoleVO role;

  public UserVO() {}

  public UserVO(
      Long id,
      String name,
      String lastName,
      String email,
      String hashPassword,
      Boolean isActive,
      RoleVO role) {
    this.id = id;
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.hashPassword = hashPassword;
    this.isActive = isActive;
    this.role = role;
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

  public RoleVO getRole() {
    return role;
  }

  public void setRole(RoleVO role) {
    this.role = role;
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
    if (!Objects.equals(hashPassword, that.hashPassword)) {
      return false;
    }
    if (!Objects.equals(isActive, that.isActive)) {
      return false;
    }
    return Objects.equals(role, that.role);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (hashPassword != null ? hashPassword.hashCode() : 0);
    result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
    result = 31 * result + (role != null ? role.hashCode() : 0);
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
