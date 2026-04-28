package com.az_qa.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRequest {
  @NotBlank
  @Size(min = 3, max = 50)
  private String name;

  @NotBlank
  @Size(min = 3, max = 50)
  private String lastName;

  @NotBlank
  @Size(min = 3, max = 50)
  private String email;

  @NotBlank
  @Size(min = 6, max = 100)
  private String hashPassword;

  private Boolean isActive;

  @NotNull private Long roleId;

  public UserRequest() {}

  public UserRequest(
      String name,
      String lastName,
      String email,
      String hashPassword,
      Boolean isActive,
      Long roleId) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.hashPassword = hashPassword;
    this.isActive = isActive;
    this.roleId = roleId;
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

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }
}
