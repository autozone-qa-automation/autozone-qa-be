/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.vo;

import com.az_qa.backend.enumeration.UserRole;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Value object that represents a role in the API layer.
 */
public class RoleVO {

  private Long id;

  @NotBlank(message = "Role permission is required")
  private UserRole permission;

  public RoleVO() {}

  public RoleVO(Long id, UserRole permission) {
    this.id = id;
    this.permission = permission;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserRole getPermission() {
    return permission;
  }

  public void setPermission(UserRole permission) {
    this.permission = permission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoleVO roleVO = (RoleVO) o;

    if (!Objects.equals(id, roleVO.id)) {
      return false;
    }
    return Objects.equals(permission, roleVO.permission);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (permission != null ? permission.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RoleVO{" + "id=" + id + ", permission='" + permission + '\'' + '}';
  }
}
