/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.entity;

import com.az_qa.backend.enumeration.UserRole;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * JPA entity that maps the roles table.
 */
@Entity
@Table(name = "roles")
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idRole")
  private Long id;

  @Column(name = "permission", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole permission;

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private List<UserEntity> users = new ArrayList<>();

  public RoleEntity() {}

  public RoleEntity(Long id, UserRole permission) {
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
    if (o == null || getClass() != o.getClass()) return false;
    RoleEntity role = (RoleEntity) o;
    return id == role.id && Objects.equals(permission, role.permission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, permission);
  }

  @Override
  public String toString() {
    return "Role{" + "id=" + id + ", permission='" + permission + '\'' + '}';
  }
}
