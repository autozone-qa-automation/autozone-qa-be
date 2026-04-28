package com.az_qa.backend.dto.response;

import com.az_qa.backend.entity.RoleEntity;

public class UserResponse {
  private Long id;
  private String name;
  private String email;
  private Boolean isActive;
  private String role;

  public UserResponse() {}

  public UserResponse(Long id, String name, String email, Boolean isActive, RoleEntity role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.isActive = isActive;
    this.role = role.getPermission();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public String getRole() {
    return role;
  }
}
