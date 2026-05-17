/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Value object for updating an existing user. Does not include id (path variable) or password.
 */
public class UpdateUserVO {

  @NotBlank(message = "User name is required")
  private String name;

  @NotBlank(message = "User last name is required")
  private String lastName;

  @NotBlank(message = "User email is required")
  private String email;

  @NotNull(message = "Active status is required")
  private Boolean isActive;

  @NotNull(message = "Role id is required")
  private Long roleId;

  public UpdateUserVO() {}

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
}
