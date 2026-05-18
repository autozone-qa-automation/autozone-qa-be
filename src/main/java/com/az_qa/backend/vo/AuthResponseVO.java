package com.az_qa.backend.vo;

public class AuthResponseVO {
  private String token;
  private UserVO user;

  public AuthResponseVO() {}

  public AuthResponseVO(String token, UserVO user) {
    this.token = token;
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserVO getUser() {
    return user;
  }

  public void setUser(UserVO user) {
    this.user = user;
  }
}
