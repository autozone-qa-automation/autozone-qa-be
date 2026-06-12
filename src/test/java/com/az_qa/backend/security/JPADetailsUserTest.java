package com.az_qa.backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.enumeration.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JPADetailsUserTest {

  private JPADetailsUser details;

  @BeforeEach
  void setUp() {

    RoleEntity role = new RoleEntity();
    role.setId(1L);
    role.setPermission(UserRole.ADMIN);

    UserEntity user = new UserEntity();
    user.setEmail("admin@test.com");
    user.setPassword("hashPassword");
    user.setRole(role);

    details = new JPADetailsUser(user);
  }

  @Test
  @DisplayName("getUsername: Debe retornar el email del usuario")
  public void getUsername_ReturnsEmail() {

    assertEquals("admin@test.com", details.getUsername());
  }

  @Test
  @DisplayName("getPassword: Debe retornar el password del usuario")
  public void getPassword_ReturnsPassword() {

    assertEquals("hashPassword", details.getPassword());
  }

  @Test
  @DisplayName("getAuthorities: Debe retornar el permiso asociado al rol")
  public void getAuthorities_ReturnsRolePermission() {

    String authority = details.getAuthorities().iterator().next().getAuthority();

    assertEquals("ADMIN", authority);
  }
}
