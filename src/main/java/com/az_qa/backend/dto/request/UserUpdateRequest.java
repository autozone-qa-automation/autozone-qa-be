package com.az_qa.backend.dto.request;

import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
  @Size(min = 3, max = 50)
  private String name;

  @Size(min = 3, max = 50)
  private String lastName;

  @Size(min = 3, max = 50)
  private String email;

  @Size(min = 6, max = 100)
  private String hashPassword;

  private Boolean isActive;

  private Long roleId;

  public UserUpdateRequest() {}

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getLastName() {
    return lastName;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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
