package com.az_qa.backend.dto.request;

import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
  @Size(min = 3, max = 50)
  private String name;

  @Size(min = 3, max = 50)
  private String email;

  public UserUpdateRequest() {}

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
}
